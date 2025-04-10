package com.utng.edu.prueba.service;

import com.utng.edu.prueba.model.cliente.ClienteRequest;
import com.utng.edu.prueba.model.cliente.ClienteResponse;

public interface ClienteService {
    ClienteResponse crearCliente(ClienteRequest clienteRequest);
    ClienteResponse obtenerClientePorId(Integer id);
    ClienteResponse actualizarCliente(Integer id, ClienteRequest clienteRequest);
    boolean eliminarCliente(Integer id);

}
