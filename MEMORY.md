# MEMORY.md — DeCulto

Memoria del proyecto entre sesiones. Mantener concisa y actualizar cuando cambien decisiones o estado.

## Estado actual

- Fase: descubrimiento y definición del producto.
- Sin funcionalidades implementadas.
- Product Brief en preparación para aprobación.
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

## Decisiones pendientes

- Confirmar si la media mostrada procede de TMDB o de las valoraciones de los miembros de DeCulto.
- Confirmar proveedores y límites gratuitos de alojamiento y base de datos.

## Documentación

- `AGENTS.md`: instrucciones de trabajo y convenciones técnicas.
- `MEMORY.md`: decisiones aprobadas y estado del proyecto.
- `PRODUCT_BRIEF.md`: definición, alcance y requisitos del producto.

## Aprendizajes y errores

- Pendiente de registrar.

## Próximos pasos

1. Aprobar y revisar `PRODUCT_BRIEF.md`.
2. Alinear `AGENTS.md` y `MEMORY.md`.
3. Preparar el esqueleto técnico inicial.
4. Implementar el MVP de forma incremental.
5. Desplegar y verificar el flujo completo.