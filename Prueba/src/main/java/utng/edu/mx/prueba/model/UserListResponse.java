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
    private Integer totalPaginas;
    private Long totalElementos;
    private List<Usuarios> usuarios;
}
