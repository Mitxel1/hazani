package com.utng.edu.prueba.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EncryptResponse extends DefaultResponse{
    private String encrypt;
    private String encryptedString;
    private String message;

    public String getEncryptedString() {
        return encryptedString;
    }

    public void setEncryptedString(String encryptedString) {
        this.encryptedString = encryptedString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}







