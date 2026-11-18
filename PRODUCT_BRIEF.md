# PRODUCT BRIEF — DeCulto

**Versión:** 0.2
**Estado:** Borrador para aprobación
**Nombre:** DeCulto
**Fecha:** Noviembre de 2026

## 1. Visión del producto

DeCulto es una aplicación web privada para un pequeño grupo de cinéfilos que quieren organizar las películas que han visto, puntuarlas, escribir reseñas y compartir recomendaciones entre ellos.

El objetivo principal es ofrecer una colección personal sencilla donde cada usuario pueda consultar sus películas y sus puntuaciones.

Compartir películas con amigos es una funcionalidad diferenciadora, pero no debe complicar la experiencia de organización personal.

Es también un proyecto de aprendizaje individual para desarrollar una aplicación web completa utilizando Java y Spring Boot.

## 2. Usuarios objetivo

Grupo privado compuesto inicialmente por:

- Fundador y administrador.
- Entre 4 y 7 cinéfilos activos.
- Entre 3 y 6 usuarios ocasionales.

No se busca crecimiento público ni monetización.

## 3. Problema

Las plataformas existentes permiten descubrir películas, registrar visionados y consultar valoraciones, pero la experiencia que se busca con DeCulto es más personal y centrada en el grupo.

Se quiere disponer de un espacio privado donde:

- Organizar películas vistas.
- Consultar rápidamente la puntuación propia.
- Consultar una media de valoraciones.
- Guardar películas pendientes.
- Escribir reseñas.
- Compartir recomendaciones con amigos.

La identificación de este problema parte inicialmente de la percepción del fundador y todavía no se ha validado con terceros.

## 4. Propuesta de valor

Una colección personal de películas, sencilla y privada, que permite organizar lo visto, puntuarlo y compartir recomendaciones con un pequeño grupo de amigos.

La nota propia debe ser siempre visible y no quedar escondida en menús o pantallas secundarias.

## 5. Objetivos

- Construir y desplegar una aplicación web funcional.
- Aprender desarrollo backend con Java y Spring Boot.
- Aprender persistencia, seguridad e integración con APIs.
- Completar un MVP en un objetivo de 6–8 semanas.
- Mantener el coste de infraestructura en 0 €, utilizando planes gratuitos disponibles.

El aprendizaje y la finalización del proyecto tienen prioridad sobre el crecimiento.

## 6. Alcance del MVP

### F1. Registro e invitaciones

- Registro mediante enlace de invitación.
- Posibilidad de desactivar invitaciones.
- Registro con email y contraseña.
- Inicio y cierre de sesión.

### F2. Búsqueda de películas

- Integración con TMDB.
- Búsqueda por título.
- Consultas en español de México (`es-MX`) e inglés (`en-US`).
- Fusión de resultados mediante el identificador de TMDB.

### F3. Ficha de película

- Póster.
- Títulos en español de México e inglés, cuando estén disponibles.
- Sinopsis.
- Año.
- Director.
- Reparto.
- Media de valoraciones, pendiente de concretar su procedencia.
- Nota propia del usuario, si existe.

### F4. Puntuación

- Escala de 1 a 10.
- Puntuar una película la incorpora a la colección.
- La puntuación puede editarse o eliminarse.
- Eliminar la puntuación elimina la película de la colección.

### F5. Colección personal

- Mostrar películas puntuadas.
- Mostrar póster y nota propia.
- Mostrar la media de valoraciones definida para el producto.
- Ordenar por nota, título, año o fecha de incorporación.

### F6. Reseñas

- Una reseña por película y usuario.
- La reseña requiere haber puntuado la película.
- Puede editarse y eliminarse.
- Al eliminar la puntuación, la reseña se conserva.
- La reseña seguirá asociada al usuario y a la película y podrá consultarse en su ficha.

### F7. Watchlist

- Añadir y quitar películas de próximas a ver.
- Puntuar una película la elimina de la watchlist.

### F8. Compartir películas

- Compartir una película con otro usuario registrado.
- Buscar destinatario por nombre de usuario exacto.
- Mensaje opcional.
- Generar una notificación para el destinatario.
- La notificación enlaza a la ficha de la película.

### F9. Búsqueda en colección

- Buscar películas dentro de la colección personal.

### F10. Perfil

- Perfil mínimo con nombre de usuario.

## 7. Fuera del MVP

- Series.
- Listas temáticas.
- Páginas de personas.
- Importación desde Letterboxd o IMDb.
- Feed público.
- Seguidores.
- Listas colaborativas.
- Notificaciones push o email.
- Aplicación nativa.
- Estadísticas avanzadas.
- Navegación de colecciones ajenas.

## 8. Experiencia y simplicidad

- Aplicación responsive y mobile-first.
- Interfaz en español de España (`es-ES`).
- Una acción principal visible por película.
- Notas visibles sin interacciones adicionales.
- Sin feed ni seguidores.
- Sin configuración avanzada.
- Navegación sencilla entre colección y ficha.
- Las colecciones de otros usuarios son privadas.
- Compartir una película no concede acceso a la colección completa del remitente.

## 9. Requisitos no funcionales

- Aplicación web responsive.
- Contraseñas almacenadas mediante hash seguro.
- HTTPS en producción.
- API key de TMDB exclusivamente en el servidor.
- Protección frente a intentos repetidos de inicio de sesión.
- Datos personales mínimos.
- Posibilidad de borrar cuenta y datos personales.
- Objetivo de capacidad: aproximadamente 100 usuarios.
- Atribución de TMDB visible.
- Accesibilidad básica: teclado, contraste y textos alternativos.
- Entorno reproducible y README.
- Infraestructura objetivo: 0 €, mediante planes gratuitos.

## 10. Flujo principal

1. Un usuario recibe una invitación.
2. Se registra e inicia sesión.
3. Busca una película.
4. Consulta su ficha.
5. La puntúa.
6. La película aparece en su colección.
7. Opcionalmente escribe una reseña, la añade a la watchlist o la comparte.
8. El destinatario recibe una notificación y puede consultar la ficha.

## 11. Entidades conceptuales

- Usuario.
- Película.
- Entrada de colección y puntuación.
- Reseña.
- Watchlist.
- Recomendación y notificación.
- Invitación.

La reseña debe poder conservarse aunque desaparezca la entrada de colección asociada a la puntuación.

## 12. Métricas de éxito

### Obligatorias

- Aplicación desplegada.
- Recorrido completo sin bloqueantes: registro → puntuar → compartir → notificación → ficha.

### Deseables

- Entre 4 y 7 usuarios registrados.
- Al menos 20 películas cargadas por el fundador antes de invitar.
- Al menos 5 recomendaciones durante las dos primeras semanas.

No se priorizan métricas de crecimiento o adquisición de usuarios.

## 13. Riesgos

- Dependencia de TMDB.
- Complejidad de autenticación y permisos.
- Alcance excesivo.
- Poca participación del grupo.
- Dificultad de despliegue.
- Retrasos derivados del aprendizaje.
- Aplicación vaclía a invitar a los primeros usuarios.

## 14. Decisiones pendientes

- **Media de valoraciones:** confirmar si se mostrará la media de usuarios de TMDB o la media de los miembros de DeCulto.
- **Contenido inicial:** decidir cómo preparar las primeras películas antes de invitar.
- **Disponibilidad real:** comprobar los planes gratuitos de alojamiento y base de datos antes del despliegue.

## 15. Criterio general de producto

La organización personal es el núcleo de DeCulto. Las funcionalidades sociales deben complementar esa experiencia, nunca complicarla.

El fundador conserva la decisión final sobre el producto, el alcance y las prioridades.
