package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private int codigo;         // Código de estado (0 = éxito, otros = error)
    private String mensaje;     // Mensaje descriptivo
    private String token;       // Token JWT (si aplica)
    private boolean requiereOtp; // Indica si se necesita OTP
    private String username;    // Nombre de usuario autenticado
    private String rol;         // Rol del usuario (opcional)

    public AuthResponse(int i, String s, boolean b) {
    }

    public AuthResponse() {

    }

    // Constructor para éxito sin OTP
    public static AuthResponse success(String mensaje, String token, String username, String rol) {
        AuthResponse response = new AuthResponse();
        response.codigo = 0;
        response.mensaje = mensaje;
        response.token = token;
        response.requiereOtp = false;
        response.username = username;
        response.rol = rol;
        return response;
    }

    // Constructor para éxito con OTP requerido
    public static AuthResponse otpRequired(String mensaje, String username) {
        AuthResponse response = new AuthResponse();
        response.codigo = 0;
        response.mensaje = mensaje;
        response.requiereOtp = true;
        response.username = username;
        return response;
    }

    // Constructor para error
    public static AuthResponse error(int codigo, String mensaje) {
        AuthResponse response = new AuthResponse();
        response.codigo = codigo;
        response.mensaje = mensaje;
        return response;
    }

    // Método para verificar si fue exitoso
    public boolean isSuccess() {
        return this.codigo == 0;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "codigo=" + codigo +
                ", mensaje='" + mensaje + '\'' +
                ", token='" + (token != null ? "[PROTECTED]" : "null") + '\'' +
                ", requiereOtp=" + requiereOtp +
                ", username='" + username + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}