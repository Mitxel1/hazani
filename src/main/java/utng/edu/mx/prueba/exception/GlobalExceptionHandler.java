package utng.edu.mx.prueba.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import utng.edu.mx.prueba.model.ProductoResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Error de validación: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        response.put("codigo", "400");
        response.put("mensaje", "Error de validación en los datos de entrada");
        response.put("errores", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductoNotFoundException(ProductoNotFoundException ex) {
        log.error("Producto no encontrado: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "404");
        response.put("mensaje", ex.getMessage());
        response.put("operacion", "consultar");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Argumento ilegal: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "400");
        response.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Captura cualquier otra excepción general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Error interno: {}", ex.getMessage(), ex);

        String operacion = "desconocida";
        String path = request.getRequestURI();

        if (path.contains("crearProducto")) {
            operacion = "agregar";
        } else if (path.contains("/buscar") || request.getMethod().equals("GET")) {
            operacion = "consultar";
        } else if (request.getMethod().equals("PUT")) {
            operacion = "actualizar";
        } else if (request.getMethod().equals("DELETE")) {
            operacion = "eliminar";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("codigo", "500");
        response.put("mensaje", "Ha ocurrido un error - " + operacion);
        response.put("operacion", operacion);
        response.put("detalle", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProductoResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Error de deserialización: {}", ex.getMessage());
        ProductoResponse errorResponse = new ProductoResponse();
        errorResponse.setCodigo("400");
        errorResponse.setMensaje("Error en el formato de los datos: " + ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
