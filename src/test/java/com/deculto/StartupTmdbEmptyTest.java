package com.deculto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Arranque con la variable TMDB_API_KEY definida pero vacía (RF-9, CA-4, CL-4).
 * La propiedad en línea tiene prioridad sobre el entorno, así que el resultado no depende del .env.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/deculto_test",
    "spring.datasource.username=deculto_test",
    "spring.datasource.password=deculto_test",
    "TMDB_API_KEY="
})
class StartupTmdbEmptyTest {

    private final TestRestTemplate http;
    private final Environment entorno;

    @Autowired
    StartupTmdbEmptyTest(TestRestTemplate http, Environment entorno) {
        this.http = http;
        this.entorno = entorno;
    }

    @Test
    void sirveLaPaginaDeInicioConLaClaveVacia() {
        ResponseEntity<String> respuesta = http.getForEntity("/", String.class);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(respuesta.getBody()).contains("Tu colección privada de películas.");
        assertThat(entorno.getProperty("tmdb.api.key")).isEmpty();
    }
}
