# Architecture Decision Record (ADR): Seguridad RBAC Multi-Tenant

## 1. El Problema del RBAC Tradicional en entornos SaaS
En una aplicación monolítica tradicional, un Usuario tiene una relación directa con un Rol (Ej. `Juan -> ADMIN`). 
En un entorno Multi-Tenant, implementar esta relación directa genera una vulnerabilidad crítica: Si a Juan se le asigna el rol `ADMIN` en el "Colegio A", heredaría automáticamente privilegios de administrador en el "Colegio B" y en todas las demás instituciones donde participe.

## 2. La Solución Arquitectónica: Bóveda de 3 Vías
Se ha decidido separar estrictamente la **Autenticación (Identidad Global)** de la **Autorización (Acceso Local)** mediante una intersección de tres vías.


### Entidades Globales (Sin `@TenantId`)
* **`User`**: Representa la identidad global de la persona. Solo almacena credenciales de acceso (`email`, `password_hash`). Un usuario es único en toda la plataforma.
* **`Tenant`**: Representa a la institución o colegio.
* **`Role`**: Catálogo global de niveles de permiso (`ADMIN`, `TEACHER`, `STUDENT`).

### Entidades Locales (Con `@TenantId`)
* **`InstitutionalPerson` (y herederos)**: Representa el perfil físico y los datos académicos o laborales del usuario dentro de un colegio específico.

### La Tabla Pivote (`user_tenant_access`)
Es el núcleo de la autorización. Define qué poderes tiene un usuario global dentro de un ecosistema aislado.
**Columnas clave:**
* `user_id` (FK) -> ¿Quién es?
* `tenant_id` (FK) -> ¿Dónde está entrando?
* `role` (Enum/FK) -> ¿Qué puede hacer aquí?
* `institutional_person_id` (FK) -> ¿Cuál es su perfil en este colegio?

## 3. Flujo de Autenticación (Login)
1. El usuario envía `email` y `password`.
2. El sistema valida las credenciales contra la tabla global `User`.
3. El sistema busca en `user_tenant_access` todos los colegios a los que este usuario tiene acceso.
4. **Respuesta Frontend:** Se devuelve al usuario un listado de sus colegios para que elija a cuál entrar (Similar al selector de espacios de Slack).
5. **Generación JWT:** Una vez elegido el colegio, se emite un Token JWT que incluye en su payload el `tenant_id` y el `role` específico de esa sesión.

## 4. Flujo de Autorización (Peticiones HTTP)
1. El cliente envía el JWT en el header `Authorization: Bearer <token>`.
2. El `JwtAuthenticationFilter` extrae el `tenant_id` del token y lo inyecta en el `TenantContext`.
3. Hibernate utiliza el `TenantContext` para aislar los datos a nivel de base de datos de forma invisible para el desarrollador.