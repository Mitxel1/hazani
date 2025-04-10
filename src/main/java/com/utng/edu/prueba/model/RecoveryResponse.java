// RecoveryResponse.java
package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryResponse {
    private int codigo;
    private String mensaje;

    public RecoveryResponse(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }
}