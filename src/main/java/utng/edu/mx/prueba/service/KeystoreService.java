package utng.edu.mx.prueba.service;

import utng.edu.mx.prueba.model.KeyPairResponse;

public interface KeystoreService {
    KeyPairResponse generateKeyPair(String alias, String password) throws Exception;
}
