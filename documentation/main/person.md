# Documentación Técnica: Personas Institucionales (Teacher/Manager/Student) y su relación con Usuarios

**Proyecto:** Notas System (SaaS Educativo Multi-Tenant)
**Fecha de Actualización:** 28 de Julio de 2026
**Versión:** 1.0.0

---

## 1. Resumen Ejecutivo

Este documento describe cómo se relacionan las **personas institucionales** (`Teacher`, `Manager`, `Student`) con
la **identidad de acceso** (`User` / `UserTenantAccess`), en qué momento se crea esa relación, y los ajustes de
seguridad y ciclo de vida que se hicieron sobre ese flujo.

### Idea central
Una persona institucional (maestro, encargado, estudiante) es un dato **propio de un colegio** (tenant). El login
(el `User`) es una identidad **global**, compartida entre colegios cuando el mismo correo aparece en más de una
institución. Ambos conceptos están desacoplados intencionalmente.

---

## 2. Modelo de datos

```
InstitutionalPerson (tabla "people", @TenantId -> tenant-scoped)
    ├── Teacher   (tabla "teachers")
    ├── Manager   (tabla "managers")
    └── Student   (tabla "students")

User (GlobalAuditableEntity, NO tenant-scoped, email unico a nivel de BD)
    └── UserTenantAccess (GlobalAuditableEntity, NO tenant-scoped)
            - user_id
            - tenant_id
            - role_id
            - institutionalPersonId   -> apunta al id de Teacher/Manager/Student
```

* **`InstitutionalPerson`** (`entity/InstitutionalPerson.java`) extiende `AuditableEntity`
  (`entity/AuditableEntity.java`), que tiene `@org.hibernate.annotations.TenantId` sobre el campo `tenantId`. Esto
  hace que **Hibernate filtre automáticamente** toda consulta sobre `people`/`teachers`/`managers`/`students` por
  el tenant activo (`TenantContext`), y que cada fila nueva se etiquete con el tenant en el que fue creada.
* **`InstitutionalPerson.email`** (`@Column(name = "email", length = 50)`) **no tiene ninguna restricción única a
  nivel de base de datos**. La única validación de duplicados es en la capa de aplicación
  (`PersonRepository.existsByEmail(...)` / `existsByEmailAndIdNot(...)`), y como la entidad es tenant-scoped, ese
  chequeo **también queda filtrado por tenant automáticamente** — solo bloquea duplicados dentro del mismo colegio.
* **`User`** (`entity/security/User.java`) es la identidad global de login: `email` (único a nivel de BD),
  `passwordHash`, `firstLogin`. No tiene `tenantId` ni `role`.
* **`UserTenantAccess`** (`entity/security/UserTenantAccess.java`) es la tabla pivote de 3 vías
  (`user_id` + `tenant_id` + `role_id`) que le da a un `User` acceso a un `Tenant` con un `Role` determinado, y
  guarda en `institutionalPersonId` el id de la fila física (Teacher/Manager/Student) que representa a esa persona
  **en ese tenant**.

### Consecuencia directa: una persona puede trabajar en varios colegios
Como `InstitutionalPerson` es tenant-scoped y su email no tiene restricción global, **el mismo correo puede
registrarse como Teacher en el Colegio A y, por separado, como Teacher en el Colegio B**: son dos filas físicas
distintas (`id` y `tenant_id` diferentes), completamente independientes — editar una no afecta a la otra. El
`User` global, en cambio, se reutiliza: la persona inicia sesión con **un solo usuario/contraseña**, y ese usuario
acumula una fila `UserTenantAccess` por cada colegio al que pertenece.

---

## 3. ¿Cuándo se crea la relación Usuario ↔ Tenant?

La relación se crea **en el momento de creación de la persona institucional**, nunca antes ni por separado. Los
tres services (`TeacherServiceImpl.save`, `ManagerServiceImpl.save`, `StudentServiceImpl.save`) llaman, justo
después de persistir la entidad, a:

```java
userProvisioningService.provisionUserForCurrentTenant(saved.getEmail(), "TEACHER", saved.getId());
```

(mismo patrón en `ManagerServiceImpl` con `"MANAGER"` y en `StudentServiceImpl` con `"STUDENT"`)

### Algoritmo de `UserProvisioningService.provisionUserForCurrentTenant` (`service/security/UserProvisioningService.java`)
1. Toma el `currentTenantId` desde `TenantContext` (lanza `IllegalStateException` si no hay tenant en contexto).
2. Resuelve el `Role` por nombre (`ADMIN`/`TEACHER`/`STUDENT`/`MANAGER`) — debe existir previamente (ver
   `PermissionAndRoleSeeder`).
3. Busca el `User` global por email:
   - Si no existe → lo crea con contraseña inicial `123456` (encriptada) y `firstLogin = true`.
   - Si ya existe (la persona ya tenía cuenta en otro colegio) → lo reutiliza tal cual.
4. Busca si ya existe `UserTenantAccess` para `(user_id, tenant_id actual)`:
   - Si no existe → crea la membresía, guardando `institutionalPersonId` = el id de la fila Teacher/Manager/Student
     recién creada en **este** tenant.

No existe (ni se necesita) un flujo de "invitar/pre-cargar usuario existente a otro tenant" separado: basta con
crear la persona (Teacher/Manager/Student) en el nuevo colegio con el mismo email — el provisioning detecta que el
`User` global ya existe y solo agrega la membresía nueva.

---

## 4. Restricciones sobre quién puede crear cada tipo de persona

Se agregó `@PreAuthorize` en los controllers de creación (antes cualquier usuario autenticado, sin importar su
rol, podía crear cualquier tipo de persona):

| Endpoint | Controller | Restricción |
|---|---|---|
| `POST /api/core/teacher` | `TeacherController.createTeacher` | `hasRole('ADMIN')` |
| `POST /api/core/managers` | `ManagerController.createManager` | `hasRole('ADMIN')` |
| `POST /api/core/students` | `StudentController.createStudent` | `hasRole('ADMIN') or hasRole('TEACHER')` |

Para que `hasRole(...)` funcionara, fue necesario que `JwtAuthenticationFilter` agregara la autoridad
`ROLE_<rol>` (tomada del claim `role` del JWT) además de las autoridades de `permissions` — antes solo se
otorgaban los `permissions` crudos, así que **ningún** `hasRole(...)` del sistema funcionaba (incluyendo
`AuthController.register`, que ya usaba `@PreAuthorize("hasRole('ADMIN')")` sin que nunca se le otorgara esa
autoridad).

---

## 5. De dónde sale el tenant activo al crear una persona

Antes, `TenantFilter` exigía el header `X-Tenant-ID` en cada request y confiaba ciegamente en el valor que mandara
el cliente — nada garantizaba que coincidiera con el tenant real del usuario autenticado (JWT). Esto significaba
que, en teoría, un usuario logueado en el Colegio A podía mandar `X-Tenant-ID: 2` y terminar creando una persona
(y su `UserTenantAccess`) en el Colegio B.

**Ahora** (`config/core/TenantFilter.java`) el tenant activo sale **exclusivamente** del JWT autenticado: el
filtro lee el `tenantId` desde el `CustomUserDetails` que ya dejó `JwtAuthenticationFilter` en el
`SecurityContextHolder`, y con eso llama a `TenantContext.setCurrentTenant(tenantId)`. El header `X-Tenant-ID` ya
no se lee ni se exige — el frontend no necesita mandarlo.

Esto es seguro porque `TenantFilter` (un `@Component` con precedencia por defecto) siempre se ejecuta **después**
de la cadena de Spring Security (que corre con prioridad alta vía `addFilterBefore` en `SecurityConfig`, y ya
exige `anyRequest().authenticated()`). Para cuando `TenantFilter` corre, o la petición ya fue cortada con `401`,
o el `SecurityContextHolder` ya tiene el `CustomUserDetails` autenticado con su `tenantId` real.

---

## 6. Rol MANAGER

`PermissionAndRoleSeeder` no creaba el rol `MANAGER`, pero `ManagerServiceImpl.save()` ya lo pedía por nombre —
crear un Manager producía `IllegalStateException: El Rol 'MANAGER' no existe en el sistema`. Se agregó:

- Permiso `manager:read` ("Ver Estudiantes a Cargo", módulo `ENCARGADOS`).
- Rol `MANAGER` con permisos `manager:read` + `grade:read` (solo lectura — un encargado consulta el progreso de
  sus estudiantes, no lo modifica).

**Pendiente / fuera de este alcance:** estos permisos son a nivel de módulo, no de fila. Hoy un `MANAGER` con
`grade:read` no está limitado a ver solo las notas de *sus* estudiantes a cargo (vía `ManagerStudents`) — eso
requeriría un filtro de datos aparte que todavía no existe.

---

## 7. Ciclo de vida: eliminación de una persona

Al eliminar (soft-delete) un Teacher/Manager/Student, ahora también se desactiva su `UserTenantAccess` en ese
tenant (`UserProvisioningService.deactivateAccessForCurrentTenant`), llamado desde `delete()` en los tres
services. Esto asegura que la persona ya no pueda iniciar sesión **en ese colegio** una vez eliminada, sin tocar
el `User` global ni sus membresías en otros colegios (si las tiene).

Antes de este cambio, eliminar una persona dejaba su `UserTenantAccess` activo indefinidamente — podía seguir
autenticándose e incluso recibir un token con `tenantId`/`role` de un colegio del que ya no formaba parte
institucionalmente.

---

## 8. Decisión de diseño: el email de la Persona NO sincroniza el email de login

Se evaluó (y se descartó) sincronizar automáticamente `User.email` cada vez que se edita el email de un
Teacher/Manager/Student. La razón: si la misma persona física tiene registros en 2+ colegios, editar su email en
el Colegio A habría cambiado su email de **login** en todos los colegios a la vez, mientras el registro en el
Colegio B seguía mostrando el email viejo — quedarían desincronizados entre sí sin que nadie lo note.

**Decisión final:** el campo `email` de `Teacher`/`Manager`/`Student` es un dato de **contacto por institución**,
totalmente independiente del email de acceso (`User.email`). Editar el email de una persona en un colegio nunca
cambia su email de login. Cambiar el email de acceso (login) queda para un flujo dedicado y explícito
(ej. "cambiar mi correo de cuenta"), no implementado todavía — es tarea aparte.

---

## 9. Resumen de archivos relevantes

| Archivo | Rol en este flujo |
|---|---|
| `entity/InstitutionalPerson.java`, `entity/AuditableEntity.java` | Tenant-scoping (`@TenantId`) de las personas |
| `entity/security/User.java`, `UserTenantAccess.java` | Identidad global y membresía por tenant |
| `service/security/UserProvisioningService.java` | Crea/reutiliza `User`, crea `UserTenantAccess`, desactiva en delete |
| `service/core/impl/TeacherServiceImpl.java`, `ManagerServiceImpl.java`, `StudentServiceImpl.java` | Disparan el provisioning al crear; desactivan el acceso al eliminar |
| `controller/core/TeacherController.java`, `ManagerController.java`, `StudentController.java` | `@PreAuthorize` por rol en la creación |
| `config/core/TenantFilter.java` | Deriva el tenant activo del JWT autenticado (ya no de un header) |
| `config/security/JwtAuthenticationFilter.java`, `CustomUserDetails.java` | Otorgan `ROLE_<rol>` como autoridad de Spring Security |
| `config/seeders/PermissionAndRoleSeeder.java` | Rol `MANAGER` + permiso `manager:read` |
| `respository/security/UserTenantAccessRepository.java` | `findByInstitutionalPersonIdAndTenantIdAndRoleName` |
