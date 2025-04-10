package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UserListRequest {
    private String username;

    @NotNull
    @Min(value = 0)
    private Long pagina;

    @NotNull
    @Min(value = 1)
    private Integer tamañoPagina;

    @NotNull
    private String firma;
}
