package utng.edu.mx.prueba.service.impl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;
import utng.edu.mx.prueba.model.DecryptRequest;
import utng.edu.mx.prueba.model.DecryptResponse;
import utng.edu.mx.prueba.model.EncryptRequest;
import utng.edu.mx.prueba.model.EncryptResponse;
import utng.edu.mx.prueba.service.EncryptService;

@Service
public class EncryptServiceimpl implements EncryptService {
    @Override
    public EncryptResponse encryptCadena(EncryptRequest encryptRequest) {
        EncryptResponse encryptResponse = new EncryptResponse();
        try {
            String cadenaSinEspacios = encryptRequest.getEncrypt().trim();

            String secretKey12 = "5TpM3x2021paL~nX!D.^$";
            String salt = "S4l7202157pm3x<bbx&";
            byte[] iv = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey12.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            String cadena = Base64.getEncoder().encodeToString(cipher.doFinal(cadenaSinEspacios.getBytes("UTF-8")));
            if (cadena != null && cadena.length() >= 16) {
                encryptResponse.setMensaje("Exito");
                encryptResponse.setCodigo("0");
                encryptResponse.setEncrypt(cadena);
                return encryptResponse;
            }

        } catch (Exception e) {
            encryptResponse.setCodigo("1");
            encryptResponse.setMensaje(e.getMessage());
            System.out.println("La encriptación no se completó debido a: " + e.getMessage());
            return encryptResponse;
        }
        return encryptResponse;
    }

    @Override
    public DecryptResponse decryptCadena(DecryptRequest decryptRequest) {
        DecryptResponse decryptResponse = new DecryptResponse();
        try {
            String secretKey12 = "5TpM3x2021paL~nX!D.^$";
            String salt = "S4l7202157pm3x<bbx&";
            byte[] iv = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey12.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] decodedBytes = Base64.getDecoder().decode(decryptRequest.getEncrypt());
            String decryptedString = new String(cipher.doFinal(decodedBytes), "UTF-8");

            decryptResponse.setMensaje("Exito");
            decryptResponse.setCodigo(0);
            decryptResponse.setDecrypt(decryptedString);
            return decryptResponse;

        } catch (Exception e) {
            decryptResponse.setCodigo(1);
            decryptResponse.setMensaje(e.getMessage());
            System.out.println("Error en la desencriptación: " + e.getMessage());
            return decryptResponse;
        }
    }


}


