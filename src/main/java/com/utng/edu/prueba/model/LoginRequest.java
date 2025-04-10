package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;    // Nombre de usuario o email
    private String password;
    private String firma;// Contraseña sin encriptar

    // Constructor vacío (necesario para Spring)
    public LoginRequest() {}

    // Constructor con parámetros
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Método para validación básica
    public boolean isValid() {
        return this.username != null && !this.username.isEmpty() &&
                this.password != null && !this.password.isEmpty();
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}