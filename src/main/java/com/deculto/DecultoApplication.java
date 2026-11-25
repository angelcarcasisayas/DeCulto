package com.deculto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion DeCulto.
 * Spring Boot levanta el contexto de aplicacion y, si no hay conexion a la
 * base de datos configurada, falla el arranque con un mensaje comprensible.
 */
@SpringBootApplication
public class DecultoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DecultoApplication.class, args);
    }
}