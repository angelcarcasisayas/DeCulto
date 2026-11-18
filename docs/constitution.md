# Constitución de DeCulto

1. **Stack mínimo.** Solo Spring Boot, Thymeleaf, PostgreSQL, Flyway y Maven; ninguna dependencia nueva sin aprobación.
2. **La spec manda.** Nada se implementa sin requisito aprobado en `PRODUCT_BRIEF.md` o `MEMORY.md`; si spec y código discrepan, el código está mal.
3. **Dominio sin interfaz.** Las reglas viven en `service`, fuera de controladores y plantillas, y se prueban sin arrancar la web.
4. **Tests con lo que ya hay.** Cada regla de dominio tiene su prueba y `mvnw.cmd verify` pasa antes de cerrar; sin dependencias nuevas de test.
5. **Datos privados.** Colección, notas, reseñas y watchlist son privadas: toda consulta se filtra por el usuario autenticado.
6. **Idioma estable.** Identificadores y código en inglés; interfaz y textos en español de España; comentarios solo para decisiones.