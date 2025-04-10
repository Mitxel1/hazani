package com.utng.edu.prueba.service;

import com.utng.edu.prueba.model.EncryptRequest;
import com.utng.edu.prueba.model.EncryptResponse;

public interface EncryptService {
    EncryptResponse encryptCadena(EncryptRequest encryptRequest);
}
