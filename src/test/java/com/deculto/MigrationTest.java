package com.deculto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Comprueba la migración inicial de Flyway sobre la base de datos de pruebas (RF-4, RF-5, CL-1, CL-2).
 * Solo lee el historial y los metadatos: no escribe datos, así que es repetible (CL-7).
 */
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/deculto_test",
    "spring.datasource.username=deculto_test",
    "spring.datasource.password=deculto_test"
})
class MigrationTest {

    private final JdbcTemplate jdbc;

    @Autowired
    MigrationTest(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Test
    void registraLaVersionInicialComoAplicadaEnElHistorial() {
        Integer aplicadas = jdbc.queryForObject(
            "select count(*) from flyway_schema_history where version = '1' and success = true",
            Integer.class);

        assertThat(aplicadas).isEqualTo(1);
    }

    @Test
    void noExisteNingunaTablaDeAplicacionEnElEsquemaActual() {
        List<String> tablas = jdbc.queryForList(
            "select table_name from information_schema.tables "
                + "where table_schema = current_schema() and table_type = 'BASE TABLE' "
                + "and table_name <> 'flyway_schema_history'",
            String.class);

        assertThat(tablas).isEmpty();
    }

    @Test
    void noReaplicaLaMigracionEnUnSegundoArranqueSobreLaMismaBase() {
        Map<String, Object> filaAntes = filaVersionInicial();
        Integer totalAntes = totalFilasHistorial();

        try (ConfigurableApplicationContext segundoArranque = new SpringApplicationBuilder(DecultoApplication.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .run(TestDatabaseConfig.ARGUMENTOS)) {
            assertThat(segundoArranque.isActive()).isTrue();
        }

        assertThat(filaVersionInicial()).isEqualTo(filaAntes);
        assertThat(totalFilasHistorial()).isEqualTo(totalAntes);
    }

    private Map<String, Object> filaVersionInicial() {
        return jdbc.queryForMap(
            "select installed_rank, installed_on, success from flyway_schema_history where version = '1'");
    }

    private Integer totalFilasHistorial() {
        return jdbc.queryForObject("select count(*) from flyway_schema_history", Integer.class);
    }
}
