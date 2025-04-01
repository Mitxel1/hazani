package utng.edu.mx.prueba.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utng.edu.mx.prueba.model.UsuariosRequest;
import utng.edu.mx.prueba.service.cryptoService;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

@Component(value = "cryptoServices")
@Slf4j
public class CryptoServicesImpl implements cryptoService {

    @Value("${app.cryto.validafirma.file}")
    private String publicKeyFile;

    @Value("${app.cryto.validafirma.password}")
    private String publicKeyPassword;

    public boolean validacionCadenaOriginalAltaUser(UsuariosRequest usuariosRequest, String firma) {
        StringBuilder cadenaOriginal = new StringBuilder();
        cadenaOriginal.append("||")
                .append(usuariosRequest.getUsername()).append("||")
                .append(usuariosRequest.getPassword()).append("||")
                .append(usuariosRequest.getEstatus()).append("||");

        return validaCadena(cadenaOriginal.toString(), firma, "servicios");
    }

    public boolean validaCadena(String cadenaOriginal, String firma, String empresa) {  // Ahora es público
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            PublicKey publicKey = obtieneLlavePublica(empresa);

            if (publicKey == null) {
                log.warn("No se encontró llave para {}", empresa);
                return false;
            }

            sign.initVerify(publicKey);
            sign.update(cadenaOriginal.getBytes(StandardCharsets.UTF_8));

            boolean isValid = sign.verify(Base64.getDecoder().decode(firma));
            log.info("Validación de firma para empresa {}: {}", empresa, isValid);
            return isValid;

        } catch (Exception e) {
            log.error("Error en validaCadena: {}", e.getMessage());
            return false;
        }
    }

    private PublicKey obtieneLlavePublica(String empresa) {
        PublicKey publicKey = null;
        try (FileInputStream fileInputStream = new FileInputStream(publicKeyFile)) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(fileInputStream, publicKeyPassword.toCharArray());

            Certificate certificate = keyStore.getCertificate(empresa);
            if (certificate == null) {
                certificate = keyStore.getCertificate("geistor-key"); // Ajustar alias si es necesario
            }

            if (certificate != null) {
                publicKey = certificate.getPublicKey();
            }

        } catch (Exception e) {
            log.error("Error en obtieneLlavePublica: {}", e.getMessage());
        }
        return publicKey;
    }

    // Método para firmar el mensaje
    public String signMessage(String message, String keystorePath, String storepass, String alias, String keypass) throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        try (FileInputStream keystoreStream = new FileInputStream(keystorePath)) {
            keystore.load(keystoreStream, storepass.toCharArray());
        }

        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keypass.toCharArray());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));

        byte[] signedMessage = signature.sign();
        return Base64.getEncoder().encodeToString(signedMessage);
    }
}
