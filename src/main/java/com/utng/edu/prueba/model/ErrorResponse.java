package com.utng.edu.prueba.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private int codigo;
    private String mensaje;
    private String operacion;

    public ErrorResponse(int codigo, String mensaje, String operacion) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.operacion = operacion;
    }
}