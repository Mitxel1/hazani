package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {
    private String username;  // Necesario para identificar al usuario
    private String otp;       // Código OTP ingresado por el usuario
    private String firma;     // Campo adicional si necesitas firma digital

    // Constructor vacío
    public OtpVerifyRequest() {
    }

    // Constructor con parámetros
    public OtpVerifyRequest(String username, String otp, String firma) {
        this.username = username;
        this.otp = otp;
        this.firma = firma;
    }

    // Método getOtp corregido (no debe devolver string vacío)
    public String getOtp() {
        return this.otp;
    }

    // Métodos adicionales que podrías necesitar
    public boolean isValid() {
        return this.username != null && !this.username.isEmpty() &&
                this.otp != null && this.otp.length() == 6;
    }

    @Override
    public String toString() {
        return "OtpVerifyRequest{" +
                "username='" + username + '\'' +
                ", otp='[PROTECTED]'" +
                ", firma='" + firma + '\'' +
                '}';
    }
}