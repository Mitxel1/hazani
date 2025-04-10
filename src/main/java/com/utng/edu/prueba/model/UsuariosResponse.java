package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuariosResponse {
    private Integer codigo;
    private String mensaje;
    private String username;
    private String correo;
    private String rol;
    private boolean estatus;
    private boolean doblePasoActivado; // Indicamos si la autenticación de doble paso está activada

    public UsuariosResponse(int i, String errorEnLaFirma) {
    }

    public UsuariosResponse() {

    }
}
