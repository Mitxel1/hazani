package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.model.EncryptRequest;
import com.utng.edu.prueba.model.EncryptResponse;
import com.utng.edu.prueba.service.EncryptService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class EncryptServiceImpl implements EncryptService {

    @Override
    public EncryptResponse encryptCadena(EncryptRequest encryptRequest) {
        try {

            String secretKey12 = "5TpM3x2021paL~nXD.^$";
            String salt = "S4l7202157pm3x<bbX&";


            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey12.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");


            byte[] iv = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);


            byte[] encryptedBytes = cipher.doFinal(encryptRequest.getEncrypt().getBytes(StandardCharsets.UTF_8));


            byte[] encryptedWithIv = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, encryptedWithIv, iv.length, encryptedBytes.length);

            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedWithIv);
            EncryptResponse encryptResponse = new EncryptResponse();
            encryptResponse.setEncryptedString(encryptedBase64);
            encryptResponse.setMessage("Encriptación exitosa");
            return encryptResponse;

        } catch (Exception e) {

            System.out.println("Error en la encriptación: " + e.getMessage());
            EncryptResponse encryptResponse = new EncryptResponse();
            encryptResponse.setMessage("Error durante la encriptación");
            return encryptResponse;
        }
    }
}
