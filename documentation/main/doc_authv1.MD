# Documentación Técnica de Arquitectura: Seguridad, Autenticación y RBAC Multi-Tenant

**Proyecto:** Notas System (SaaS Educativo Multi-Tenant)  
**Fecha de Actualización:** 24 de Julio de 2026  
**Versión:** 2.0.0 (Refactorización SaaS Stateless & RBAC Granular)  
**Autor:** Equipo de Ingeniería & Arquitectura Backend  

---

## 1. Resumen Ejecutivo y Contexto

El sistema evolucionó de un modelo monolítico tradicional (donde el usuario poseía un rol global y estaba directamente acoplado a una sola institución) hacia una **Arquitectura B2B SaaS Multi-Tenant**.

### Objetivos Clave Alcanzados
1. **Aislamiento Total de Datos (Multi-Tenancy):** Garantía mediante Hibernate `@TenantId` y `TenantContext` de que ninguna consulta pueda cruzar datos entre colegios.
2. **Rendimiento Stateless Elevado:** El filtro de seguridad (`JwtAuthenticationFilter`) ya **NO consulta la base de datos** en cada petición HTTP. Toda la información de sesión viaja dentro del payload del Token JWT.
3. **Control de Acceso Granular (RBAC + Permisos por Acción):** Separación entre **Identidad Global** (`User`) y **Autorización Local** (`UserTenantAccess`).
4. **Excepciones de Permisos por Usuario (Permission Overrides):** Capacidad de otorgar o revocar permisos específicos a un usuario en una institución determinada sin alterar el rol base.
5. **Auto-Aprovisionamiento Reutilizable (`UserProvisioningService`):** Lógica unificada para registrar perfiles de profesores, estudiantes y administradores sin duplicar cuentas globales.

---

## 2. Modelo Físico de Base de Datos y Entidades

El modelo diferencia estrictamente entre entidades globales (ciegas al colegio) y la tabla pivote de acceso de 3 vías.
[ Permission ] (Global)
    ^
    | (ManyToMany: role_permissions)
    v
[ Role ] (Global)
    ^
    | (ManyToOne)
    v
+-------------------------------------------------------------+
|                   UserTenantAccess                          |
|  (user_id + tenant_id + role_id + institutional_person_id)  |
|                                                             |
|  - grantedPermissions (ManyToMany: user_tenant_granted_p)   |
|  - revokedPermissions (ManyToMany: user_tenant_revoked_p)   |
+-------------------------------------------------------------+
^                      ^
| (ManyToOne)          | (ManyToOne)
[ User ]               [ Tenant ]
(Global)               (Global)

### Entidades Centrales
* **`User`** (`GlobalAuditableEntity`): Representa la identidad de la persona en la plataforma. Almacena únicamente `email`, `passwordHash`, `firstLogin` y `active`. No posee columna `tenant_id` ni `role`.
* **`Tenant`**: Representa la institución educativa o colegio registrado en el SaaS.
* **`Permission`** (`GlobalAuditableEntity`): Define acciones atómicas con la convención `módulo:acción` (Ej: `student:read`, `student:write`, `grade:read`, `grade:write`).
* **`Role`** (`GlobalAuditableEntity`): Agrupador de permisos globales (`ADMIN`, `TEACHER`, `STUDENT`, `MANAGER`). Hereda un `Set<Permission>` a través de la tabla intermedia `role_permissions`.
* **`UserTenantAccess`** (`GlobalAuditableEntity`): La Bóveda de 3 Vías. Unifica:
  * `user_id`: Identidad global.
  * `tenant_id`: Colegio en el que participa.
  * `role_id`: Rol asignado para ese colegio.
  * `institutionalPersonId`: ID del perfil físico (`Student`, `Teacher`, `Manager`).
  * `grantedPermissions`: Permisos adicionales concedidos individualmente.
  * `revokedPermissions`: Permisos del rol base retirados explícitamente.

---

## 3. Flujo Completo de Autenticación y Autorización

### A. Proceso de Login (`AuthService.java`)

[ Frontend Vue.js ] ──► POST /api/auth/login { email, password }
│
▼
1. Validar Credenciales via AuthenticationManager (BCrypt)
│
▼
2. Consultar UserTenantAccess.findByUserId(user.getId())
│
▼
3. Seleccionar Tenant Activo (Estrategia de Enrutamiento)
│
▼
4. Calcular Permisos Efectivos para ese Tenant
│
▼
5. Emitir JWT Enriquecido (Tenant + Rol + Permisos)

#### Fórmula de Permisos Efectivos:
$$\text{Permisos Efectivos} = (\text{Permisos del Rol Base} \cup \text{Permisos Concedidos}) \setminus \text{Permisos Revocados}$$

### B. Payload del Token JWT Emitido
El token devuelto al cliente encapsula todo el contexto necesario para ejecutar peticiones de forma *stateless*:

```json
{
  "sub": "usuario@colegio.edu",
  "uid": 15,
  "tenantId": 1,
  "role": "TEACHER",
  "permissions": [
    "student:read",
    "grade:read",
    "grade:write"
  ],
  "iss": "notas-system-api",
  "iat": 1721833000,
  "exp": 1721919400
}
```

---

### C. Intercepción de Peticiones HTTP (`JwtAuthenticationFilter.java`)
Cada petición recibida en endpoints protegidos pasa por el filtro de seguridad:

1. **Extracción:** Se lee la cabecera `Authorization: Bearer <token>`.
2. **Verificación Criptográfica:** Se valida la firma del JWT con la clave secreta HS256 (sin consultas a MySQL).
3. **Inyección en Hibernate:** Se extrae `tenantId` y se ejecuta `TenantContext.setCurrentTenant(tenantId)`. Hibernate automáticamente añade `WHERE tenant_id = ?` a todas las consultas JPA de esa petición.
4. **Inyección en Spring Security:** Se extrae el `role` y la lista de `permissions`, construyendo las autoridades del usuario en el `SecurityContextHolder`:
   * `ROLE_TEACHER`
   * `student:read`
   * `grade:write`
5. **Limpieza en Bloque `finally`:** Al terminar el ciclo HTTP (incluso si ocurre una excepción), se ejecuta `TenantContext.clear()` para evitar fugas de contexto en el pool de hilos de Tomcat.

---

## 4. Auto-Aprovisionamiento de Usuarios (`UserProvisioningService`)

Para respetar el principio DRY (Don't Repeat Yourself), la creación de perfiles físicos (`Manager`, `Teacher`, `Student`) invoca al servicio centralizado `UserProvisioningService.provisionUserForCurrentTenant(...)`.

#### Algoritmo de Aprovisionamiento
1. Obtiene el `currentTenantId` activo desde el `TenantContext`.
2. Verifica si el `User` global ya existe por `email`:
   * Si **no** existe: crea el `User` global con una contraseña inicial encriptada y `firstLogin = true`.
   * Si **sí** existe: reutiliza la cuenta global existente.
3. Verifica si ya existe una membresía en `UserTenantAccess` para la combinación (`user_id`, `tenant_id`):
   * Si **no** existe: crea la fila en `UserTenantAccess` asociando el `tenant_id`, el `role_id` y el `institutionalPersonId`.

---

## 5. Configuración de Arranque y Orden de Seeders

### Solución a Condición de Carrera (Hibernate vs. Spring Security)
En `application.properties` se definió la ruta explícita del resolver para evitar que Hibernate despierte sin contexto durante el arranque de Spring Security:

```properties
spring.jpa.properties.hibernate.tenant_identifier_resolver=com.sistema.notas.core.tenant.TenantIdentifierResolver
```

### Orden Estricto en `DatabaseSeeder.java`
Los datos semilla deben crearse en una secuencia lógica donde las dependencias previas estén garantizadas:

1. **`TenantSeeder`:** Crea la institución educativa de prueba (Tenant ID 1).
2. **`PermissionAndRoleSeeder`:** Crea la matriz de permisos base (`student:read`, `grade:write`, etc.) y asigna los permisos a los roles (`ADMIN`, `TEACHER`, `STUDENT`, `MANAGER`).
3. **`AdminBootstrapSeeder`:** Crea el usuario administrador global (`admin@notas.local`) y registra su membresía como `ADMIN` en el Tenant ID 1 a través de `UserTenantAccess`.