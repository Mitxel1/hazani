package utng.edu.mx.prueba.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import utng.edu.mx.prueba.model.KeyPairResponse;
import utng.edu.mx.prueba.service.KeystoreService;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


@Service
@Slf4j
public class KeystoreServiceImpl implements KeystoreService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    @Override
    public KeyPairResponse generateKeyPair(String alias, String password) throws Exception {
        KeyPairResponse response = new KeyPairResponse();

        try {
            // Generar par de claves RSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Crear certificado autofirmado
            X509Certificate certificate = generateSelfSignedCertificate(keyPair, alias);

            // Crear keystore y guardar el certificado
            KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);
            keyStore.setKeyEntry(alias,
                    keyPair.getPrivate(),
                    password.toCharArray(),
                    new Certificate[]{certificate});

            // Convertir keystore a bytes y codificar en Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            keyStore.store(baos, password.toCharArray());
            String keystoreBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

            // Codificar llaves en Base64
            response.setKeystoreBytes(keystoreBase64);
            response.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            response.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            response.setSuccess(true);

        } catch (Exception e) {
            log.error("Error generando keystore: ", e);
            throw e;
        }

        return response;
    }

    private X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String alias) throws Exception {
        X500Name dnName = new X500Name("CN=" + alias);
        BigInteger certSerialNumber = new BigInteger(Long.toString(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC")
                .build(keyPair.getPrivate());

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                dnName,
                certSerialNumber,
                startDate,
                endDate,
                dnName,
                keyPair.getPublic());

        return new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certBuilder.build(contentSigner));
    }
}
