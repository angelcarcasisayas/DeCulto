package com.deculto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Comprueba el contenido y el estado de la página de inicio y de la página 404 (RF-2, RF-3, RF-8, RF-10, RNF-8).
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/deculto_test",
    "spring.datasource.username=deculto_test",
    "spring.datasource.password=deculto_test"
})
class PagesTest {

    private final TestRestTemplate http;

    @Autowired
    PagesTest(TestRestTemplate http) {
        this.http = http;
    }

    @Test
    void paginaDeInicioDevuelve200ConNombrePropositoYAtribucionTmdb() {
        ResponseEntity<String> respuesta = http.getForEntity("/", String.class);
        String cuerpo = respuesta.getBody();

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cuerpo)
            .contains("<html lang=\"es-ES\"")
            .contains("<h1>DeCulto</h1>")
            .contains("Tu colección privada de películas.")
            .contains("Datos de películas proporcionados por")
            .contains("https://www.themoviedb.org/");
    }

    @Test
    void paginaDeInicioNoTieneEnlacesDeNavegacionNiTrazas() {
        String cuerpo = http.getForEntity("/", String.class).getBody();

        assertThat(cuerpo)
            .doesNotContain("<nav")
            .doesNotContain("Whitelabel")
            .doesNotContain("Exception");
    }

    @Test
    void direccionInexistenteDevuelve404ConPaginaDeNoExistencia() {
        ResponseEntity<String> respuesta = http.getForEntity("/cualquier-direccion-inexistente", String.class);
        String cuerpo = respuesta.getBody();

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(cuerpo)
            .contains("La página que buscas no existe.")
            .contains("<a href=\"/\">Volver al inicio</a>")
            .contains("<h1>DeCulto</h1>")
            .contains("https://www.themoviedb.org/");
    }

    @Test
    void direccionInexistenteNoMuestraDetallesTecnicos() {
        String cuerpo = http.getForEntity("/cualquier-direccion-inexistente", String.class).getBody();

        assertThat(cuerpo)
            .doesNotContain("Whitelabel")
            .doesNotContain("Exception")
            .doesNotContain("at com.deculto")
            .doesNotContain("org.springframework");
    }
}
