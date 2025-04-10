package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.model.*;
import com.utng.edu.prueba.service.FirmaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
@Service
public class FirmaServiceImpl implements FirmaService {

    @Value("${app.crypto.validafirma.file}")
    private String privateKeyFile;

    @Value("${app.crypto.validafirma.password}")
    private String privateKeyPassword;

    private static final String ALIAS = "service";

    @Override
    public String generarFirma(Object request) {
        try {
            String cadenaOriginal = generarCadenaOriginal(request);
            return firmarCadena(cadenaOriginal);
        } catch (Exception e) {
            log.error("Error al generar firma: {}", e.getMessage());
            throw new RuntimeException("Error al generar firma digital", e);
        }
    }

    private String generarCadenaOriginal(Object request) {
        if (request instanceof UsuariosRequest usuariosRequest) {
            return new StringBuilder("||")
                    .append(usuariosRequest.getUsername()).append("|")
                    .append(usuariosRequest.getPassword()).append("|")
                    .append(usuariosRequest.isEstatus()).append("|")
                    .append(usuariosRequest.getCorreo()).append("|")
                    .append(usuariosRequest.getRol()).append("||")
                    .toString();

        } else if (request instanceof UpdateUserRequest updateUserRequest) {
            return new StringBuilder("||")
                    .append(updateUserRequest.getNombre()).append("|")
                    .append(updateUserRequest.getNombreBuscar()).append("|")
                    .append(updateUserRequest.getPassword()).append("|")
                    .append(updateUserRequest.getEstatus()).append("|")
                    .append(updateUserRequest.getCorreo()).append("|")
                    .append(updateUserRequest.getRol()).append("||")
                    .toString();

        } else if (request instanceof UserListRequest userListRequest) {
            return new StringBuilder("||")
                    .append(userListRequest.getUsername()).append("|")
                    .append(userListRequest.getPagina()).append("|")
                    .append(userListRequest.getTamañoPagina()).append("||")
                    .toString();

        } else if (request instanceof DeleteUserRequest deleteUserRequest) {
            return "||" + deleteUserRequest.getUsername() + "||";

        } else if (request instanceof LoginRequest loginRequest) {
            return "||" + loginRequest.getUsername() + "|" + loginRequest.getPassword() + "||";

        } else if (request instanceof OtpVerifyRequest otpRequest) {
            return "||" + otpRequest.getUsername() + "|" + otpRequest.getOtp() + "||";

        } else {
            throw new IllegalArgumentException("Tipo de solicitud no soportado para firma digital");
        }
    }

    private String firmarCadena(String cadenaOriginal) throws Exception {
        PrivateKey privateKey = obtenerClavePrivada();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(cadenaOriginal.getBytes(StandardCharsets.UTF_8));

        byte[] firmaBytes = signature.sign();
        return Base64.getEncoder().encodeToString(firmaBytes);
    }

    private PrivateKey obtenerClavePrivada() throws Exception {
        try (FileInputStream fis = new FileInputStream(privateKeyFile)) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(fis, privateKeyPassword.toCharArray());

            Key key = keyStore.getKey(ALIAS, privateKeyPassword.toCharArray());
            if (!(key instanceof PrivateKey)) {
                throw new KeyStoreException("La clave obtenida no es una clave privada");
            }
            return (PrivateKey) key;
        }
    }
}