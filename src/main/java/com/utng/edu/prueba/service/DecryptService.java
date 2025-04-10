package com.utng.edu.prueba.service;


import com.utng.edu.prueba.model.DecryptResponse;
import com.utng.edu.prueba.model.DecryptRequest;

public interface DecryptService {
    DecryptResponse decryptCadena(DecryptRequest decryptRequest);
}
