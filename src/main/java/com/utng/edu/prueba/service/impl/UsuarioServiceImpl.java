package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.entity.empresa.Otp;
import com.utng.edu.prueba.entity.empresa.Usuarios;
import com.utng.edu.prueba.model.*;
import com.utng.edu.prueba.repositories.empresa.UsuariosRepositories;
import com.utng.edu.prueba.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuariosRepositories usuariosRepositories;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Override
    public UsuariosResponse crearUsuario(UsuariosRequest usuariosRequest) {
        UsuariosResponse response = new UsuariosResponse();

        try {
            // Verificar si el usuario ya existe
            if (usuariosRepositories.findByUsername(usuariosRequest.getUsername()).isPresent()) {
                response.setCodigo(1);
                response.setMensaje("El nombre de usuario ya existe");
                return response;
            }

            // Validar campos obligatorios
            if (usuariosRequest.getPassword() == null || usuariosRequest.getPassword().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }

            // Crear nuevo usuario
            Usuarios user = new Usuarios();
            user.setUsername(usuariosRequest.getUsername());
            user.setPassword(usuariosRequest.getPassword());
            user.setCorreo(usuariosRequest.getCorreo());
            user.setRol(usuariosRequest.getRol());
            user.setEstatus(true);
            user.setDoblePasoActivado(usuariosRequest.isDoblePasoActivado());

            // Guardar usuario
            usuariosRepositories.save(user);

            // Si el doble paso está activado, enviar OTP de verificación inicial
            if (usuariosRequest.isDoblePasoActivado() && usuariosRequest.getCorreo() != null) {
                String otp = generarOtp();
                otpService.storeOtp(user.getUsername(), otp);
                emailService.sendOtpEmail(user.getCorreo(), otp);
            }

            // Preparar respuesta
            response.setCodigo(0);
            response.setMensaje("Usuario creado exitosamente");
            BeanUtils.copyProperties(user, response);

        } catch (Exception e) {
            log.error("Error al crear usuario: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al crear usuario: " + e.getMessage());
        }

        return response;
    }

    @Override
    public UserListResponse listarUsers(UserListRequest usuarioRequest) {
        // Implementación para listar usuarios (puedes completar según tus necesidades)
        return null;
    }

    @Override
    public UsuariosResponse updateUsuario(UpdateUserRequest updateUserRequest) {
        UsuariosResponse response = new UsuariosResponse();

        try {
            Optional<Usuarios> usuarioOpt = usuariosRepositories.findByUsername(updateUserRequest.getNombreBuscar());

            if (!usuarioOpt.isPresent()) {
                response.setCodigo(1);
                response.setMensaje("Usuario no encontrado");
                return response;
            }

            Usuarios user = usuarioOpt.get();

            if (updateUserRequest.getRol() != null) {
                user.setRol(updateUserRequest.getRol());
            }
            if (updateUserRequest.getCorreo() != null) {
                user.setCorreo(updateUserRequest.getCorreo());
            }

            // Manejo de doble paso
            if (updateUserRequest.isDoblePasoActivado() && !user.isDoblePasoActivado()) {
                // Si se está activando el doble paso, enviar OTP de verificación
                String otp = generarOtp();
                otpService.storeOtp(user.getUsername(), otp);
                emailService.sendOtpEmail(user.getCorreo(), otp);
            }
            user.setDoblePasoActivado(updateUserRequest.isDoblePasoActivado());

            // Guardar cambios
            usuariosRepositories.save(user);

            response.setCodigo(0);
            response.setMensaje("Usuario actualizado exitosamente");
            BeanUtils.copyProperties(user, response);

        } catch (Exception e) {
            log.error("Error al actualizar usuario: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al actualizar usuario: " + e.getMessage());
        }

        return response;
    }

    @Override
    public UsuariosResponse eliminarUsuario(DeleteUserRequest deleteUserRequest) {
        UsuariosResponse response = new UsuariosResponse();

        try {
            Optional<Usuarios> usuarioOpt = usuariosRepositories.findByUsername(deleteUserRequest.getUsername());

            if (!usuarioOpt.isPresent()) {
                response.setCodigo(1);
                response.setMensaje("Usuario no encontrado");
                return response;
            }

            // Eliminar cualquier OTP asociado primero
            otpService.deleteByUsername(deleteUserRequest.getUsername());

            // Eliminar usuario
            usuariosRepositories.delete(usuarioOpt.get());

            response.setCodigo(0);
            response.setMensaje("Usuario eliminado exitosamente");

        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error al eliminar usuario: " + e.getMessage());
        }

        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AuthResponse response = new AuthResponse();

        try {
            Optional<Usuarios> usuarioOpt = usuariosRepositories.findByUsername(request.getUsername());

            if (!usuarioOpt.isPresent()) {
                response.setCodigo(1);
                response.setMensaje("Usuario o contraseña incorrectos");
                return response;
            }

            Usuarios usuario = usuarioOpt.get();

            // Verificar contraseña
            if (!usuario.getPassword().equals(request.getPassword())) {
                response.setCodigo(1);
                response.setMensaje("Usuario o contraseña incorrectos");
                return response;
            }

            // Verificar estatus
            if (!usuario.isEstatus()) {
                response.setCodigo(1);
                response.setMensaje("Usuario inactivo");
                return response;
            }

            // Manejo de doble factor
            if (usuario.isDoblePasoActivado()) {
                String otp = generarOtp();
                otpService.storeOtp(usuario.getUsername(), otp);

                if (usuario.getCorreo() != null) {
                    emailService.sendOtpEmail(usuario.getCorreo(), otp);
                    response.setMensaje("Hemos enviado un código OTP a tu correo electrónico");
                } else {
                    response.setCodigo(1);
                    response.setMensaje("Autenticación de doble factor activada pero no hay método de contacto configurado");
                    return response;
                }

                response.setCodigo(0);
                response.setRequiereOtp(true);
                response.setUsername(usuario.getUsername());
            } else {
                response.setCodigo(0);
                response.setMensaje("Autenticación exitosa");
                response.setRequiereOtp(false);
                // Aquí podrías incluir información adicional del usuario o tokens JWT
            }

        } catch (Exception e) {
            log.error("Error en el proceso de login: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error en el proceso de autenticación");
        }

        return response;
    }

    @Override
    public OtpResponse verificarOtp(OtpVerifyRequest otpVerifyRequest) {
        OtpResponse response = new OtpResponse();

        try {
            // 1. Verificar si existe un OTP para este usuario
            Optional<Otp> otpOpt = otpService.getOtp(otpVerifyRequest.getUsername());
            if (!otpOpt.isPresent()) {
                response.setCodigo(1);
                response.setMensaje("No se encontró solicitud de OTP para este usuario");
                return response;
            }

            Otp otp = otpOpt.get();

            // 2. Verificar si el OTP ha expirado
            if (otpService.isOtpExpired(otp)) {
                otpService.deleteOtp(otp); // Limpiar OTP expirado
                response.setCodigo(1);
                response.setMensaje("El código OTP ha expirado. Por favor solicite uno nuevo.");
                return response;
            }

            // 3. Verificar si el OTP coincide
            if (!otp.getOtp().equals(otpVerifyRequest.getOtp())) {
                response.setCodigo(1);
                response.setMensaje("Código OTP incorrecto. Intente nuevamente.");
                return response;
            }

            // 4. Si todo es correcto
            otpService.deleteOtp(otp); // Limpiar OTP usado
            response.setCodigo(0);
            response.setMensaje("OTP verificado correctamente");
            return response;

        } catch (Exception e) {
            log.error("Error al verificar OTP: {}", e.getMessage());
            response.setCodigo(1);
            response.setMensaje("Error interno al verificar OTP");
            return response;
        }
    }

    // Método auxiliar para generar OTP
    private String generarOtp() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}