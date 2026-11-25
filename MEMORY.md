# MEMORY.md — DeCulto

Memoria del proyecto entre sesiones. Mantener concisa y actualizar cuando cambien decisiones o estado.

## Estado actual

- Fase: esqueleto técnico implementado (spec 001); siguiente, especificar F1.
- Esqueleto técnico (spec 001) implementado y verificado: arranque, migración de Flyway, páginas de inicio y error, y 13 pruebas automáticas que pasan.
- Sin funcionalidades de producto implementadas.
- Product Brief v0.2 aprobado.
- Objetivo: MVP en 6–8 semanas.
- Proyecto individual orientado al aprendizaje y despliegue.

## Decisiones aprobadas

- Nombre: DeCulto.
- Stack: Java LTS, Spring Boot, Spring MVC, Thymeleaf SSR, PostgreSQL, Flyway, Spring Security con sesiones y Maven.
- Sin framework ni dependencias de frontend.
- Interfaz en español de España (`es-ES`).
- Búsqueda TMDB en `es-MX` y `en-US`, fusionando por ID.
- Mostrar ambos títulos de la película cuando estén disponibles.
- Metadatos de películas en `es-MX`, con fallback a `en-US`.
- Escala de puntuación de 1–10.
- Puntuar incorpora la película a la colección y la saca de la watchlist.
- Eliminar la puntuación elimina la película de la colección, pero conserva la reseña.
- Una reseña requiere puntuación previa.
- Colecciones privadas: no se permite consultar la colección completa de otro usuario.
- Compartir exige destinatario registrado y genera una notificación.
- TMDB es la única API externa prevista.
- Objetivo de infraestructura: 0 €, usando planes gratuitos.
- Organización personal como prioridad; compartir es complementario.
- Flujo SDD con agentes de OpenCode en `.opencode/agents/`: `coordinator` es primary y delega; `planner`, `implementer` y `reviewer` son subagentes.
- El implementer solo puede escribir en `src/**`, `specs/**` y `MEMORY.md`; el resto de rutas queda bloqueado.
- Sus comandos de terminal se limitan a `mvnw.cmd`, `git status` y `git diff`; cualquier otro pide permiso al usuario.
- PostgreSQL 17 como versión de base de datos probada, con la dependencia `org.flywaydb:flyway-database-postgresql` (versión gestionada por Spring Boot).
- Las pruebas fijan su base de datos (`deculto_test`) en código con `@SpringBootTest(properties)`, sin perfil `test`.

## Decisiones pendientes

- Confirmar si la media mostrada procede de TMDB o de las valoraciones de los miembros de DeCulto.
- Confirmar proveedores y límites gratuitos de alojamiento y base de datos.

## Documentación

- `AGENTS.md`: instrucciones de trabajo y convenciones técnicas.
- `MEMORY.md`: decisiones aprobadas y estado del proyecto.
- `PRODUCT_BRIEF.md`: definición, alcance y requisitos del producto.

## Aprendizajes y errores

- PostgreSQL 19 no es compatible con Flyway 11.7.2. La dependencia `flyway-database-postgresql` es necesaria desde Flyway 10.
- Las variables `SPRING_DATASOURCE_*` de `.env` tienen prioridad sobre el perfil, por lo que las pruebas necesitan fijar su propia base de datos en código.

## Próximos pasos

1. Redactar la spec de registro e invitaciones (F1).
2. Alinear `AGENTS.md` y `MEMORY.md`.
3. Implementar el MVP de forma incremental.
4. Desplegar y verificar el flujo completo.