# AGENTS.md — DeCulto

Web en español donde un grupo privado de cinéfilos registra, puntúa (1–10),
reseña y comparte películas. Proyecto de aprendizaje de un solo desarrollador,
10–12 semanas, infraestructura de 0 €.

Alcance v1: registro por invitación, búsqueda en TMDB, ficha, puntuación,
colección ordenable, reseña, watchlist, compartir con notificaciones y perfil
mínimo. Fuera: series, listas temáticas, páginas de persona, importación, feed
público, seguidores, colaboración, push/email y app nativa.

Estado y decisiones: `MEMORY.md`.

## Stack y estructura

- Java LTS + Spring Boot (versiones en `pom.xml`; no actualizar sin aprobación).
- Thymeleaf con renderizado en servidor. Sin framework ni dependencias de frontend.
- PostgreSQL + Flyway + Spring Data JPA.
- Spring Security con formulario y sesión. Sin JWT ni OAuth.
- Maven con wrapper (`mvnw`). Docker → Render free. BD en Neon o Supabase free.
- TMDB, única API externa, siempre llamada desde el servidor.

```
src/main/java/com/deculto/
  config/        SecurityConfig, TmdbConfig
  user/          registro, login, perfil, invitaciones
  movie/         búsqueda, ficha, cliente TMDB, caché
  collection/    valoraciones, reseñas, watchlist, orden
  sharing/       recomendaciones y notificaciones
  common/        errores, validación, utilidades
src/main/resources/
  templates/     vistas Thymeleaf
  static/        css y js nativo
  db/migration/  Flyway (V1__init.sql, ...)
  application.yml
```

Solo `movie/` habla con TMDB; `collection/` trabaja con películas ya persistidas.

## Comandos

```bash
mvnw.cmd spring-boot:run      # desarrollo
mvnw.cmd test                 # pruebas
mvnw.cmd verify               # compilar + pruebas + checks
mvnw.cmd clean package        # empaquetar
docker build -t deculto .     # imagen
docker run --env-file .env -p 8080:8080 deculto
```

Variables: `TMDB_API_KEY`, `SPRING_DATASOURCE_URL`,
`SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`,
`SPRING_PROFILES_ACTIVE`.

Lint: no configurado en la v1. No añadir sin aprobación.

## Convenciones

- Identificadores en inglés; interfaz en español (tuteo); comentarios en español
  solo para explicar decisiones, nunca el qué.
- Java: inyección por constructor, sin Lombok, `record` para DTO, sin
  `System.out.println`.
- Base de datos: plural y `snake_case`; `created_at`/`updated_at` con zona horaria.
- Capas: `controller` valida y delega · `service` contiene las reglas ·
  `repository` accede a datos. Las plantillas no consultan repositorios.
- CSS con variables y mobile-first; JS nativo sin `console.log`.
- Referencia: `movie/MovieController.java`, `collection/RatingService.java`,
  `db/migration/V1__init.sql`.

## Reglas de dominio / trampas conocidas

- **Nota propia y media siempre visibles**, en la colección y en la ficha, sin
  menús ni clics extra. Es la razón de ser del producto.
- **Escala 1–10.** La media de TMDB ya viene sobre 10: no convertir.
- **Puntuar crea la entrada en la colección.** Borrar la nota saca la película de
  la colección, pero **conserva la reseña**, que sigue visible en la ficha.
- **No se reseña sin haber puntuado.** Puntuar saca la película de la watchlist.
- **Colecciones privadas:** toda consulta se filtra por el usuario autenticado. No
  existe vista ni endpoint con datos de otro usuario.
- **Compartir** exige destinatario registrado, búsqueda por usuario exacto, y
  genera una notificación que enlaza a la ficha.
- **TMDB se cachea en `movies` al primer acceso.** Solo endpoints de película
  (`/movie`, `/search/movie`); nunca `/tv` ni `/person`.
- **Búsqueda bilingüe:** cada búsqueda consulta TMDB en `es-MX` y en `en-US` y
  fusiona resultados por `id`. Se busca al enviar el formulario o con retardo,
  nunca en cada pulsación.
- **Metadatos en `es-MX`** con fallback a `en-US`. Atribución de TMDB visible en el
  pie de todas las páginas.

## Forma de trabajar

- Planifica y espera aprobación si el cambio es una funcionalidad nueva, toca más
  de 2 archivos, modifica el esquema, afecta a seguridad o es ambiguo.
- Una funcionalidad por rama. Si un cambio toca más de 5 archivos, divídelo.
- Sin refactors fuera de alcance: anota, no arregles.
- Al terminar: qué cambió, cómo verificarlo, qué queda pendiente. Actualiza
  `MEMORY.md`.
- Commits con Conventional Commits en español: `feat: valorar películas de 1 a 10`.

## Límites

**✅ Siempre**
- Crear una migración Flyway para cada cambio de esquema.
- Validar entradas y mostrar errores comprensibles.

**⚠️ Pregunta antes**
- Añadir cualquier dependencia (Maven, CSS o JS).
- Crear archivos o carpetas fuera de la estructura.
- Cambiar el esquema de datos.
- Modificar este archivo, `pom.xml` o las versiones del stack.
- Implementar algo fuera del alcance de la v1.

**🚫 Nunca**
- Implementar fuera de la v1: series, listas, personas, importación, feed público,
  seguidores, colaboración, push/email, app nativa, ver colecciones ajenas.
- Desactivar CSRF, validaciones o seguridad.
- Exponer secretos o commitear `.env`.
- Registrar datos personales en logs.
- Permitir que un usuario acceda a datos de otro.
- Añadir frameworks o dependencias de frontend.
- Editar migraciones ya aplicadas o reescribir el historial.

## Verificación

`mvnw.cmd verify` debe pasar, y además:

- Recorrido completo con dos cuentas: registro → puntuar → colección → reseña →
  watchlist → compartir → notificación.
- Privacidad: la segunda cuenta no accede a datos de la primera (403 o 404).
- Fallo de TMDB (clave inválida): error claro, nunca pantalla en blanco.
- Responsive a 375 px y 1440 px.
- Atribución de TMDB visible.
