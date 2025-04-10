package com.utng.edu.prueba.entity.empresa;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuarios usuario;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // Método para verificar si el token ha expirado
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}