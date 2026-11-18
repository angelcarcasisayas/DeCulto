# AGENTS.md — DeCulto

## Documentación del proyecto

Antes de trabajar, consulta los documentos relevantes:

- `PRODUCT_BRIEF.md`: visión, usuarios, alcance y requisitos del producto.
- `MEMORY.md`: decisiones aprobadas y estado actual.
- `AGENTS.md`: instrucciones de trabajo, convenciones y límites técnicos.

Si encuentras contradicciones entre documentos, no decidas por tu cuenta. Expón el conflicto y solicita la decisión del usuario.

El usuario conserva siempre la decisión final sobre producto, alcance, prioridades y metodología.

## Stack tecnológico

- Java LTS + Spring Boot. Las versiones se definen en `pom.xml`; no actualizarlas sin aprobación.
- Spring MVC para gestionar las peticiones web.
- Thymeleaf con renderizado en servidor (SSR).
- HTML, CSS y JavaScript nativo. Sin frameworks ni dependencias de frontend.
- PostgreSQL como base de datos.
- Spring Data JPA para persistencia.
- Flyway para migraciones.
- Spring Security con formulario y sesión. Sin JWT ni OAuth.
- Maven con wrapper (`mvnw`).
- Docker para empaquetado cuando corresponda.
- Despliegue previsto en Render y base de datos en Neon o Supabase, sujetos a verificar sus planes gratuitos.
- TMDB es la única API externa prevista y siempre se consulta desde el servidor.

## Estructura de paquetes

```text
src/main/java/com/deculto/
  config/        SecurityConfig, TmdbConfig
  user/          registro, login, perfil, invitaciones
  movie/         búsqueda, ficha, cliente TMDB, caché
  collection/    valoraciones, reseñas, watchlist, orden
  sharing/       recomendaciones y notificaciones
  common/        errores, validación, utilidades

src/main/resources/
  templates/     vistas Thymeleaf
  static/        CSS y JavaScript nativo
  db/migration/  migraciones Flyway
  application.yml
```

Solo `movie/` se comunica con TMDB. `collection/` trabaja con películas persistidas localmente.

## Comandos

En Windows:

```bat
mvnw.cmd spring-boot:run
mvnw.cmd test
mvnw.cmd verify
mvnw.cmd clean package
```

Docker, cuando se utilice:

```bat
docker build -t deculto .
docker run --env-file .env -p 8080:8080 deculto
```

Variables de entorno previstas:

- `TMDB_API_KEY`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_PROFILES_ACTIVE`

Lint: no configurado para la v1. No añadir herramientas de lint sin aprobación.

## Convenciones

- Identificadores en inglés.
- Interfaz en español de España y tratamiento de tú.
- Comentarios en español solo para explicar decisiones, nunca para describir lo evidente.
- Java: inyección por constructor, sin Lombok y uso de `record` para DTO cuando corresponda.
- No utilizar `System.out.println`.
- Base de datos: nombres plurales y `snake_case`.
- Fechas de auditoría con zona horaria: `created_at` y `updated_at`.
- Capas:
  - `controller`: recibe peticiones, valida y delega.
  - `service`: contiene reglas de negocio.
  - `repository`: accede a datos.
  - `templates`: no consulta repositorios directamente.
- CSS con variables y enfoque mobile-first.
- JavaScript nativo sin `console.log`.

## Reglas técnicas y de dominio

- La escala de puntuación es de 1 a 10.
- Puntuar una película crea su entrada en la colección.
- Eliminar una puntuación saca la película de la colección.
- La reseña se conserva aunque se elimine la puntuación. No debe depender exclusivamente de la existencia de la entrada de colección.
- No se puede escribir una reseña sin haber puntuado la película.
- Puntuar elimina la película de la watchlist.
- Las consultas de colecciones se filtran siempre por el usuario autenticado.
- No permitir consultar colecciones completas de otros usuarios.
- Compartir exige un destinatario registrado y genera una notificación enlazada a la ficha.
- Compartir una película no concede acceso a la colección del remitente.
- TMDB se cachea en `movies` al primer acceso.
- Utilizar únicamente endpoints de películas de TMDB. Nunca `/tv` ni `/person`.
- Las búsquedas consultan `es-MX` y `en-US` y fusionan resultados por ID.
- Mostrar ambos títulos cuando estén disponibles.
- Metadatos en `es-MX`, con fallback a `en-US`.
- La interfaz debe estar en español de España.
- La atribución de TMDB debe aparecer en el pie de todas las páginas.

## Forma de trabajar

- Para funcionalidades nuevas, prepara un plan y espera aprobación antes de modificar código.
- También solicita aprobación cuando el cambio afecte a más de dos archivos, modifique el esquema, afecte a seguridad o tenga requisitos ambiguos.
- Una funcionalidad por rama.
- Si un cambio afecta a más de cinco archivos, propone dividirlo.
- No hagas refactors fuera del alcance aprobado.
- Si detectas mejoras ajenas a la tarea, anótalas y comunícalas sin implementarlas.
- No decidas por el usuario qué funcionalidades relacionadas deben incluirse.
- No introduzcas dependencias, tecnologías ni patrones innecesarios.
- La metodología y el orden de trabajo los decide el usuario. Puedes recomendar alternativas y explicar riesgos, pero no imponer fases.
- Al terminar, informa de archivos modificados, comprobaciones realizadas, resultados y tareas pendientes.
- Actualiza `MEMORY.md` cuando cambien decisiones o estado relevante del proyecto.

## Límites

## Reglas de trabajo

- Lee `CONSTITUTION.md` y `AGENTS.md` antes de trabajar.
- Antes de modificar código relacionado con una funcionalidad, consulta su spec activa en `specs/NNN-nombre/`.
- No implementes requisitos que no estén aprobados.
- Si no existe una spec cuando es necesaria, detente y propón crearla.
- Respeta las aprobaciones del usuario antes de avanzar de fase.

### Siempre

- Crear una migración Flyway por cada cambio de esquema.
- Validar entradas y mostrar errores comprensibles.
- Proteger los datos de cada usuario.
- Mantener los secretos fuera del repositorio.

### Preguntar antes

- Añadir dependencias Maven, CSS o JavaScript.
- Crear archivos o carpetas fuera de la estructura definida.
- Cambiar el esquema de datos.
- Modificar este archivo o `pom.xml`.
- Actualizar versiones del stack.
- Implementar funcionalidades fuera del alcance aprobado.

### Nunca

- Implementar funcionalidades excluidas del MVP sin aprobación.
- Desactivar CSRF, validaciones o mecanismos de seguridad.
- Exponer secretos o subir `.env` al repositorio.
- Registrar datos personales en logs.
- Permitir que un usuario acceda a datos privados de otro.
- Añadir frameworks o dependencias de frontend.
- Editar migraciones ya aplicadas o reescribir el historial.

## Verificación

Utilizar comprobaciones proporcionales al cambio.

Como referencia:

- `mvnw.cmd verify` para la verificación general.
- Pruebas de los componentes afectados.
- Recorrido con dos cuentas: registro → puntuar → colección → reseña → watchlist → compartir → notificación → ficha.
- Comprobar que la segunda cuenta no puede acceder a la colección privada de la primera (403 o 404).
- Comprobar errores de TMDB, incluida una clave inválida.
- Comprobar diseño responsive a 375 px y 1440 px.
- Comprobar que la atribución de TMDB está visible.

No ejecutes verificaciones costosas o ajenas al cambio sin una razón justificada.