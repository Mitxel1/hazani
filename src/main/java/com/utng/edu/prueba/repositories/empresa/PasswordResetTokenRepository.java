package com.utng.edu.prueba.repositories.empresa;

import com.utng.edu.prueba.entity.empresa.PasswordResetToken;
import com.utng.edu.prueba.entity.empresa.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUsuario(Usuarios usuario);

    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate <= ?1")
    void deleteAllExpiredSince(LocalDateTime now);

    @Transactional
    @Modifying
    void deleteByUsuario(Usuarios usuario);
}