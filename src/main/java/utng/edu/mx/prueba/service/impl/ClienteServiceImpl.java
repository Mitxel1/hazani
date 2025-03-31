package utng.edu.mx.prueba.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utng.edu.mx.prueba.entity.empresa.Cliente;
import utng.edu.mx.prueba.model.ClienteRequest;
import utng.edu.mx.prueba.model.ClienteResponse;
import utng.edu.mx.prueba.repositories.empresa.ClienteRepository;
import utng.edu.mx.prueba.service.ClienteService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponse crearCliente(ClienteRequest request) {
        ClienteResponse response = new ClienteResponse();

        // Validaciones
        if (clienteRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setCodigo(1);
            response.setMensaje("El email ya está registrado");
            return response;
        }

        if (request.getEdad() > 100) {
            response.setCodigo(1);
            response.setMensaje("La edad máxima permitida es 100");
            return response;
        }

        // Mapeo de request a entidad
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setEdad(String.valueOf(request.getEdad()));
        cliente.setSexo(request.getSexo());

        // Guardar cliente
        clienteRepository.save(cliente);

        // Preparar respuesta
        response.setCodigo(0);
        response.setMensaje("Cliente registrado exitosamente");
        return response;
    }

    @Override
    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {
        ClienteResponse response = new ClienteResponse();

        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            response.setCodigo(1);
            response.setMensaje("Cliente no encontrado");
            return response;
        }

        if (request.getEdad() > 100) {
            response.setCodigo(1);
            response.setMensaje("La edad máxima permitida es 100");
            return response;
        }

        Cliente cliente = clienteOpt.get();
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setEdad(String.valueOf(request.getEdad()));
        cliente.setSexo(request.getSexo());

        clienteRepository.save(cliente);

        response.setCodigo(0);
        response.setMensaje("Cliente actualizado exitosamente");
        return response;
    }

    @Override
    public ClienteResponse eliminarCliente(Long id) {
        ClienteResponse response = new ClienteResponse();

        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            response.setCodigo(1);
            response.setMensaje("Cliente no encontrado");
            return response;
        }

        clienteRepository.delete(clienteOpt.get());

        response.setCodigo(0);
        response.setMensaje("Cliente eliminado exitosamente");
        return response;
    }

    @Override
    public List<ClienteResponse> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(cliente -> {
                    ClienteResponse response = new ClienteResponse();
                    response.setId(cliente.getIdCliente());  // Cambié de getId() a getIdCliente()
                    response.setNombre(cliente.getNombre());
                    response.setEmail(cliente.getEmail());
                    response.setTelefono(cliente.getTelefono());
                    response.setEdad(Integer.parseInt(cliente.getEdad()));
                    response.setSexo(cliente.getSexo());
                    response.setCodigo(0);
                    response.setMensaje("Cliente: " + cliente.getNombre());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponse obtenerCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        ClienteResponse response = new ClienteResponse();

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            response.setId(cliente.getIdCliente());  // Cambié de getId() a getIdCliente()
            response.setNombre(cliente.getNombre());
            response.setEmail(cliente.getEmail());
            response.setTelefono(cliente.getTelefono());
            response.setEdad(Integer.parseInt(cliente.getEdad()));
            response.setSexo(cliente.getSexo());
            response.setCodigo(0);
            response.setMensaje("Cliente encontrado");
        } else {
            response.setCodigo(1);
            response.setMensaje("Cliente no encontrado");
        }

        return response;
    }
}
