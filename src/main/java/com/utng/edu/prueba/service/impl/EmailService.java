package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.service.EmailServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.sender-name:TuApp}")
    private String senderName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía código OTP por email
     */
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = createBaseMessage();
        message.setTo(toEmail);
        message.setSubject("Código de Verificación");
        message.setText(String.format(
                "Tu código de verificación es: %s\n\n" +
                        "Este código expirará en 5 minutos.\n\n" +
                        "Si no solicitaste este código, por favor ignora este mensaje.",
                otp
        ));
        mailSender.send(message);
    }

    /**
     * Envía recuperación de nombre de usuario
     */
    public void sendUsernameRecoveryEmail(String email, String username, String rol) {
        SimpleMailMessage message = createBaseMessage();
        message.setTo(email);
        message.setSubject("Recuperación de Credenciales");
        message.setText(String.format(
                "Estimado usuario,\n\n" +
                        "Tus datos de acceso son:\n\n" +
                        "Nombre de usuario: %s\n" +
                        "Rol: %s\n\n" +
                        "Si no solicitaste esta información, por favor contacta al soporte.\n\n" +
                        "Atentamente,\nEl equipo de %s",
                username, rol, senderName
        ));
        mailSender.send(message);
    }

    /**
     * Envía confirmación de cambio de contraseña
     */
    public void sendPasswordChangedConfirmation(String email) {
        SimpleMailMessage message = createBaseMessage();
        message.setTo(email);
        message.setSubject("Contraseña Actualizada");
        message.setText(String.format(
                "Estimado usuario,\n\n" +
                        "Tu contraseña ha sido cambiada exitosamente.\n\n" +
                        "Si no realizaste este cambio, por favor contacta al soporte inmediatamente.\n\n" +
                        "Atentamente,\nEl equipo de seguridad de %s",
                senderName
        ));
        mailSender.send(message);
    }

    /**
     * Envía enlace para restablecer contraseña
     */
    public void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = createBaseMessage();
        message.setTo(email);
        message.setSubject("Restablecer Contraseña");
        message.setText(String.format(
                "Hola,\n\n" +
                        "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n\n" +
                        "%s\n\n" +
                        "Este enlace expirará en 24 horas.\n\n" +
                        "Si no solicitaste este cambio, ignora este mensaje.\n\n" +
                        "Atentamente,\nEl equipo de %s",
                resetLink, senderName
        ));
        mailSender.send(message);
    }

    /**
     * Crea mensaje base con configuración común
     */
    private SimpleMailMessage createBaseMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(String.format("%s <%s>", senderName, fromEmail));
        return message;
    }
}