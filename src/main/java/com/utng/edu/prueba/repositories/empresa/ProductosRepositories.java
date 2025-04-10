package com.utng.edu.prueba.repositories.empresa;

import com.utng.edu.prueba.entity.empresa.Productos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductosRepositories extends JpaRepository<Productos, Integer> {

    Optional<Productos> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    Page<Productos> findByNombreContaining(String nombre, Pageable pageable);

    Page<Productos> findByCategoriaContaining(String categoria, Pageable pageable);

    Page<Productos> findByNombreContainingAndCategoriaContaining(String nombre, String categoria, Pageable pageable);

    // Buscar por nombre y fecha dentro de un rango de tiempo

    Page<Productos> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Productos> findByNombreContainingIgnoreCaseAndFechaCreacionBetween(
            String nombre,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Pageable pageable
    );

    Page<Productos> findByFechaCreacionBetween(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Pageable pageable
    );

}