package utng.edu.mx.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import utng.edu.mx.prueba.model.ClienteRequest;
import utng.edu.mx.prueba.model.ClienteResponse;
import utng.edu.mx.prueba.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Crear un nuevo cliente
    @PostMapping("/crear")
    public ResponseEntity<ClienteResponse> crearCliente(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.crearCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Se debe devolver código 201 para creación exitosa.
    }

    // Actualizar un cliente existente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteService.actualizarCliente(id, request);
        if (response == null) { // Si no se encuentra el cliente
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve un 404
        }
        return ResponseEntity.ok(response);
    }

    // Eliminar un cliente por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ClienteResponse> eliminarCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.eliminarCliente(id));
    }


    // Obtener una lista de todos los clientes
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        List<ClienteResponse> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    // Obtener un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerCliente(@PathVariable Long id) {
        ClienteResponse cliente = clienteService.obtenerCliente(id);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra el cliente
        }
        return ResponseEntity.ok(cliente);
    }
}
