package com.utng.edu.prueba.controller;
import com.utng.edu.prueba.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice


public class GlobalExcepcion {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExcepcion.class);

    // Manejo de errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        // Crear mensaje detallado con los campos que fallaron
        String errorMessage = "Error de validación en los datos de entrada: " + errors.toString();

        // Log detallado del error
        logger.error("Error de validación: {}", errorMessage);

        // Respuesta estructurada
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                "Validación"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Manejo de excepciones personalizadas
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Crear mensaje detallado con el tipo de excepción
        String errorMessage = "Error en la solicitud (" + ex.getClass().getSimpleName() + "): " + ex.getMessage();

        // Log detallado del error
        logger.error(errorMessage, ex);

        // Respuesta estructurada
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                "Operación específica"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Manejo de excepciones genéricas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Crear mensaje detallado con el tipo de excepción
        String errorMessage = "Error interno del servidor (" + ex.getClass().getSimpleName() + "): " + ex.getMessage();

        // Log detallado del error
        logger.error(errorMessage, ex);

        // Respuesta estructurada
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                errorMessage,
                "Operación no especificada"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}