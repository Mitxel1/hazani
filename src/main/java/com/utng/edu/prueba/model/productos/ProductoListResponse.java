package com.utng.edu.prueba.model.productos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utng.edu.prueba.entity.empresa.Productos;
import com.utng.edu.prueba.model.DefaultResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductoListResponse extends DefaultResponse {
    private Integer totalPaginas; // Total de páginas disponibles
    private Long totalElementos; // Total de elementos encontrados
    private List<ProductoResponse> productos; // Lista de productos


    public ProductoListResponse(int i, String firmaInválida) {
        super();
    }

    public ProductoListResponse() {

    }

    public void setTotalElements(long totalElements) {
    }

    public void setProductos(List<Productos> content) {
    }
}