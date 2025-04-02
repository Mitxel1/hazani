package utng.edu.mx.prueba.model;

import javax.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Size(max = 100, message = "El email no debe exceder 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "el Campo no puede ser null")
    private String estatus;

    @NotBlank(message = "El rol es obligatorio")
    private String role;

}
