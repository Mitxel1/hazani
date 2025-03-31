package utng.edu.mx.prueba.model;


import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UpdateUserRequest {
    private Integer id;
    @NotNull
    private String username;
    private String password;
    private Boolean estatus;
}
