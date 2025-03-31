package utng.edu.mx.prueba.model;


import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResquest {
    private String username;
    @NotNull
    @Min(value = 0)
    private long pagina;

    @NotNull
    @Min(value = 1)
    private Integer tamañoPagina;

}
