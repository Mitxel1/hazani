package com.utng.edu.prueba.model.productos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProductoListRequest {

    private String nombre;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String Fecha_creacion;

    @NotNull
    @Min(value = 0, message = "La página no puede ser negativa")
    private Long pagina;

    @NotNull
    @Min(value = 1, message = "El tamaño de página debe ser al menos 1")
    private Integer tamañoPagina;

    private String firma;

}