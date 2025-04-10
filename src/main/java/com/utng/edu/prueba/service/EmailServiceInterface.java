package com.utng.edu.prueba.service;

public interface EmailServiceInterface {
    void sendOtpEmail(String toEmail, String otp);
    void sendUsernameRecoveryEmail(String email, String username, String rol);
    void sendPasswordChangedConfirmation(String email);
    void sendPasswordResetEmail(String email, String resetLink);
}
