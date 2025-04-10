package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.DecryptRequest;
import com.utng.edu.prueba.model.DecryptResponse;
import com.utng.edu.prueba.service.DecryptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/API/v1/DECRYPT/decryptServicio")
public class DecryptController {

    private final DecryptService decryptService;

    // Inyección de dependencias por constructor (recomendado)
    public DecryptController(DecryptService decryptService) {
        this.decryptService = decryptService;
    }

    @PostMapping(value = "decrypt", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DecryptResponse> decryptCadenaOriginal(@Valid @RequestBody DecryptRequest decryptRequest) {
        try {
            DecryptResponse decryptResponse = decryptService.decryptCadena(decryptRequest);
            return ResponseEntity.ok(decryptResponse); // Devuelve directamente la respuesta con 200 OK
        } catch (Exception e) {
            // Manejo de errores generales
            DecryptResponse errorResponse = new DecryptResponse();
            errorResponse.setMessage("Error durante la desencriptación: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
