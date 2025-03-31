package utng.edu.mx.prueba.model;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data

public class EncryptRequest {
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[^\\s\"']+$", message = "El campo 'encrypt' no debe contener espacios, comillas simples o dobles.")
    private String encrypt;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
            this.encrypt = encrypt;
    }
}
