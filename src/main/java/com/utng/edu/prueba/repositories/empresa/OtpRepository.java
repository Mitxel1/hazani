package com.utng.edu.prueba.repositories.empresa;

import com.utng.edu.prueba.entity.empresa.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM Otp o WHERE o.username = :username")
    void deleteByUsername(String username);


}