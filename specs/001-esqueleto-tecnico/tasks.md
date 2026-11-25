# Tareas — Spec 001 Esqueleto técnico

Estado: aprobado

Máximo 10 tareas, ordenadas por dependencias, de 20–30 minutos preferiblemente. T1 a T10 están completadas (`[x]`).

## T1. Crear el proyecto Maven (pom.xml)
Estado: [x] completada
**Descripción:** Crear `pom.xml` con Java 21, Spring Boot 3.5.x y las dependencias de producción: `spring-boot-starter-web`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-jdbc`, controlador de PostgreSQL y `flyway-core`. Dependencia de test: `spring-boot-starter-test` (JUnit 5 + Spring Boot Test). Sin `spring-boot-starter-security` ni dependencias de test nuevas.
**RF que cubre:** RF-1, RF-4, RF-6, RNF-5
**Hecho cuando:**
- `mvnw.cmd validate` termina correctamente y resuelve todas las dependencias declaradas.
- El pom declara Java 21 y Spring Boot 3.5.x.
- No incluye `spring-boot-starter-security` ni dependencias de test que no existan ya en el proyecto.

## T2. Configurar aplicación, variables de entorno y gitignore
Estado: [x] completada
**Descripción:** Crear `src/main/resources/application.yml` (datasource por variables de entorno, perfil activo, Flyway habilitado, `TMDB_API_KEY` opcional, puerto 8080, Whitelabel desactivado, Thymeleaf sin caché en desarrollo), `.env.example` versionado (todas las variables, `TMDB_API_KEY` vacía, distinguiendo obligatorias y opcional) y `.gitignore` (`.env`, `target/`, IDE, valores reales).
**RF que cubre:** RF-9, RF-11, RF-12, RNF-6, RNF-7
**Hecho cuando:**
- Se puede crear un `.env` a partir de `.env.example` y la aplicación lee URL, usuario, contraseña y perfil del entorno.
- `.env.example` contiene `TMDB_API_KEY` vacía y distingue obligatorias de opcional.
- `.gitignore` excluye `.env`, `target/` y cualquier fichero con valores reales de configuración.

## T3. Crear clase principal de arranque
Estado: [x] completada
**Descripción:** Crear `src/main/java/com/deculto/DecultoApplication.java` con `@SpringBootApplication` como punto de entrada de la aplicación.
**RF que cubre:** RF-1, RF-6
**Hecho cuando:**
- La clase compila y es el punto de entrada del proyecto Maven.
- Al arrancar, Spring Boot levanta el contexto si hay base de datos y falla con mensaje comprensible si no hay conexión.

## T4. Crear migración Flyway inicial vacía
Estado: [x] completada
**Descripción:** Crear `src/main/resources/db/migration/V1__initial_schema.sql` vacía (sin tablas de aplicación) para que Flyway registre el estado inicial al arrancar contra una base de datos vacía.
**RF que cubre:** RF-4, RF-5
**Hecho cuando:**
- Al arrancar contra una base vacía, `flyway_schema_history` registra la versión `1` como aplicada.
- No se crea ninguna tabla de aplicación.
- Un segundo arranque sobre la misma base no reaplica la migración ni produce errores.

## T5. Crear fragmentos de cabecera y pie con atribución TMDB
Estado: [x] completada
**Descripción:** Crear `src/main/resources/templates/fragments/header.html` (nombre de la aplicación, sin enlaces de navegación) y `src/main/resources/templates/fragments/footer.html` (atribución de TMDB visible sin interacción adicional).
**RF que cubre:** RF-3, RNF-4
**Hecho cuando:**
- Ambos fragmentos existen y se pueden insertar en cualquier plantilla Thymeleaf.
- El pie muestra la atribución de TMDB visible sin requerir interacción del usuario.

## T6. Crear página de inicio
Estado: [x] completada
**Descripción:** Crear `HomeController` con `GET /` y la plantilla `src/main/resources/templates/home.html` que muestra el nombre de la aplicación, una frase sobre su propósito (colección privada de películas), cabecera y pie comunes, sin listas de funcionalidades futuras ni enlaces a direcciones inexistentes. Todo en español de España, tratamiento de tú.
**RF que cubre:** RF-1, RF-2, RF-8, RNF-1, RNF-2, RNF-3
**Hecho cuando:**
- `GET /` devuelve estado 200.
- La página contiene el nombre de la aplicación y la frase de propósito.
- La cabecera no tiene enlaces de navegación.
- El pie incluye la atribución de TMDB.
- No hay listas de funcionalidades futuras ni enlaces a direcciones inexistentes.

## T7. Crear página de error propia (404 y 500)
Estado: [x] completada
**Descripción:** Crear `ErrorController` con `GET /error` que lea el código de estado de la petición y devuelva `error/404` o `error/500`. Crear las plantillas `src/main/resources/templates/error/404.html` y `src/main/resources/templates/error/500.html` con cabecera y pie comunes, enlace al inicio y sin detalles técnicos internos.
**RF que cubre:** RF-7, RF-8, RF-10, RNF-1, RNF-4, RNF-8
**Hecho cuando:**
- Una dirección inexistente devuelve 404 con mensaje en español indicando que la página no existe, enlace al inicio, cabecera y pie, sin trazas ni rutas internas.
- Un error interno devuelve 500 con mensaje genérico ("algo ha fallado" o equivalente), enlace al inicio, cabecera y pie, sin trazas ni rutas internas.
- 404 y 500 nunca se aplican a la misma petición.

## T8. Crear estilos CSS (responsive y accesibilidad)
Estado: [x] completada
**Descripción:** Crear `src/main/resources/static/css/styles.css` con variables CSS, enfoque mobile-first, accesibilidad básica (contraste mínimo 4,5:1 para texto normal, foco visible en elementos interactivos, navegación completa por teclado).
**RF que cubre:** RNF-2, RNF-3
**Hecho cuando:**
- Las páginas se leen sin desplazamiento horizontal ni solapamientos a 375 px y a 1440 px de ancho.
- Todos los elementos interactivos muestran foco visible.
- Se puede recorrer la página completa por teclado.

## T9. Crear README de entorno reproducible
Estado: [x] completada
**Descripción:** Crear `README.md` que documente requisitos locales, pasos de arranque, cómo ejecutar las comprobaciones y las variables de entorno necesarias (obligatorias: URL, usuario y contraseña de la base de datos, perfil activo; opcional: `TMDB_API_KEY`), de forma que el entorno se reproduzca en otra máquina sin información externa.
**RF que cubre:** RF-11, RF-12, RNF-9
**Hecho cuando:**
- El README permite arrancar la aplicación desde cero siguiendo solo sus pasos.
- Enumera todas las variables necesarias y distingue obligatorias de opcional.
- No requiere preparar el esquema de la base de datos a mano.

## T10. Crear pruebas automáticas (arranque, migración, páginas)
Estado: [x] completada
**Descripción:** Crear pruebas con JUnit 5 y Spring Boot Test (sin dependencias nuevas) que verifiquen: arranque contra la base de datos de pruebas, migración inicial registrada y no reaplicada, contenido y estado de la página de inicio (200), de la página de error 404 y de la página de error 500, y arranque con `TMDB_API_KEY` ausente o vacía. Ejecutar contra la base de datos de pruebas (`deculto_test`), que se fija en las propias pruebas (la URL, el usuario y la contraseña de `deculto_test` se fijan en las pruebas, no en el entorno), sin modificar la de desarrollo. No se usa perfil `test`: el fichero `application-test.yml` se eliminó por decisión del usuario.
**RF que cubre:** RF-4, RF-5, RF-6, RF-7, RF-8, RF-9, RNF-5, RNF-6
**Hecho cuando:**
- `mvnw.cmd verify` ejecuta las pruebas contra la base de datos de pruebas.
- Las pruebas acreditan que la migración inicial está aplicada y que no hay tablas de aplicación.
- Las pruebas comprueban contenido y estado de inicio (200), error 404 y error 500.
- La base de desarrollo no presenta cambios tras ejecutar las pruebas.
- El conjunto de pruebas no está vacío: cada prueba aserciiona contenido o estado concreto.
