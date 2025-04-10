package com.utng.edu.prueba.model.productos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductoUpdateRequest {

    @NotNull(message = "El nombre original es obligatorio")
    @NotBlank(message = "El nombre original no puede estar vacío")
    @Size(max = 100, message = "El nombre original no puede exceder los 100 caracteres")
    private String nombreOriginal; // Nombre original (para buscar el producto)

    private String nuevoNombre; // Nuevo nombre (opcional, para actualizar)

    private String descripcion;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    private String categoria;

    @NotNull(message = "La firma es obligatoria")
    private String firma;
}