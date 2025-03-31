package utng.edu.mx.prueba.model;

import jakarta.validation.constraints.NotBlank;

public class DecryptRequest {
    @NotBlank(message = "El campo 'encrypt' no puede estar vacío")
    private String encrypt;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }
}
