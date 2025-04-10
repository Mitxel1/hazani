package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DecryptResponse extends DefaultResponse {
    private String decrypt;
    private String decryptedString;
    private String message;

    public String getDecryptedString() {
        return decryptedString;
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
