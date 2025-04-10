package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.*;
import com.utng.edu.prueba.service.CryptoServices;
import com.utng.edu.prueba.service.FirmaService;
import com.utng.edu.prueba.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/API/v1/ENCRYPT/usuarioServicio")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CryptoServices cryptoService;

    @Autowired
    private FirmaService firmaService;

    @PostMapping(value = "/crearUsuario")
    public ResponseEntity<UsuariosResponse> crearUsuario(@Valid @RequestBody UsuariosRequest usuariosRequest) {
        try {
            String firmaGenerada = firmaService.generarFirma(usuariosRequest);
            usuariosRequest.setFirma(firmaGenerada);

            if (!cryptoService.validiacionCadenaOriginalAltaUser(usuariosRequest)) {
                return ResponseEntity.badRequest().body(
                        new UsuariosResponse(1, "Error en la firma")
                );
            }

            UsuariosResponse response = usuarioService.crearUsuario(usuariosRequest);
            if (response.getCodigo() == 0) {
                response.setMensaje("Usuario creado exitosamente - Firma válida");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new UsuariosResponse(1, "Error interno: " + e.getMessage())
            );
        }
    }

    @PostMapping(value = "/listarUsuarios")
    public ResponseEntity<UserListResponse> listarUsuarios(@Valid @RequestBody UserListRequest userListRequest) {
        try {
            String firmaGenerada = firmaService.generarFirma(userListRequest);
            userListRequest.setFirma(firmaGenerada);

            if (!cryptoService.validacionCadenaOriginalListUser(userListRequest)) {
                return ResponseEntity.badRequest().body(
                        new UserListResponse(1, "Error en la firma")
                );
            }

            UserListResponse response = usuarioService.listarUsers(userListRequest);
            if (response != null && response.getCodigo() == 0) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/eliminarUsuario")
    public ResponseEntity<UsuariosResponse> eliminarUsuario(@Valid @RequestBody DeleteUserRequest deleteUserRequest) {
        try {
            String firmaGenerada = firmaService.generarFirma(deleteUserRequest);
            deleteUserRequest.setFirma(firmaGenerada);

            if (!cryptoService.validacionCadenaOriginalBajaUser(deleteUserRequest)) {
                return ResponseEntity.badRequest().body(
                        new UsuariosResponse(1, "Error en la firma")
                );
            }

            UsuariosResponse response = usuarioService.eliminarUsuario(deleteUserRequest);
            if (response.getCodigo() == 0) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new UsuariosResponse(1, "Error al eliminar usuario: " + e.getMessage())
            );
        }
    }

    @PutMapping(value = "/actualizarUsuario")
    public ResponseEntity<UsuariosResponse> actualizarUsuario(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            String firmaGenerada = firmaService.generarFirma(updateUserRequest);
            updateUserRequest.setFirma(firmaGenerada);

            if (!cryptoService.validacionCadenaOriginalActualizarUser(updateUserRequest)) {
                return ResponseEntity.badRequest().body(
                        new UsuariosResponse(1, "Error en la firma")
                );
            }

            UsuariosResponse response = usuarioService.updateUsuario(updateUserRequest);
            if (response.getCodigo() == 0) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new UsuariosResponse(1, "Error al actualizar usuario: " + e.getMessage())
            );
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String firmaGenerada = firmaService.generarFirma(loginRequest);
            loginRequest.setFirma(firmaGenerada);

            if (!cryptoService.validacionCadenaOriginalLogin(loginRequest)) {
                return ResponseEntity.badRequest().body(
                        new AuthResponse(1, "Error en la firma", false)
                );
            }

            AuthResponse response = usuarioService.login(loginRequest);
            if (response.getCodigo() == 0) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new AuthResponse(1, "Error en el login: " + e.getMessage(), false)
            );
        }
    }

    @PostMapping(value = "/verify-otp")
    public ResponseEntity<OtpResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest otpVerifyRequest) {
        try {
            // Validar firma primero
            String firmaGenerada = firmaService.generarFirma(otpVerifyRequest);
            otpVerifyRequest.setFirma(firmaGenerada);

            if (!cryptoService.validacionCadenaOriginalOtp(otpVerifyRequest)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new OtpResponse(1, "Error en la firma digital"));
            }

            OtpResponse response = usuarioService.verificarOtp(otpVerifyRequest);

            // Aquí está el cambio importante:
            if (response.getCodigo() == 0) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OtpResponse(1, "Error interno del servidor"));
        }
    }
}