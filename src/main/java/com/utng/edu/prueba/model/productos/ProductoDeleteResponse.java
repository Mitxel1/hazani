package com.utng.edu.prueba.model.productos;

import com.utng.edu.prueba.model.DefaultResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductoDeleteResponse extends DefaultResponse {
    private String nombre;
    private String mensaje;
}