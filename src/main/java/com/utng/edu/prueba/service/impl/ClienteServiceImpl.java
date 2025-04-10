package com.utng.edu.prueba.service.impl;
import com.utng.edu.prueba.entity.empresa.Cliente;
import com.utng.edu.prueba.model.cliente.ClienteRequest;
import com.utng.edu.prueba.model.cliente.ClienteResponse;
import com.utng.edu.prueba.repositories.empresa.ClienteRepositories;
import com.utng.edu.prueba.service.ClienteService;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepositories clienteRepositories;


    @Override
    public ClienteResponse crearCliente(ClienteRequest clienteRequest) {
        if (!EmailValidator.esEmailValido(clienteRequest.getEmail())) {
            throw new IllegalArgumentException("Formato de correo inválido");
        }
        if (clienteRepositories.existsByEmail(clienteRequest.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteRequest, cliente);
        clienteRepositories.save(cliente);

        ClienteResponse clienteResponse = new ClienteResponse();
        BeanUtils.copyProperties(cliente, clienteResponse);
        return clienteResponse;
    }

    public class EmailValidator {
        private static final String EMAIL_REGEX = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$";
        private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

        public static boolean esEmailValido(String email) {
            return email != null && pattern.matcher(email).matches();
        }
    }

    @Override
    public ClienteResponse obtenerClientePorId(Integer id) {
        Cliente cliente = clienteRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        ClienteResponse response = new ClienteResponse();
        BeanUtils.copyProperties(cliente, response);
        return response;
    }

    @Override
    public ClienteResponse actualizarCliente(Integer id, ClienteRequest clienteRequest) {
        Cliente cliente = clienteRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        try {
            BeanUtils.copyProperties(clienteRequest, cliente, "id_cliente", "fechaCreacion");
            clienteRepositories.save(cliente);

            ClienteResponse response = new ClienteResponse();
            BeanUtils.copyProperties(cliente, response);
            return response;

        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Error de concurrencia, intenta nuevamente.");
        }
    }

    @Override
    public boolean eliminarCliente(Integer id) {
        if (!clienteRepositories.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado.");
        }
        clienteRepositories.deleteById(id);
        return true;
    }

}
