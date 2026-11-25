package com.deculto;

/**
 * Configuración de la base de datos de pruebas (deculto_test), separada de la de desarrollo.
 * Las pruebas que arrancan la aplicación con SpringApplicationBuilder reciben estos valores
 * como argumentos de línea de comandos, que tienen prioridad sobre las variables de entorno:
 * así el resultado no depende del fichero .env del usuario.
 */
final class TestDatabaseConfig {

    static final String[] ARGUMENTOS = {
        "--spring.datasource.url=jdbc:postgresql://localhost:5432/deculto_test",
        "--spring.datasource.username=deculto_test",
        "--spring.datasource.password=deculto_test",
        "--spring.main.banner-mode=off"
    };

    private TestDatabaseConfig() {
    }
}
