package utng.edu.mx.prueba.service;

import utng.edu.mx.prueba.model.DecryptRequest;
import utng.edu.mx.prueba.model.DecryptResponse;
import utng.edu.mx.prueba.model.EncryptRequest;
import utng.edu.mx.prueba.model.EncryptResponse;

public interface EncryptService {
    EncryptResponse encryptCadena(EncryptRequest encryptRequest);
    DecryptResponse decryptCadena(DecryptRequest decryptRequest);
}
