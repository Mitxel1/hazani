package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.DefaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.HashMap;

@ControllerAdvice
@Slf4j

public class FieldsControllerValidations {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse> dtoValidation(Errors errors) {
        DefaultResponse respuestaError = new DefaultResponse();
        HashMap<Object, Object> listError = new HashMap<>();
        for (FieldError fieldError : errors.getFieldErrors()) {
            listError.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        respuestaError.setCodigo(DefaultResponse.FIRMA_INVALIDA);
        respuestaError.setMensaje(DefaultResponse.FIRMA_INVALIDA_MENSAJE);

        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }
}