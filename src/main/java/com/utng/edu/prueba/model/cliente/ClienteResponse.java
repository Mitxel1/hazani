package com.utng.edu.prueba.model.cliente;

import com.utng.edu.prueba.entity.empresa.Cliente;
import com.utng.edu.prueba.model.DefaultResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ClienteResponse extends DefaultResponse {
    private String nombre;
    private String email;
    private String telefono;
    private String edad;
    private String sexo;
    private LocalDateTime fecha_creacion;

}
