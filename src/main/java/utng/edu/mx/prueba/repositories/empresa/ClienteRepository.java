package utng.edu.mx.prueba.repositories.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import utng.edu.mx.prueba.entity.empresa.Cliente;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
