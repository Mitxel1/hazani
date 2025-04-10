package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpResponse {
    private int codigo;
    private String mensaje;

    // Constructor vacío
    public OtpResponse() {
    }

    // Constructor con parámetros
    public OtpResponse(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    // Método para verificar si la respuesta es exitosa
    public boolean isSuccess() {
        return this.codigo == 0;
    }

    @Override
    public String toString() {
        return "OtpResponse{" +
                "codigo=" + codigo +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}