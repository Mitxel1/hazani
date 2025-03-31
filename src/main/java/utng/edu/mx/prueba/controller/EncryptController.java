package utng.edu.mx.prueba.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utng.edu.mx.prueba.model.DecryptRequest;
import utng.edu.mx.prueba.model.DecryptResponse;
import utng.edu.mx.prueba.model.EncryptRequest;
import utng.edu.mx.prueba.model.EncryptResponse;
import utng.edu.mx.prueba.service.EncryptService;


@Component
@RestController
@RequestMapping(value = "/API/v1/ENCRYPT/encryptServicio")
public class EncryptController {
    @Autowired
    private EncryptService encryptService;

    @PostMapping(value = "encrypt", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncryptResponse> encryptaCadenaOriginal(@Valid @RequestBody EncryptRequest encryptRequest) {
        EncryptResponse encryptResponse = encryptService.encryptCadena(encryptRequest);

        if (encryptResponse != null && encryptResponse.getCodigo() == "0") {
            return new ResponseEntity<>(encryptResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(encryptResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "decrypt", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DecryptResponse> decryptCadenaOriginal(@Valid @RequestBody DecryptRequest decryptRequest) {
        DecryptResponse decryptResponse = encryptService.decryptCadena(decryptRequest);

        if (decryptResponse != null && decryptResponse.getCodigo() == 0) {
            return new ResponseEntity<>(decryptResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(decryptResponse, HttpStatus.BAD_REQUEST);
    }
}

