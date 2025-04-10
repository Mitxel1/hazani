package com.utng.edu.prueba.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class DeleteUserRequest {
    @NotNull
    private String username;
    @NotNull
    private String firma;
}
