package utng.edu.mx.prueba.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private int edad;
    private String sexo;
    private int codigo;
    private String mensaje;

}
