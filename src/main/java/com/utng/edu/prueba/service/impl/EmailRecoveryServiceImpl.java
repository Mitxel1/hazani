// EmailRecoveryServiceImpl.java
package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.entity.empresa.PasswordResetToken;
import com.utng.edu.prueba.entity.empresa.Usuarios;
import com.utng.edu.prueba.model.*;
import com.utng.edu.prueba.repositories.empresa.PasswordResetTokenRepository;
import com.utng.edu.prueba.repositories.empresa.UsuariosRepositories;
import com.utng.edu.prueba.service.EmailRecoveryService;
import com.utng.edu.prueba.service.EmailServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EmailRecoveryServiceImpl implements EmailRecoveryService {

    @Autowired
    private UsuariosRepositories usuariosRepositories;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailServiceInterface emailService;

    @Value("${app.recovery.expiration-hours}")
    private int expirationHours;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public RecoveryResponse handleRecoveryRequest(RecoveryRequest request) {
        try {
            Optional<Usuarios> usuarioOpt = usuariosRepositories.findByCorreo(request.getEmail());

            // Si no se encuentra el usuario
            if (!usuarioOpt.isPresent()) {
                return new RecoveryResponse(0, "No se encontró una cuenta con ese correo electrónico intente con uno registrado");
            }

            Usuarios usuario = usuarioOpt.get();

            switch(request.getType()) {
                case USERNAME_RECOVERY:
                    return handleUsernameRecovery(usuario); // Este método debe retornar un mensaje de éxito adecuado
                case PASSWORD_RESET:
                    return handlePasswordReset(usuario); // Este método también debe retornar un mensaje de éxito adecuado
                default:
                    return new RecoveryResponse(1, "Tipo de recuperación no válido");
            }
        } catch (Exception e) {
            log.error("Error en recuperación: {}", e.getMessage());
            return new RecoveryResponse(1, "Error al procesar la solicitud, por favor intente nuevamente más tarde.");
        }
    }



    private RecoveryResponse handleUsernameRecovery(Usuarios usuario) {
        try {
            emailService.sendUsernameRecoveryEmail(
                    usuario.getCorreo(),
                    usuario.getUsername(),
                    usuario.getRol()
            );
            return new RecoveryResponse(0, "Se ha enviado la informacion del usuario al correo electrónico registrado");
        } catch (Exception e) {
            log.error("Error al enviar username por email: {}", e.getMessage());
            return new RecoveryResponse(1, "Error al enviar el correo electrónico");
        }
    }

    private RecoveryResponse handlePasswordReset(Usuarios usuario) {
        try {
            // Generar token
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(expirationHours);

            // Crear y guardar token
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUsuario(usuario);
            resetToken.setExpiryDate(expiryDate);
            tokenRepository.save(resetToken);

            // Construir enlace de reset
            String resetLink = frontendUrl + "/resetpassword?token=" + token;

            // Enviar correo
            emailService.sendPasswordResetEmail(usuario.getCorreo(), resetLink);

            return new RecoveryResponse(0, "Se ha enviado un enlace de recuperación a tu correo electrónico");
        } catch (Exception e) {
            log.error("Error al procesar reset de password: {}", e.getMessage());
            return new RecoveryResponse(1, "Error al procesar la solicitud");
        }
    }

    @Override
    public RecoveryResponse resetPassword(ResetPasswordRequest request) {
        try {
            // Validar que las contraseñas coincidan
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return new RecoveryResponse(1, "Las contraseñas no coinciden");
            }

            // Buscar token válido
            Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(request.getToken());

            if (!tokenOpt.isPresent()) {
                return new RecoveryResponse(1, "Token inválido o expirado");
            }

            PasswordResetToken token = tokenOpt.get();

            // Validar fecha de expiración
            if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(token);
                return new RecoveryResponse(1, "El enlace ha expirado");
            }

            // Actualizar contraseña
            Usuarios usuario = token.getUsuario();
            usuario.setPassword(request.getNewPassword()); // Considera cifrar la contraseña
            usuariosRepositories.save(usuario);

            // Eliminar token usado
            tokenRepository.delete(token);

            // Enviar confirmación por email
            emailService.sendPasswordChangedConfirmation(usuario.getCorreo());

            return new RecoveryResponse(0, "Contraseña actualizada exitosamente. Puedes iniciar sesión con la nueva contraseña.");

        } catch (Exception e) {
            log.error("Error al restablecer contraseña: {}", e.getMessage());
            return new RecoveryResponse(1, "Error al restablecer la contraseña");
        }
    }
}