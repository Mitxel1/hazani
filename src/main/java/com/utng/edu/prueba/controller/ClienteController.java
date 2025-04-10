package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.cliente.ClienteRequest;
import com.utng.edu.prueba.model.cliente.ClienteResponse;
import com.utng.edu.prueba.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/API/v1/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/crearCliente")
    public ResponseEntity<ClienteResponse> crearCliente(@RequestBody ClienteRequest clienteRequest) {
        return new ResponseEntity<>(clienteService.crearCliente(clienteRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(@PathVariable Integer id,
                                                             @RequestBody ClienteRequest clienteRequest) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, clienteRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok("Cliente eliminado con éxito");
    }

}
