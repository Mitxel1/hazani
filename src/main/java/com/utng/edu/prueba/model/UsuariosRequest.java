package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuariosRequest {
    private String username;
    private String password;
    private String correo;
    private String rol;
    private boolean estatus;
    private String firma;
    private boolean doblePasoActivado; // Nuevo campo para activar/desactivar la autenticación de doble paso
}
