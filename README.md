# User Microservice

Microservicio de usuarios de Music Store desarrollado con Spring Boot, Oracle Cloud y Flyway. Administra las cuentas, los roles (`ADMIN` y `USER`) y la autenticación HTTP Basic que también utiliza el microservicio `album`.

## Requisitos

- Java 21
- Oracle Cloud o Oracle local
- Wallet de Oracle Cloud
- Conexión a Internet
- PowerShell o una terminal compatible

## Configuración de Oracle

En una nueva PowerShell, define las variables de conexión:

```powershell
cd "C:\ruta\al\proyecto\user"

$env:DB_URL="jdbc:oracle:thin:@NOMBRE_DEL_SERVICIO"
$env:DB_USER="USER_SERVICE"
$env:DB_PASSWORD="CONTRASEÑA_DEL_USUARIO"
$env:TNS_ADMIN="C:\ruta\a\la\wallet"
```

Reemplaza `NOMBRE_DEL_SERVICIO` por el alias definido en `tnsnames.ora`, y configura el usuario, contraseña y ruta de Wallet correspondientes.

La carpeta Wallet debe contener archivos como:

```text
tnsnames.ora
sqlnet.ora
cwallet.sso
```

## Creación de la base de datos

Flyway crea automáticamente la tabla de usuarios mediante:

```text
src/main/resources/db/migration/V1__create_users_table.sql
```

Hibernate utiliza:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Esto significa que Hibernate valida la estructura de la base de datos, pero no crea ni elimina tablas.

El usuario de Oracle debe tener permisos para crear tablas, secuencias y triggers en su schema.

## Cuentas iniciales

Antes del primer inicio, define las contraseñas de las cuentas iniciales (mínimo 8 caracteres):

```powershell
$env:MUSICSTORE_SEED_ADMIN_PASSWORD="CONTRASEÑA_ADMIN"
$env:MUSICSTORE_SEED_USER_ONE_PASSWORD="CONTRASEÑA_USER1"
$env:MUSICSTORE_SEED_TWO_PASSWORD="CONTRASEÑA_USER2"
```

La aplicación crea estas cuentas solo cuando la tabla está vacía. En los inicios siguientes las variables se ignoran y las contraseñas existentes no cambian.

| Usuario | Correo | Rol |
|---|---|---|
| `admin` | `admin@musicstore.test` | `ADMIN` |
| `user1` | `user1@musicstore.test` | `USER` |
| `user2` | `user2@musicstore.test` | `USER` |

Las contraseñas se almacenan cifradas con BCrypt.

## Endpoints

| Método | Ruta | Acceso | Respuesta |
|---|---|---|---|
| GET | `/health` | Público | 200, estado del servicio |
| POST | `/api/auth/register` | Público | 201, cuenta creada siempre con rol `USER` |
| POST | `/api/auth/login` | Autenticado | 200, `{ id, username, email, role }` |
| GET | `/api/auth/profile` | Autenticado | 200, `{ user, role }`; lo usa el microservicio `album` |
| PUT | `/api/auth/profile` | Autenticado | 200, actualiza el correo y la contraseña del propio usuario |
| GET | `/api/users` | `ADMIN` | 200, lista de usuarios |
| GET | `/api/users/{id}` | `ADMIN` | 200, o 404 si no existe |
| POST | `/api/users` | `ADMIN` | 201, usuario creado con el rol indicado |
| PUT | `/api/users/{id}` | `ADMIN` | 200, usuario actualizado |
| DELETE | `/api/users/{id}` | `ADMIN` | 204 |

La autenticación es HTTP Basic. Un usuario `USER` solo puede modificar su propio perfil y nunca su rol.

Cuerpo para `POST /api/auth/register`:

```json
{
  "username": "nuevo_usuario",
  "email": "nuevo@dominio.cl",
  "password": "Clave123!"
}
```

Cuerpo para `PUT /api/auth/profile` (la contraseña es opcional):

```json
{
  "email": "nuevo@dominio.cl",
  "password": "Clave123!"
}
```

### Validaciones

| Campo | Regla |
|---|---|
| `username` | Obligatorio, 3 a 50 caracteres, solo letras, números y `_` |
| `email` | Obligatorio, máximo 100 caracteres, con dominio y extensión (`nombre@dominio.cl`) |
| `password` | 8 a 100 caracteres |

### Errores

| Código | Caso |
|---|---|
| 400 | Datos inválidos; el campo `fields` indica el error de cada atributo |
| 401 | Sin credenciales o credenciales incorrectas |
| 403 | El rol no tiene permiso |
| 404 | Usuario inexistente |
| 409 | Usuario o correo ya registrado, o intento de quitar el último `ADMIN` |

## Ejecución

Desde la carpeta del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

El servicio se ejecuta en:

```text
http://localhost:8081
```

## Docker

El `Dockerfile` compila el proyecto con Maven y Java 21, y lo ejecuta en `eclipse-temurin:21-jre-alpine` con un usuario sin privilegios. El wallet no se incluye en la imagen: se monta como volumen.

```powershell
docker build -t musicstore-user:1.0 .
```

La ejecución junto al frontend y al microservicio `album` se realiza con Docker Compose desde el repositorio `musicstore-deploy`.
