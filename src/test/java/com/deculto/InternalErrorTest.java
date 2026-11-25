package com.deculto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Comprueba la página de error interno (RF-7, RF-10, CL-6).
 * Usa un servidor real y no MockMvc: MockMvc propaga la excepción al test y no pasa por la
 * página de error de la aplicación, que es lo que se quiere verificar.
 * El controlador que provoca el error existe solo en las fuentes de prueba y solo en esta clase.
 */
@Import(InternalErrorTest.ControladorErrorPrueba.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/deculto_test",
    "spring.datasource.username=deculto_test",
    "spring.datasource.password=deculto_test"
})
class InternalErrorTest {

    private final TestRestTemplate http;

    @Autowired
    InternalErrorTest(TestRestTemplate http) {
        this.http = http;
    }

    @Test
    void errorInternoDevuelve500ConPaginaDeErrorGenerica() {
        ResponseEntity<String> respuesta = http.getForEntity("/prueba/error-interno", String.class);
        String cuerpo = respuesta.getBody();

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(cuerpo)
            .contains("Algo ha fallado.")
            .doesNotContain("no existe")
            .contains("<a href=\"/\">Volver al inicio</a>")
            .contains("<h1>DeCulto</h1>")
            .contains("https://www.themoviedb.org/");
    }

    @Test
    void errorInternoNoMuestraTrazasNiDetallesTecnicos() {
        String cuerpo = http.getForEntity("/prueba/error-interno", String.class).getBody();

        assertThat(cuerpo)
            .doesNotContain("IllegalStateException")
            .doesNotContain("fallo simulado")
            .doesNotContain("at com.deculto")
            .doesNotContain("Whitelabel")
            .doesNotContain("Exception");
    }

    @RestController
    static class ControladorErrorPrueba {

        @GetMapping("/prueba/error-interno")
        String provocarErrorInterno() {
            throw new IllegalStateException("fallo simulado para la prueba");
        }
    }
}
