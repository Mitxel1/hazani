package com.utng.edu.prueba.model;

import com.utng.edu.prueba.model.DefaultResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserResponse {
    private String nombre;
    private String password;
    private Boolean estatus;

    private String newNombre;
    private String newPassword;
    private Boolean newStatus;
}
