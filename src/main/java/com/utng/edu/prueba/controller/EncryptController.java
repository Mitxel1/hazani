package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.EncryptRequest;
import com.utng.edu.prueba.model.EncryptResponse;
import com.utng.edu.prueba.service.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Component
@RestController
@RequestMapping(value = "/API/v1/ENCRYPT/encryptServicio")
public class EncryptController {

    @Autowired
    private EncryptService encryptService;

    @PostMapping(value = "encrypt", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncryptResponse> encryptCadenaOriginal(@Valid @RequestBody EncryptRequest encryptRequest) {
        EncryptResponse encryptResponse = encryptService.encryptCadena(encryptRequest);
        if (encryptResponse != null) {
            return new ResponseEntity<>(encryptResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
}
