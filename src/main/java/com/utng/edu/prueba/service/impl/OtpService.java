package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.entity.empresa.Otp;
import com.utng.edu.prueba.repositories.empresa.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public void storeOtp(String username, String otp) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        // Eliminar cualquier OTP existente para este usuario
        otpRepository.deleteByUsername(username);

        Otp otpEntity = new Otp();
        otpEntity.setUsername(username);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(expirationTime);

        otpRepository.save(otpEntity);
    }

    public Optional<Otp> getOtp(String username) {
        return otpRepository.findByUsername(username);
    }

    public boolean isOtpExpired(Otp otpEntity) {
        return otpEntity.getExpirationTime().isBefore(LocalDateTime.now());
    }

    public void deleteOtp(Otp otp) {
        otpRepository.delete(otp);
    }

    // En OtpService.java
    @Transactional
    public void deleteByUsername(String username) {
        otpRepository.deleteByUsername(username);
    }
}