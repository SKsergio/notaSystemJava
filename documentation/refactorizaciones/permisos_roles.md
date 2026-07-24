# Architecture Decision Record (ADR) 002: Refactorización Auth, Multi-Tenant y RBAC Granular

**Fecha:** 23 de Julio de 2026  
**Estado:** Aceptado e Implementado  
**Área:** Seguridad, Base de Datos, Spring Security, API REST  

---

## 1. Contexto Arquitectónico
El sistema requería evolucionar de una arquitectura monolítica tradicional a un SaaS (Software as a Service) Multi-Tenant. Se identificó que el diseño original acoplaba la **Identidad Global (Autenticación)** con el **Acceso Local (Autorización)**, y utilizaba roles estáticos no configurables.

Se decide implementar un modelo de autorización fina (Fine-Grained Authorization) basado en **Permisos por Acción** (`módulo:acción`), donde los Roles actúan como agrupadores configurables y el alcance de los permisos queda estrictamente delimitado al Tenant seleccionado en la sesión.

---

## 2. Decisión Arquitectónica: Bóveda de 3 Vías y Permisos Granulares

### Entidades Globales (Sin `@TenantId`)
* **`User`**: Identidad global única por persona (`email`, `password_hash`).
* **`Tenant`**: Institución educativa o colegio.
* **`Permission`**: Acción atómica del sistema (Ej. `student:read`, `student:write`, `grade:write`).
* **`Role`**: Agrupador global de permisos (contiene un `Set<Permission>`).

### Entidad Pivote y Evaluador de Contexto
* **`UserTenantAccess`**: Relaciona `user_id` + `tenant_id` + `role_id` + `institutional_person_id`.

---

## 3. Aislamiento Estricto de Permisos por Tenant

**Regla de Oro:** Un usuario NO hereda permisos de una institución a otra. Los permisos se evalúan única y exclusivamente según el `tenant_id` de la sesión activa.


---
### Flujo de Generación de Token JWT
1. El cliente autentica credenciales (`email`, `password`).
2. El cliente selecciona el `tenantId` al que desea ingresar.
3. `AuthService` busca el registro en `UserTenantAccess` correspondiente a ese `userId` y `tenantId`.
4. Se leen los permisos asociados al `Role` de **esa fila específica**.
5. Se emite un JWT que incluye en sus Claims:
   * `tenantId`: Identificador del colegio activo.
   * `role`: Nombre del rol en ese colegio.
   * `permissions`: Array con los códigos de permisos autorizados (`["student:read", "grade:write"]`).

---

## 4. Componentes y Modificaciones Técnicas

### 1. Modelo de Datos (`Permission` y `Role`)
* La entidad `Permission` almacena el código técnico (`code`), nombre legible (`name`) y módulo (`module`).
* La entidad `Role` posee una relación `@ManyToMany` con `Permission` (`role_permissions`).

### 2. Evaluador de Seguridad Stateless (`JwtAuthenticationFilter`)
* El filtro desencripta el JWT en cada petición HTTP **sin consultar la base de datos**.
* Extrae el `tenantId` e inyecta el contexto en Hibernate via `TenantContext.setCurrentTenant(tenantId)`.
* Extrae la lista de `permissions` y los mapea a objetos `GrantedAuthority` de Spring Security.

### 3. Protección Atómica en Controladores
A partir de este cambio, la protección de endpoints deja de usar roles estáticos (`hasRole`) y utiliza permisos atómicos:

```java
// Ejemplo en controlador de estudiantes:
@PostMapping
@PreAuthorize("hasAuthority('student:write')")
public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) { ... }