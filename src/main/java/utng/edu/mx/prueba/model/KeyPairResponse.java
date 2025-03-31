package utng.edu.mx.prueba.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class KeyPairResponse {
    private String keystoreBytes;
    private String publicKey;
    private String privateKey;
    private boolean success;
}
