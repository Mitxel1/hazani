package utng.edu.mx.prueba.model;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UsuariosRequest {

    @NotNull(message = "el Campo no puede ser null")
    private String username;

    @NotNull(message = "el Campo no puede ser null")
    private String password;

    @NotNull(message = "el Campo no puede ser null")
    private String estatus;

    @NotNull(message = "el Campo no puede ser null")
    private String role;

}
