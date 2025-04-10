package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordRequest {

    @NotBlank(message = "El token de recuperación es requerido")
    private String token;

    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String newPassword;

    @NotBlank(message = "La confirmación de contraseña es requerida")
    private String confirmPassword;

    // Método para validar coincidencia de contraseñas
    public boolean passwordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }

    // Método para limpiar espacios en blanco
    public void trimFields() {
        if (token != null) token = token.trim();
        if (newPassword != null) newPassword = newPassword.trim();
        if (confirmPassword != null) confirmPassword = confirmPassword.trim();
    }
}