package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultResponse {
    public static final int FIRMA_INVALIDA = 400;
    public static final String FIRMA_INVALIDA_MENSAJE = "No se aceptan nulos";

    private int codigo;
    private String mensaje;

    public void setEstatus(boolean b) {

    }
}