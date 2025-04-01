package utng.edu.mx.prueba.service;

import utng.edu.mx.prueba.model.UsuariosRequest;

public interface cryptoService {
    boolean validacionCadenaOriginalAltaUser(UsuariosRequest usuariosRequest, String firma);
    String signMessage(String message, String keystorePath, String storepass, String alias, String keypass) throws Exception;
}
