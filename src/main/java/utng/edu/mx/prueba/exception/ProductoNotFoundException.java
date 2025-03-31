package utng.edu.mx.prueba.exception;

// Excepción personalizada para manejar errores de negocio
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
