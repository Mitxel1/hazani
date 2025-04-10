package com.utng.edu.prueba.model;

import com.utng.edu.prueba.entity.empresa.Usuarios;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserListResponse extends DefaultResponse {
    private Integer totalPaginas;
    private Long totalElements;
    private List<Usuarios> usuarios;

    public UserListResponse(int i, String errorEnLaFirma) {

    }

    public void setTotalElementos(long totalElements) {
        this.totalElements = totalElements; // Se actualiza este método para que funcione correctamente.
    }
}
