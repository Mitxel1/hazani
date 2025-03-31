package utng.edu.mx.prueba.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProductoResponse extends DefaulResponse{

        private Long id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Integer cantidad;
        private String categoria;
        private LocalDateTime fechaCreacion;

    public ProductoResponse() {
        super();  // Si DefaulResponse tiene lógica de inicialización, llama al constructor padre
    }

}
