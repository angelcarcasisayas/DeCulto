# MEMORY.md — DeCulto

Memoria del proyecto entre sesiones. Máximo ~50 líneas: resume o elimina lo que ya no
aporte.

## Estado actual

- Fase 0: descubrimiento cerrado, sin código.
- Stack: Java + Spring Boot + Thymeleaf SSR, PostgreSQL, Flyway, Spring Security (sesión).
- Sin dependencias de frontend. Infraestructura 0 € (Render free + Neon/Supabase free).
- Alcance v1: invitación, TMDB, ficha, nota 1–10, colección, reseña, watchlist, compartir.

## Decisiones (y por qué)

- Aprendizaje y despliegue por encima de crecimiento: proyecto personal, 10–12 semanas a 10 h/semana.
- Organización personal como héroe: compartir no debe añadir complejidad a la colección.
- Nota 1–10: coincide con la escala de TMDB, sin conversiones.
- Login y colección privada: compartir exige destinatario identificado.
- TMDB única API, media de usuarios (no críticos).
- Búsqueda bilingüe `es-MX` + `en-US` fusionada por `id`: se busca en ambos idiomas.
- Reseña exige nota y sobrevive al borrarla: la colección solo muestra películas puntuadas.
- Puntuar saca de la watchlist: no puede estar en las dos.
- Thymeleaf SSR en vez de SPA: menos plomería y menos riesgo en 96 h estimadas.
- Solo películas, sin series ni listas: alcance controlado para terminar.
- Sin documentos externos: AGENTS.md autocontenido y este archivo como único estado.

## Aprendizajes y errores a evitar

- (vacío por ahora)

## Próximos pasos

- Crear repo, `pom.xml`, `Dockerfile` y primer despliegue vacío.
- Verificar los free tiers vigentes de Render y Neon/Supabase.
- Implementar: auth → TMDB → ficha → nota → colección → reseña → watchlist → compartir.
- Cargar 20–30 películas antes de invitar.
