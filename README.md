# User Microservice

Microservicio de usuarios desarrollado con Spring Boot, Oracle Cloud y Flyway.

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

Antes de iniciar la aplicación, define las contraseñas:

```powershell
$env:MUSICSTORE_SEED_ADMIN_PASSWORD="Admin1234"
$env:MUSICSTORE_SEED_USER_ONE_PASSWORD="User1234"
$env:MUSICSTORE_SEED_USER_TWO_PASSWORD="User5678"
```

La aplicación crea estas cuentas cuando la tabla está vacía:

| Usuario | Rol |
|---|---|
| `admin` | `ADMIN` |
| `user1` | `USER` |
| `user2` | `USER` |

Las contraseñas se almacenan cifradas.

## Ejecución

Desde la carpeta del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

El servicio se ejecuta en:

```text
http://localhost:8081
```
