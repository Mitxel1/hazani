package utng.edu.mx.prueba.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utng.edu.mx.prueba.entity.empresa.Usuarios;
import java.util.List;

@Getter
@Setter
@ToString
public class UserListResponse extends DefaulResponse{
    private List<UsuariosResponse> usuarios;
    private Long totalElementos;
    private Integer totalPaginas;
    private Integer paginaActual;
    private String codigo;
    private String mensaje;
}
