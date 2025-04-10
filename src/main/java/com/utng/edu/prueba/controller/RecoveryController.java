package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.*;
import com.utng.edu.prueba.service.EmailRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/API/v1/auth/recuperacion")
public class RecoveryController {

    @Autowired
    private EmailRecoveryService recoveryService;

    @PostMapping("/iniciarRecuperacion")
    public ResponseEntity<RecoveryResponse> iniciarRecuperacion(
            @Valid @RequestBody RecoveryRequest request) {
        RecoveryResponse response = recoveryService.handleRecoveryRequest(request);
        return ResponseEntity.status(response.getCodigo() == 0 ? 200 : 400).body(response);
    }

    @PostMapping("/restablecer-contraseña")
    public ResponseEntity<RecoveryResponse> restablecerContraseña(
            @Valid @RequestBody ResetPasswordRequest request) {
        RecoveryResponse response = recoveryService.resetPassword(request);
        return ResponseEntity.status(response.getCodigo() == 0 ? 200 : 400).body(response);
    }
}
