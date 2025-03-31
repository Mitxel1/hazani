package utng.edu.mx.prueba.repositories.empresa;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import utng.edu.mx.prueba.entity.empresa.Productos;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductoRepositories extends JpaRepository<Productos, Long> {
    // Buscar por nombre y fecha dentro de un rango de tiempo
    List<Productos> findByNombreContainingIgnoreCaseAndFechaCreacionBetween(
            String nombre,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    // Buscar solo por nombre (ignorando mayúsculas y minúsculas)
    List<Productos> findByNombreContainingIgnoreCase(
            String nombre,
            Pageable pageable
    );

    List<Productos> findByNombreContainingIgnoreCase(
            String nombre
    );

    // Buscar solo por rango de fechas
    List<Productos> findByFechaCreacionBetween(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Productos p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    void eliminarPorNombre(@Param("nombre") String nombre);

    boolean existsByNombre(String nombre);
}
