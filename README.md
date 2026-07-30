# Notas System (Backend)

API REST en Spring Boot para un sistema de gestión de notas escolar **multi-tenant** (varios colegios sobre la
misma instalación, aislados por `tenant_id` vía Hibernate `@TenantId`).

---

## 1. Requisitos previos

| Herramienta | Versión | Notas |
|---|---|---|
| **JDK** | **21** | Obligatorio. El proyecto no compila con Java 8/11/17. Verifica con `java -version`. Si tu `JAVA_HOME`/PATH apunta a otra versión, instala JDK 21 (ej. Temurin/Oracle) y ajusta `JAVA_HOME` antes de compilar. |
| **Maven** | 3.9.x | No es obligatorio instalarlo aparte: el repo incluye el wrapper (`mvnw` / `mvnw.cmd`), que descarga Maven 3.9.14 automáticamente la primera vez. |
| **MySQL** | 8.x | Puede ser el MySQL/MariaDB que trae **XAMPP**, o una instalación aparte. Debe estar corriendo en `localhost:3306` (o ajustar la URL, ver sección 3). |
| **Git** | cualquiera | Para clonar el repositorio. |

No se necesita Node/npm — este repositorio es solo el backend (no hay frontend aquí).

### IDE (opcional pero recomendado)
El proyecto usa **Lombok** intensivamente (`@Getter`, `@Setter`, `@RequiredArgsConstructor`, etc.). Si tu editor
marca errores de compilación que Maven no marca, instala el plugin de Lombok correspondiente:
- **VS Code**: extensión "Lombok Annotations Support for VS Code".
- **IntelliJ IDEA**: plugin "Lombok" (viene incluido en versiones recientes) + habilitar *Annotation Processing*.

---

## 2. Clonar el repositorio

```bash
git clone https://github.com/SKsergio/notaSystemJava.git
cd notaSystemJava
```

---

## 3. Base de datos

Hibernate (`spring.jpa.hibernate.ddl-auto=create`) crea las **tablas** automáticamente al arrancar, pero **no crea
la base de datos en sí**. Hay que crearla una sola vez, manualmente:

```sql
CREATE DATABASE NOTA_SYSTEM;
```

(desde phpMyAdmin de XAMPP, MySQL Workbench, o `mysql -u root -p -e "CREATE DATABASE NOTA_SYSTEM;"`)

> ⚠️ **Importante**: con `ddl-auto=create`, **cada reinicio del backend borra y recrea todo el schema desde cero**
> (pensado para desarrollo). No uses esta configuración en un ambiente donde los datos deban persistir entre
> reinicios sin cambiarla antes a `update`/`validate`.

---

## 4. Configuración (variables de entorno)

Todo tiene un valor por defecto en `src/main/resources/application.properties`, pensado para correr local sin
configurar nada. Para producción o para cambiar credenciales, define estas variables de entorno:

| Variable | Default (dev) | Descripción |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/NOTA_SYSTEM?serverTimezone=UTC` | URL JDBC de la base de datos |
| `DB_USERNAME` | `root` | Usuario de MySQL |
| `DB_PASSWORD` | `pass123` | Contraseña de MySQL |
| `JWT_SECRET` | *(valor de ejemplo en el properties)* | Secreto HMAC-SHA256 para firmar los JWT. **Debe tener al menos 32 caracteres** — la app falla al arrancar si es más corto. Cambialo en cualquier ambiente real. |
| `JWT_EXPIRATION_MS` | `86400000` (24h) | Tiempo de vida del token en milisegundos |
| `JWT_ISSUER` | `notas-system` | Claim `iss` del JWT |
| `ADMIN_EMAIL` | `admin@notas.local` | Correo del usuario administrador/superadmin inicial (creado automáticamente al primer arranque) |
| `ADMIN_PASSWORD` | `Admin123!` | Contraseña del administrador inicial |

Puedes exportarlas en tu shell antes de correr el proyecto, o crear un archivo de entorno según cómo lo despliegues
(el proyecto no incluye `.env` ni perfiles Spring adicionales por ahora).

---

## 5. Levantar el proyecto

Con el wrapper incluido (recomendado, no requiere Maven instalado):

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / Mac / Git Bash
./mvnw spring-boot:run
```

O compilando el jar y ejecutándolo directo:

```bash
./mvnw clean package
java -jar target/notas-0.0.1-SNAPSHOT.jar
```

Al arrancar por primera vez, los *seeders* (`config/seeders/`) crean automáticamente:
- Un tenant de prueba.
- Los roles base (`ADMIN`, `TEACHER`, `STUDENT`, `MANAGER`) y sus permisos.
- El administrador inicial (`ADMIN_EMAIL` / `ADMIN_PASSWORD`), ya marcado como **superadmin** y con membresía en
  el tenant de prueba.

---

## 6. Verificar que quedó arriba

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Login de prueba**: `POST /api/auth/login` con `{ "email": "admin@notas.local", "password": "Admin123!" }`
  (o los valores que hayas puesto en `ADMIN_EMAIL`/`ADMIN_PASSWORD`).

---

## 7. Stack principal

- **Spring Boot 4.0.5** (Java 21) — Web MVC, Data JPA, Security, Validation.
- **Hibernate multi-tenant** (`@TenantId`, discriminador por columna `tenant_id`, resuelto por `TenantContext`/JWT).
- **JWT** (`io.jsonwebtoken` / jjwt 0.12.6) para autenticación stateless.
- **MapStruct** para mapeo entidad ↔ DTO.
- **springdoc-openapi** para Swagger UI.
- **MySQL Connector/J** como driver de base de datos.
- **Lombok** para reducir boilerplate.

---

## 8. Documentación adicional

Ver [`documentation/main/`](documentation/main/) para el detalle de la arquitectura de seguridad/multi-tenancy
(`doc_authv1.md`) y del flujo de personas institucionales y su relación con usuarios (`person.md`).
