package utng.edu.mx.prueba.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductoRequest {

    @NotBlank(message = "El nombre no puede ser nulo o vacío")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    @Pattern(regexp = "^(?![0-9]+$)(?!null$).*$", message = "El nombre no debe contener solo números ni la palabra 'null'.")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 255, message = "La descripción debe tener máximo 255 caracteres")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precio;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Max(value = 999999, message = "La cantidad no puede exceder 999999")
    private Integer cantidad;

    @NotBlank(message = "La categoría no puede ser nula o vacía")
    @Size(max = 50, message = "La categoría debe tener máximo 50 caracteres")
    private String categoria;

    @NotBlank(message = "La firma no puede estar vacía")
    private String firma;

    // 👇 Aquí se valida que "precio" y "cantidad" no sean enviados como String
    @JsonCreator
    public ProductoRequest(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("precio") Object precio,  // Se recibe como Object para verificar su tipo
            @JsonProperty("cantidad") Object cantidad,
            @JsonProperty("categoria") String categoria) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;

        // Validar que precio sea un número real y no una cadena
        if (precio instanceof BigDecimal) {
            this.precio = (BigDecimal) precio;
        } else if (precio instanceof Number) {
            this.precio = new BigDecimal(precio.toString());
        } else {
            throw new IllegalArgumentException("El precio debe ser un número y no una cadena.");
        }

        // Validar que cantidad sea un número entero y no una cadena
        if (cantidad instanceof Integer) {
            this.cantidad = (Integer) cantidad;
        } else if (cantidad instanceof Number) {
            this.cantidad = ((Number) cantidad).intValue();
        } else {
            throw new IllegalArgumentException("La cantidad debe ser un número entero y no una cadena.");
        }
    }
}

