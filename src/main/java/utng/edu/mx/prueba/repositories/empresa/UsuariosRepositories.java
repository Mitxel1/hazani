package utng.edu.mx.prueba.repositories.empresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utng.edu.mx.prueba.entity.empresa.Usuario;

import java.util.Optional;

@Repository
public interface UsuariosRepositories extends JpaRepository<Usuario, Integer> {
    Page<Usuario> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);

    // Método para verificar si un email existe
    boolean existsByEmail(String email);

    // Método opcional para buscar por email
    Optional<Usuario> findByEmail(String email);
}
