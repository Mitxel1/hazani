package utng.edu.mx.prueba.service;

import org.springframework.stereotype.Service;
import utng.edu.mx.prueba.entity.empresa.Cliente;
import utng.edu.mx.prueba.model.ClienteRequest;
import utng.edu.mx.prueba.model.ClienteResponse;

import java.util.List;

public interface ClienteService {

    ClienteResponse crearCliente(ClienteRequest request);

    ClienteResponse actualizarCliente(Long id, ClienteRequest request);

    ClienteResponse eliminarCliente(Long id);

    List<ClienteResponse> listarClientes();

    ClienteResponse obtenerCliente(Long id);

}
