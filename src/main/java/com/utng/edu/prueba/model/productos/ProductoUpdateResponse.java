package com.utng.edu.prueba.model.productos;

import com.utng.edu.prueba.model.DefaultResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductoUpdateResponse extends DefaultResponse {
    private String nombre;
    private String descripcion;
    private Float precio;
    private Integer cantidad;
    private String categoria;
}