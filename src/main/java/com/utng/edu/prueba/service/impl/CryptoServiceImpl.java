package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.model.*;
import com.utng.edu.prueba.service.CryptoServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

@Slf4j
@Service
public class CryptoServiceImpl implements CryptoServices {

    @Value("${app.crypto.validafirma.file}")
    private String publicKeyFile;

    @Value("${app.crypto.validafirma.password}")
    private String publicKeyPassword;

    // Método principal para validar cadenas
    private boolean validaCadena(String cadenaOriginal, String firma, String empresa) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            PublicKey publicKey = obtieneLlavePublica(empresa);

            if (publicKey == null) {
                log.warn("No se encontró la llave pública para {}", empresa);
                return false;
            }

            sign.initVerify(publicKey);
            sign.update(cadenaOriginal.getBytes(StandardCharsets.UTF_8));

            byte[] firmaBytes = Base64.getDecoder().decode(firma.getBytes(StandardCharsets.UTF_8));
            if (sign.verify(firmaBytes)) {
                log.info("Firma válida para empresa: {}", empresa);
                return true;
            }
            log.warn("Firma inválida para empresa: {}", empresa);
            return false;

        } catch (Exception e) {
            log.error("Error en validaCadena: {}", e.getMessage());
            return false;
        }
    }

    // Obtiene la llave pública del keystore
    private PublicKey obtieneLlavePublica(String empresa) {
        try (FileInputStream fileInputStream = new FileInputStream(publicKeyFile)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fileInputStream, publicKeyPassword.toCharArray());

            Certificate certificate = keyStore.getCertificate(empresa);
            if (certificate == null) {
                certificate = keyStore.getCertificate("service");
            }

            if (certificate == null) {
                log.error("No se encontró el certificado para {}", empresa);
                return null;
            }

            return certificate.getPublicKey();

        } catch (Exception e) {
            log.error("Error al obtener llave pública: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean validiacionCadenaOriginalAltaUser(UsuariosRequest usuariosRequest) {
        StringBuilder cadenaOriginal = new StringBuilder("||")
                .append(usuariosRequest.getUsername()).append("|")
                .append(usuariosRequest.getPassword()).append("|")
                .append(usuariosRequest.isEstatus()).append("|")
                .append(usuariosRequest.getCorreo()).append("|")
                .append(usuariosRequest.getRol()).append("||");

        return validaCadena(cadenaOriginal.toString(), usuariosRequest.getFirma(), "service");
    }

    @Override
    public Boolean validacionCadenaOriginalActualizarUser(UpdateUserRequest updateUserRequest) {
        StringBuilder cadenaOriginal = new StringBuilder("||")
                .append(updateUserRequest.getNombre()).append("|")
                .append(updateUserRequest.getNombreBuscar()).append("|")
                .append(updateUserRequest.getPassword()).append("|")
                .append(updateUserRequest.getEstatus()).append("|")
                .append(updateUserRequest.getCorreo()).append("|")
                .append(updateUserRequest.getRol()).append("||");

        return validaCadena(cadenaOriginal.toString(), updateUserRequest.getFirma(), "service");
    }

    @Override
    public Boolean validacionCadenaOriginalBajaUser(DeleteUserRequest deleteUserRequest) {
        String cadenaOriginal = "||" + deleteUserRequest.getUsername() + "||";
        return validaCadena(cadenaOriginal, deleteUserRequest.getFirma(), "service");
    }

    @Override
    public Boolean validacionCadenaOriginalListUser(UserListRequest userListRequest) {
        StringBuilder cadenaOriginal = new StringBuilder("||")
                .append(userListRequest.getUsername()).append("|")
                .append(userListRequest.getPagina()).append("|")
                .append(userListRequest.getTamañoPagina()).append("||");

        return validaCadena(cadenaOriginal.toString(), userListRequest.getFirma(), "service");
    }

    @Override
    public Boolean validacionCadenaOriginalLogin(LoginRequest loginRequest) {
        String cadenaOriginal = "||" + loginRequest.getUsername() + "|" +
                loginRequest.getPassword() + "||";
        return validaCadena(cadenaOriginal, loginRequest.getFirma(), "service");
    }

    @Override
    public Boolean validacionCadenaOriginalOtp(OtpVerifyRequest otpRequest) {
        String cadenaOriginal = "||" + otpRequest.getUsername() + "|" +
                otpRequest.getOtp() + "||";
        return validaCadena(cadenaOriginal, otpRequest.getFirma(), "service");
    }
}