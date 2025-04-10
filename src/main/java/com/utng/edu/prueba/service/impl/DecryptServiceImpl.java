package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.model.DecryptRequest;
import com.utng.edu.prueba.model.DecryptResponse;
import com.utng.edu.prueba.service.DecryptService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class DecryptServiceImpl implements DecryptService {

    private static final String SECRET_KEY = "5TpM3x2021paL~nXD.^$";
    private static final String SALT = "S4l7202157pm3x<bbX&";
    private static final int IV_LENGTH = 16;

    @Override
    public DecryptResponse decryptCadena(DecryptRequest decryptRequest) {
        DecryptResponse decryptResponse = new DecryptResponse();

        try {

            if (decryptRequest == null || decryptRequest.getDecrypt() == null || decryptRequest.getDecrypt().isEmpty()) {
                decryptResponse.setMessage("Error: La cadena a desencriptar no puede ser nula o vacía.");
                return decryptResponse;
            }

            // Decodificar la cadena base64
            byte[] encryptedWithIv = Base64.getDecoder().decode(decryptRequest.getDecrypt());

            // Extraer IV
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(encryptedWithIv, 0, iv, 0, IV_LENGTH);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            // Extraer datos encriptados
            byte[] encryptedBytes = new byte[encryptedWithIv.length - IV_LENGTH];
            System.arraycopy(encryptedWithIv, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

            // Generar clave secreta
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(StandardCharsets.UTF_8), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Configurar Cipher para desencriptar
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            // Desencriptar
            String decryptedString = new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);

            // Preparar respuesta
            decryptResponse.setDecryptedString(decryptedString);
            decryptResponse.setMessage("Desencriptación exitosa");
            return decryptResponse;

        } catch (Exception e) {
            System.out.println("Error en la desencriptación: " + e.getMessage());
            decryptResponse.setMessage("Error durante la desencriptación: " + e.getMessage());
            return decryptResponse;
        }
    }
}
