package com.utng.edu.prueba.repositories.empresa;

import com.utng.edu.prueba.entity.empresa.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepositories extends JpaRepository<Cliente, Integer> {
    Page<Cliente> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}
