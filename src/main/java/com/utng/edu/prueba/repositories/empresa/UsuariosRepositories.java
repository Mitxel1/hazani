package com.utng.edu.prueba.repositories.empresa;

import com.utng.edu.prueba.entity.empresa.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepositories extends JpaRepository<Usuarios, Integer> {
    Page<Usuarios> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Optional<Usuarios> findByUsername(String username);
    boolean existsByUsername(String username);

    Optional<Usuarios> findByCorreo(String email);
}
