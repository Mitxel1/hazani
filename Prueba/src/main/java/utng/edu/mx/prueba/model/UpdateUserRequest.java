package utng.edu.mx.prueba.model;


import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UpdateUserRequest {
    @NotNull
    private String username;
    @NotNull
    private String newUsername; // Nuevo username
}
