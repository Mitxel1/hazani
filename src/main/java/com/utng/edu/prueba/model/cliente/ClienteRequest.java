package com.utng.edu.prueba.model.cliente;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequest {


    @NotNull(message = "La edad es obligatoria")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de correo inválido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^(\\+52)?\\d{10}$", message = "El teléfono debe ser de México y contener 10 dígitos")
    private String telefono;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private Integer edad;

    @NotBlank(message = "El sexo no puede estar vacío")
    @Pattern(regexp = "^(M|F|Otro)$", message = "El sexo debe ser 'M', 'F' o 'Otro'")
    private String sexo;
}
