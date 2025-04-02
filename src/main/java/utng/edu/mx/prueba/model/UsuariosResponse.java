package utng.edu.mx.prueba.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class UsuariosResponse extends DefaulResponse {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private Boolean estatus;
    private String role;
    private String codigo;
    private String mensaje;
}
