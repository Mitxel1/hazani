package com.utng.edu.prueba.model.productos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDeleteRequest {

    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotNull(message = "La firma es obligatoria")
    private String firma;
}