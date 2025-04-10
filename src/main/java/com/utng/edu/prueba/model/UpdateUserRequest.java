package com.utng.edu.prueba.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserRequest {
    @NotNull
    private String nombre;

    @NotNull
    private String nombreBuscar;

    @NotNull
    private String password;

    @NotNull
    private Boolean estatus;

    @NotNull
    private String firma;

    @NotNull(message = "El correo no puede ser nulo")
    private String correo;  // Asegúrate de que esté aquí para poder usar getTelefono()

    @NotNull(message = "El teléfono no puede ser nulo")
    private  String rol;

    private boolean doblePasoActivado;
}
