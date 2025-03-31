package utng.edu.mx.prueba.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Size(max = 100, message = "El email no debe exceder 100 caracteres")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe tener 10 dígitos numéricos")
    @Size(max = 15, message = "El teléfono no debe exceder 15 caracteres")
    private String telefono;

    @Max(value = 100, message = "La edad debe ser menor o igual a 100")
    private int edad;

    @Size(max = 15, message = "El sexo no debe exceder 15 caracteres")
    private String sexo;
}
