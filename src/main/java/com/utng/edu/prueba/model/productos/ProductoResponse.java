package com.utng.edu.prueba.model.productos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utng.edu.prueba.model.DefaultResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProductoResponse extends DefaultResponse {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidad;
    private String categoria;
    private String firma;


    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    public ProductoResponse(int i, String firmaInválida) {
        super();
    }

    public ProductoResponse() {

    }
}
