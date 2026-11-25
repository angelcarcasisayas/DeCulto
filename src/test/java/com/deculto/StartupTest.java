package com.deculto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Comprueba que la aplicación arranca contra la base de datos de pruebas y responde.
 * La base queda fijada en la propia clase, no en el entorno (RNF-6).
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/deculto_test",
    "spring.datasource.username=deculto_test",
    "spring.datasource.password=deculto_test"
})
class StartupTest {

    private final TestRestTemplate http;
    private final JdbcTemplate jdbc;

    @Autowired
    StartupTest(TestRestTemplate http, JdbcTemplate jdbc) {
        this.http = http;
        this.jdbc = jdbc;
    }

    @Test
    void conectaConLaBaseDePruebasYNoConLaDeDesarrollo() {
        String baseActual = jdbc.queryForObject("select current_database()", String.class);

        assertThat(baseActual).isEqualTo("deculto_test");
    }

    @Test
    void laAplicacionResponderAPeticionesEnLaRaiz() {
        ResponseEntity<String> respuesta = http.getForEntity("/", String.class);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getHeaders().getContentType().toString()).startsWith("text/html");
    }
}
