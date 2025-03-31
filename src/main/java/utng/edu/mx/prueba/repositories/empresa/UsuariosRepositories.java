package utng.edu.mx.prueba.repositories.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utng.edu.mx.prueba.entity.empresa.Usuarios;

import java.util.Optional;

@Repository
public interface UsuariosRepositories extends JpaRepository<Usuarios, Integer> {
    Page<Usuarios> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Optional<Usuarios> findByUsername(String username);
    boolean existsByUsername(String username);

    // Buscar usuarios por estatus
    Page<Usuarios> findByEstatus(Boolean estatus, Pageable pageable);

    // Buscar usuarios por username y estatus
    Page<Usuarios> findByUsernameContainingIgnoreCaseAndEstatus(String username, Boolean estatus, Pageable pageable);

    // Eliminar usuario por username
    void deleteByUsername(String username);

    // Contar usuarios por estatus
    long countByEstatus(Boolean estatus);
}
