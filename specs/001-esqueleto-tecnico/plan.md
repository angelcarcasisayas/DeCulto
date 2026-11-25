# Plan — Spec 001 Esqueleto técnico

Estado: aprobado

## 1. Archivos afectados y responsabilidad de cada uno

| Archivo | Responsabilidad |
|---|---|
| `pom.xml` | Proyecto Maven: Java 21, Spring Boot 3.5.x, `spring-boot-starter-web`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-jdbc`, controlador de PostgreSQL, `flyway-core`, `flyway-database-postgresql` (módulo de soporte de PostgreSQL de Flyway, sin versión explícita: la gestiona Spring Boot; desde Flyway 10 el soporte de PostgreSQL es un módulo aparte y sin él Flyway rechaza la base de datos con «Unsupported Database»), `spring-boot-starter-test` (JUnit 5, Spring Boot Test). Sin `spring-boot-starter-security` (fuera de alcance, F1) ni dependencias de test nuevas. `flyway-database-postgresql` es la única dependencia de producción añadida respecto a lo previsto inicialmente. |
| `src/main/resources/application.yml` | Datasource (URL, usuario, contraseña desde variables de entorno), perfil activo, Flyway habilitado, `TMDB_API_KEY` opcional, puerto 8080, deshabilitar página de error por defecto de Spring (Whitelabel), Thymeleaf sin caché en desarrollo. El puerto por defecto es 8080; si ese puerto está ocupado, se puede cambiar con la variable de entorno `SERVER_PORT` sin modificar la configuración del proyecto. |
| `src/main/resources/db/migration/V1__initial_schema.sql` | Migración inicial de Flyway: vacía, sin tablas de aplicación. Solo deja constancia en el historial de migraciones. |
| `src/main/java/com/deculto/DecultoApplication.java` | Clase principal `@SpringBootApplication`. Punto de arranque. |
| `src/main/java/com/deculto/common/controller/HomeController.java` | Controlador de la página de inicio: `GET /` devuelve la plantilla `home`. Sin lógica de negocio (no hay dominio aún). |
| `src/main/java/com/deculto/common/controller/ErrorController.java` | Controlador de errores: `GET /error` lee el código de estado del atributo de la petición y devuelve `error/404` o `error/500`. Maneja direcciones inexistentes y errores internos sin exponer trazas. |
| `src/main/resources/templates/home.html` | Página de inicio: nombre de la aplicación, frase de propósito (colección privada de películas), cabecera y pie comunes (vía fragmentos), sin listas de funcionalidades futuras ni enlaces a direcciones inexistentes. |
| `src/main/resources/templates/fragments/header.html` | Fragmento de cabecera común: nombre de la aplicación, sin enlaces de navegación. |
| `src/main/resources/templates/fragments/footer.html` | Fragmento de pie común: atribución de TMDB visible, presente en todas las páginas incluidas las de error. |
| `src/main/resources/templates/error/404.html` | Variante de dirección inexistente: mensaje en español de España ("la página no existe"), enlace al inicio, cabecera y pie comunes, sin detalles técnicos. |
| `src/main/resources/templates/error/500.html` | Variante de error interno: mensaje genérico en español de España ("algo ha fallado"), enlace al inicio, cabecera y pie comunes, sin trazas ni rutas internas. |
| `src/main/resources/static/css/styles.css` | CSS con variables, enfoque mobile-first, accesibilidad (contraste mínimo 4,5:1, foco visible, navegación por teclado, textos alternativos). |
| `src/test/java/com/deculto/...` | Pruebas automáticas: arranque, migración inicial, página de inicio, página de error 404, página de error 500, arranque sin `TMDB_API_KEY`. |
| `README.md` | Requisitos locales, pasos de arranque, variables de entorno, ejecución de comprobaciones, reproducción del entorno en otra máquina. |
| `.env.example` | Fichero de ejemplo versionado: todas las variables documentadas, `TMDB_API_KEY` vacía, distingue obligatorias de opcional. |
| `.gitignore` | Excluye `.env`, `target/`, archivos de IDE y cualquier fichero con valores reales de configuración. |

Nota: `application-test.yml` figuraba en versiones anteriores de este plan con el perfil `test`. Se eliminó por decisión del usuario; no existe perfil `test` en el proyecto y la base de pruebas se fija en las clases de prueba (sección 7).

## 2. Organización de la lógica de dominio y de la interfaz

- **Capas:** controller → service → repository (convención de `AGENTS.md`). En esta spec no hay modelo de dominio, por lo que no hay service ni repository. Los controladores devuelven vistas directamente. Se añadirán service y repository cuando lleguen las funcionalidades F1–F10.
- **Interfaz:** Thymeleaf con renderizado en servidor (SSR). Layout común mediante fragmentos (`header.html`, `footer.html`) insertados en cada plantilla (`home.html`, `error/404.html`, `error/500.html`).
- **Recursos estáticos:** CSS en `static/css/`. No hay JavaScript ni imágenes en esta especificación.
- **Controladores:** uno para el inicio (`HomeController`) y uno para errores (`ErrorController`). Dos controladores son suficientes: uno para la única página funcional y uno para las dos variantes de error.

## 3. Funciones puras

No se incluyen funciones puras en esta especificación. No hay reglas de dominio ni cálculos dependientes del tiempo que las justifiquen. Si una funcionalidad futura las necesita, se añadirán en la capa `service` según la Constitución (punto 3).

## 4. Pseudocódigo y algoritmos

### Flujo de arranque de la aplicación

```
MAIN → SpringApplication.run(DecultoApplication.class)
  → Spring Boot autoconfigura DataSource (variables de entorno)
  → Flyway valida y aplica migraciones pendientes
      → Si V1 no está en flyway_schema_history: la aplica (no crea tablas de aplicación)
      → Si V1 ya está: no la reaplica, conserva objetos existentes
  → Si no hay conexión a la base de datos: falla el arranque, el proceso termina
    y emite mensaje comprensible sin credenciales ni cadena de conexión completa
  → Si todo es correcto: contexto levantado, servidor web escucha en el puerto configurado
```

### Manejo de errores (controlador `/error`)

```
GET /error
  → Leer atributo javax.servlet.error.status_code de la petición
  → Si código == 404: devolver plantilla error/404 con estado 404
  → Si código == 500: devolver plantilla error/500 con estado 500
  → Otro código: devolver plantilla error/404 con estado 404
  → Ambas plantillas usan fragmentos header y footer, mensaje en español,
    enlace al inicio y sin detalles técnicos internos
```

### Comprobación de migración en pruebas (pseudocódigo)

```
Consultar flyway_schema_history WHERE version = '1'
  → Si existe una fila: la migración inicial está aplicada (RF-4, RF-5)
  → Consultar information_schema.tables para tablas de la aplicación:
      → Si no hay tablas de aplicación: correcto (no se creó estructura de dominio)
```

## 5. Cambios de interfaz

- **Página de inicio (`/`):** nombre "DeCulto", frase sobre su propósito (colección privada de películas), cabecera común sin enlaces de navegación, pie con atribución de TMDB. Todo en español de España, tratamiento de tú.
- **Página de error 404:** indicación de que la página no existe, enlace al inicio, cabecera y pie comunes, sin trazas ni rutas internas.
- **Página de error 500:** mensaje genérico ("algo ha fallado"), enlace al inicio, cabecera y pie comunes, sin trazas ni rutas internas, sin afirmar que la página no existe.
- **Cabecera común:** nombre de la aplicación, sin enlaces de navegación mientras no existan funcionalidades.
- **Pie común:** atribución de TMDB visible sin interacción adicional, en todas las páginas incluidas las de error.

## 6. Decisiones técnicas justificadas y alternativas descartadas

| Decisión | Justificación | Alternativa descartada |
|---|---|---|
| Thymeleaf SSR + fragmentos | Requerido por `AGENTS.md` (stack mínimo), permite fragmentos reutilizables y encaja con Spring Boot. | HTML estático: no permitiría fragmentos ni renderizado dinámico sencillo. |
| Spring MVC + controlador `/error` para 404 y 500 | Un solo controlador lee el código de estado y devuelve la variante adecuada; evita duplicar lógica. | `@ControllerAdvice` con `@ExceptionHandler` por separado: más complejo para una spec sin dominio. |
| `spring-boot-starter-jdbc` sin `data-jpa` | No hay entidades ni tablas de aplicación; JDBC es suficiente para el DataSource que necesita Flyway. | `spring-boot-starter-data-jpa`: autoconfigura Hibernate y escanea entidades; sin entidades podría causar fallos de arranque innecesarios. |
| No añadir `spring-boot-starter-security` en esta spec | Seguridad y login están fuera de alcance (F1). Añadir el starter sin configuración segura todos los endpoints automáticamente, rompiendo las páginas públicas. | Añadirlo y desactivarlo: añade dependencia sin beneficio en esta fase; se añadirá con la spec de registro e invitaciones. |
| URL, usuario y contraseña de `deculto_test` fijados con `@SpringBootTest(properties = ...)` | Las propiedades de la anotación tienen prioridad sobre las variables de entorno, así la base de pruebas queda determinada en el código de prueba y no depende de la terminal ni de `SPRING_PROFILES_ACTIVE` (RNF-6, CA-10). Las credenciales de prueba también se fijan en las pruebas: son de una base local de prueba, no son secretos reales (RNF-6), y no se leen del entorno. | Perfil `test` activado con `SPRING_PROFILES_ACTIVE=test` y terminal limpia: el aislamiento dependía del estado de la terminal y no quedaba garantizado (CA-10). Por esta razón y por decisión del usuario, `application-test.yml` se eliminó. Variables de entorno con sufijo `_TEST`: más variables que documentar y mantener. |
| Migración inicial vacía (sin tablas) | La spec define explícitamente que la migración inicial no crea tablas de aplicación, solo registra el estado inicial en el historial de Flyway. | Crear tablas vacías de antelación: violaría RF-4 y el alcance de la spec (modelo de dominio nace con F1). |
| Módulo `flyway-database-postgresql` con la versión gestionada por Spring Boot | Desde Flyway 10 el soporte de PostgreSQL es un módulo aparte; sin él Flyway rechaza la base con «Unsupported Database». Se usa la versión que Spring Boot gestiona para no fijar versiones fuera del stack. Es una dependencia de producción; no se añade ninguna de test. | Actualizar Flyway a una versión propia: obliga a gestionar la versión fuera del BOM de Spring Boot y no resuelve la necesidad del módulo. |
| PostgreSQL 17 para desarrollo y pruebas | PostgreSQL 19 no es compatible con la versión de Flyway del proyecto. | PostgreSQL 19: incompatible con Flyway en la versión actual del proyecto. |
| Puerto 8080 por defecto, cambiable con `SERVER_PORT` | Mantiene una configuración única y predecible; si el puerto está ocupado en la máquina local, la variable de entorno lo resuelve sin tocar el proyecto. | Cambiar el puerto por defecto en `application.yml`: modifica la configuración del proyecto sin necesidad. |

## 7. Estrategia de pruebas

Se utiliza JUnit 5 y Spring Boot Test, ambos incluidos en `spring-boot-starter-test` (no se añaden dependencias de test nuevas, RNF-5). Las pruebas se ejecutan contra la base de datos de pruebas (`deculto_test`), nunca contra la de desarrollo (RNF-6, CA-10). La única dependencia añadida (`flyway-database-postgresql`) es de producción, no de test.

### Clases de prueba previstas

1. **Arranque y contexto (`StartupTest`):** `@SpringBootTest(webEnvironment = RANDOM_PORT)`. Verifica que el contexto se levanta, la aplicación responde a peticiones y la página de inicio devuelve estado 200. Cubre RF-1, RF-4, RF-9.
2. **Migración Flyway (`MigrationTest`):** `@SpringBootTest`. Verifica que `flyway_schema_history` contiene la versión `1` como aplicada y que no existen tablas de aplicación. Ejecutado dos veces sobre la misma base para comprobar que no se reaplica. Cubre RF-4, RF-5, CL-1, CL-2.
3. **Páginas públicas (`PagesTest`):** `@SpringBootTest(webEnvironment = RANDOM_PORT)` con `TestRestTemplate`.
   - `GET /` → 200, contiene nombre de la aplicación, frase de propósito y atribución de TMDB. Cubre RF-2, RF-3, RNF-1, RNF-4.
   - `GET /cualquier-direccion-inexistente` → 404, contiene "la página no existe", enlace al inicio y atribución de TMDB. Cubre RF-8, RF-10, RNF-8.
4. **Error interno (`InternalErrorTest`):** `MockMvc` con un controlador de prueba (solo en fuentes de test) que lanza una excepción no controlada. Verifica estado 500, mensaje genérico, enlace al inicio y sin trazas. Cubre RF-7, RF-10, CL-6.
5. **Arranque sin `TMDB_API_KEY`:** dos clases, `StartupTmdbMissingTest` (variable `TMDB_API_KEY` no definida en el entorno) y `StartupTmdbEmptyTest` (variable definida vacía). Verifican que la aplicación arranca y sirve el inicio sin errores visibles. Cubre RF-9, CL-4.
6. **Clase auxiliar `TestDatabaseConfig`:** no es una prueba; agrupa la configuración de la base de pruebas (`deculto_test`) compartida por las clases anteriores.

### Estrategia de base de datos en pruebas

- Las clases de prueba fijan la URL, el usuario y la contraseña de `deculto_test` con propiedades de `@SpringBootTest(properties = ...)`. Estas propiedades tienen prioridad sobre las variables de entorno, por lo que la base de pruebas queda determinada en el propio código de prueba y no depende de la terminal, de `SPRING_PROFILES_ACTIVE` ni de `SPRING_DATASOURCE_*`.
- Las arrancadas manuales contra la base de pruebas pasan esos mismos valores como argumentos de línea de comandos, de modo que también prevalecen sobre las variables de entorno.
- Los valores de `deculto_test` son de una base local de prueba, no son secretos reales, y se documentan en el README; no se versiona ningún valor real de configuración de desarrollo (RNF-7).
- La base de desarrollo (`deculto_dev` o similar) no se toca durante la ejecución automática (CA-10).
- Requisitos de la base de datos: PostgreSQL 17 para desarrollo y pruebas. PostgreSQL 19 no es compatible con la versión de Flyway del proyecto.
- PostgreSQL debe estar arrancado antes de ejecutar `mvnw.cmd verify` (riesgo aceptado, documentado en la spec y el README).

### Casos límite cubiertos por pruebas

- CL-1, CL-2: migración en BD vacía y no reaplicación.
- CL-3: arranque con BD parada (verificado manualmente, no en prueba automática, porque impediría el contexto de prueba).
- CL-4: `TMDB_API_KEY` ausente o vacía.
- CL-7: repetición de la comprobación automática sobre BD de pruebas con datos de ejecución previa (las pruebas son idempotentes).

## 8. Relación entre cada parte del plan y los requisitos RF

| Parte del plan | RF cubiertos |
|---|---|
| `pom.xml` + `DecultoApplication.java` + `application.yml` | RF-1, RF-4, RF-5, RF-6, RF-9 |
| Migración `V1__initial_schema.sql` | RF-4, RF-5, CL-1, CL-2 |
| `HomeController` + `home.html` | RF-1, RF-2, RF-3, RF-8, RNF-1, RNF-2, RNF-3 |
| `ErrorController` + `error/404.html` + `error/500.html` | RF-7, RF-8, RF-10, RNF-1, RNF-4, RNF-8 |
| Fragmentos `header.html` + `footer.html` | RF-3, RNF-4 |
| `styles.css` | RNF-2, RNF-3 |
| Pruebas automáticas | RF-4, RF-5, RF-6, RF-7, RF-8, RF-9, RNF-5, RNF-6, CA-9, CA-10, CL-7 |
| `README.md` + `.env.example` + `.gitignore` | RF-11, RF-12, RNF-7, CL-8, CL-10 |

## 9. Criterios de finalización del plan

Este plan se considera completo cuando:
- Todos los archivos afectados están listados con responsabilidad clara.
- Cada parte del plan se relaciona con al menos un RF.
- La estrategia de pruebas usa solo herramientas existentes (JUnit, Spring Boot Test, Maven) y no añade dependencias de test.
- Todas las tareas de `tasks.md` son verificables y están ordenadas por dependencias.
- No se incluyen funcionalidades fuera de alcance (F1–F10, login, TMDB real, Docker, CI, modelo de dominio).