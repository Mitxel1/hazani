package utng.edu.mx.prueba.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

@Slf4j
@Service
public class FirmaDigitalService {
    @Value("${app.cryto.validafirma.file}")
    private String keystoreFile;

    @Value("${app.cryto.validafirma.password}")
    private String keystorePassword;

    private static final String KEY_ALIAS = "producto-api";

    public String generarFirma(String data) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, keystorePassword.toCharArray());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());

        byte[] firmaBytes = signature.sign();
        return Base64.getEncoder().encodeToString(firmaBytes);
    }

//    public boolean validarFirma(String operacion, String firmaRecibida) {
//        try {
//            String firmaEsperada = generarFirma(operacion);
//            return firmaEsperada.equals(firmaRecibida);
//        } catch (Exception e) {
//            log.error("Error al validar firma: {}", e.getMessage(), e);
//            return false;
//        }
//    }

    public String generarFirmaConTiempo(String operacion, long tiempoValidezMs) throws Exception {
        // Tiempo actual
        long currentTime = System.currentTimeMillis();
        // Tiempo de expiración
        long expirationTime = currentTime + tiempoValidezMs;

        String datosAFirmar = operacion + "|" + expirationTime;

        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, keystorePassword.toCharArray());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(datosAFirmar.getBytes());

        byte[] firmaBytes = signature.sign();
        // Devolvemos la firma junto con el tiempo de expiración
        return Base64.getEncoder().encodeToString(firmaBytes) + "|" + expirationTime;
    }

    public boolean validarFirmaConTiempo(String operacion, String firmaCompleta) {
        try {
            String[] partes = firmaCompleta.split("\\|");
            if (partes.length != 2) {
                return false;
            }

            String firma = partes[0];
            long tiempoExpiracion = Long.parseLong(partes[1]);

            // Verificar si la firma ha expirado
            if (System.currentTimeMillis() > tiempoExpiracion) {
                log.warn("Firma expirada para operación: {}", operacion);
                return false;
            }

            // Recrear los datos originales
            String datosOriginales = operacion + "|" + tiempoExpiracion;

            // Verificar la firma
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                keyStore.load(fis, keystorePassword.toCharArray());
            }

            PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(datosOriginales.getBytes());

            return signature.verify(Base64.getDecoder().decode(firma));
        } catch (Exception e) {
            log.error("Error al validar firma con tiempo: {}", e.getMessage(), e);
            return false;
        }
    }
}
