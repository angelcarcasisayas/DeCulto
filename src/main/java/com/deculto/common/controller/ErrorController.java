package com.deculto.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de errores propios.
 * Lee el código de estado de la petición y develm�e la plantilla adecuada
 * (404 o 500) sin exponer trazas ni detalles técnicos internos.
 */
@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode != null && statusCode == 500) {
            response.setStatus(500);
            return "error/500";
        }
        response.setStatus(404);
        return "error/404";
    }

}