# DeCulto

DeCulto es una colección privada de películas. Esta versión es el **esqueleto técnico**: arranca, se conecta a PostgreSQL, prepara su base de datos sola y muestra una página de inicio y las páginas de error propias. Todavía no tiene funcionalidades de producto.

Este README permite poner el entorno en marcha en otra máquina sin información externa.

## 1. Requisitos locales

- **JDK 21 o superior.** El proyecto declara Java 21 en el `pom.xml` y se ha probado con JDK 25. No funciona con Java 8: el build falla. Comprueba la versión con `java -version`. Si tu `java` por defecto es Java 8, define `JAVA_HOME` apuntando a un JDK compatible antes de ejecutar `mvnw.cmd`.
- **PostgreSQL 17** instalado y en marcha en local (`localhost`, puerto `5432`). Es la versión probada. PostgreSQL 19 no es compatible con la versión de Flyway del proyecto (11.7.2).
- **Maven no hace falta instalarlo.** `mvnw.cmd` usa el wrapper incluido en el repositorio, que descarga Maven la primera vez.

## 2. Crear las bases de datos y sus usuarios

Este paso crea las bases y los usuarios de PostgreSQL. **No** crea tablas de la aplicación: esas las prepara la propia aplicación al arrancar.

Conéctate como superusuario (por ejemplo `postgres`):

```bat
psql -h localhost -p 5432 -U postgres
```

Y ejecuta en `psql`:

```sql
CREATE ROLE deculto_dev LOGIN PASSWORD 'cambia-esta-contrasena-de-desarrollo';
CREATE DATABASE deculto_dev OWNER deculto_dev;

CREATE ROLE deculto_test LOGIN PASSWORD 'deculto_test';
CREATE DATABASE deculto_test OWNER deculto_test;
```

- `deculto_dev` es la base de desarrollo. Elige tú su contraseña y úsala después en `.env`.
- `deculto_test` es la base de pruebas. Su contraseña debe ser `deculto_test`, porque así la fijan las pruebas. Solo es válida para esta base local de pruebas.

Sal de `psql` con `\q`.

## 3. Configurar las variables de entorno

Spring Boot no lee el fichero `.env` por sí solo. Sigue estos pasos.

### 3.1. Crear tu fichero `.env`

En la raíz del proyecto, copia la plantilla:

```powershell
Copy-Item .env.example .env
```

Abre `.env` y rellena las variables obligatorias. El fichero `.env` está excluido del control de versiones (`.gitignore`): **nunca lo subas al repositorio**.

### 3.2. Cargar `.env` en la terminal antes de arrancar

Ejecuta esto en PowerShell, desde la raíz del proyecto, en la misma terminal en la que vayas a arrancar la aplicación:

```powershell
Get-Content .env | Where-Object { $_ -match '^\s*[^#\s][^=]*=' } | ForEach-Object {
    $clave, $valor = $_ -split '=', 2
    [Environment]::SetEnvironmentVariable($clave.Trim(), $valor.Trim(), 'Process')
}
```

Las variables solo viven en esa terminal. Si abres una terminal nueva, vuelve a cargar `.env`.

## 4. Variables de entorno

| Variable | Obligatoria | Descripción | Ejemplo (no real) |
|---|---|---|---|
| `SPRING_DATASOURCE_URL` | Sí | URL JDBC de la base de datos de desarrollo | `jdbc:postgresql://localhost:5432/deculto_dev` |
| `SPRING_DATASOURCE_USERNAME` | Sí | Usuario de la base de datos | `deculto_dev` |
| `SPRING_DATASOURCE_PASSWORD` | Sí | Contraseña de ese usuario | `cambia-esta-contrasena` |
| `SPRING_PROFILES_ACTIVE` | Sí | Perfil activo. Debe ser `dev`, el único perfil que existe ahora | `dev` |
| `TMDB_API_KEY` | No | Clave de la API de TMDB. Puede quedar vacía: la aplicación arranca igual. La usará la búsqueda de películas en una fase posterior | *(vacía)* |
| `SERVER_PORT` | No | Puerto HTTP de la aplicación. Por defecto `8080`; cámbialo solo si ese puerto está ocupado | `8081` |

La plantilla `.env.example` contiene todas estas variables. Ningún valor real debe estar versionado.

## 5. Arrancar la aplicación

Con PostgreSQL en marcha, las bases creadas y `.env` cargado en la terminal:

```bat
mvnw.cmd spring-boot:run
```

Cuando el arranque termine, abre **http://localhost:8080** en el navegador.

Si el puerto 8080 está ocupado, define `SERVER_PORT` en la misma terminal antes de arrancar y abre en el navegador el puerto que elijas (por ejemplo, `http://localhost:8081`):

```powershell
$env:SERVER_PORT = '8081'
mvnw.cmd spring-boot:run
```

- No hace falta preparar el esquema a mano. Al arrancar contra una base vacía, Flyway aplica sola la migración inicial. Esa migración no crea tablas de la aplicación; solo queda registrada en el historial de migraciones.
- En arranques posteriores sobre la misma base, la migración no se vuelve a aplicar.
- Si la base de datos no está disponible o las credenciales son incorrectas, el arranque falla con un mensaje en la terminal. Revisa que PostgreSQL esté en marcha y que las variables `SPRING_DATASOURCE_*` sean correctas.
- Para detener la aplicación, pulsa `Ctrl+C` en la terminal.

## 6. Ejecutar las comprobaciones

```bat
mvnw.cmd verify
```

Requisitos:

- PostgreSQL 17 en marcha con la base `deculto_test` creada y su usuario, como en el apartado 2.

Las pruebas fijan en código la URL, el usuario y la contraseña de la base de pruebas (`deculto_test`). Por eso no necesitas cargar `.env` ni activar ningún perfil antes de ejecutarlas. Basta con:

```bat
mvnw.cmd verify
```

## 7. Atribución de TMDB

Los datos de películas proceden de [TMDB](https://www.themoviedb.org/). La atribución aparece en el pie de todas las páginas.

## 8. Notas

- El esqueleto solo tiene la página de inicio (`/`) y las páginas de error propias (404 y 500). Cualquier otra dirección responde con la página de «página no existe».
- El proyecto se ha probado con **JDK 25**. El `pom.xml` declara Java 21.
- La aplicación usa la dependencia `org.flywaydb:flyway-database-postgresql`, necesaria para que Flyway funcione con PostgreSQL desde Flyway 10. Su versión la gestiona Spring Boot.
- No hay Docker ni despliegue en esta versión.
