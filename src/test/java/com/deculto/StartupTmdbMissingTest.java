package com.deculto;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

/**
 * Arranque con la variable TMDB_API_KEY ausente del entorno (RF-9, CA-4, CL-4).
 * Una prueba de contexto normal no puede quitar una variable de entorno, así que se sustituye
 * la fuente de variables del sistema por una copia sin TMDB_API_KEY. Así la aplicación ve la
 * variable exactamente como si no estuviera definida. No se usa un valor vacío en este caso.
 * Sin @SpringBootTest: el contexto se arranca a mano para poder preparar el entorno antes.
 */
class StartupTmdbMissingTest {

    @Test
    void arrancaYSirveLaPaginaDeInicioSinLaVariable() throws IOException, InterruptedException {
        Map<String, Object> entornoSinClave = new HashMap<>(System.getenv());
        entornoSinClave.keySet().removeIf("TMDB_API_KEY"::equalsIgnoreCase);

        ApplicationContextInitializer<ConfigurableApplicationContext> sinClave = contexto -> {
            MutablePropertySources fuentes = contexto.getEnvironment().getPropertySources();
            fuentes.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                new MapPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, entornoSinClave));
        };

        String[] argumentos = new String[TestDatabaseConfig.ARGUMENTOS.length + 1];
        System.arraycopy(TestDatabaseConfig.ARGUMENTOS, 0, argumentos, 0, TestDatabaseConfig.ARGUMENTOS.length);
        argumentos[argumentos.length - 1] = "--server.port=0";

        try (ConfigurableApplicationContext contexto = new SpringApplicationBuilder(DecultoApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .initializers(sinClave)
                .run(argumentos)) {

            assertThat(contexto.getEnvironment().containsProperty("TMDB_API_KEY")).isFalse();
            assertThat(contexto.getEnvironment().getProperty("tmdb.api.key")).isEmpty();

            String puerto = contexto.getEnvironment().getProperty("local.server.port");
            HttpResponse<String> respuesta = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + puerto + "/")).GET().build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            assertThat(respuesta.statusCode()).isEqualTo(200);
            assertThat(respuesta.body()).contains("Tu colección privada de películas.");
        }
    }
}
