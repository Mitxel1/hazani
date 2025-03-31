package utng.edu.mx.prueba.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class UserListResquest {
    private String username;
    @NotNull
    @Min(value = 0)
    private long pagina;

    @NotNull
    @Min(value = 1)
    private Integer tamañoPagina;

}
