package com.utng.edu.prueba.service;

import com.utng.edu.prueba.entity.empresa.Productos;
import com.utng.edu.prueba.model.productos.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductosService {
    // Método para crear un producto
    ProductoResponse crearProducto(@Valid ProductoRequest productoRequest);

    // Método para obtener un producto por su nombre
    ProductoResponse obtenerProductoPorNombre(String nombre);

    ProductoResponse actualizarProducto(@Valid ProductoUpdateRequest productoUpdateRequest);

    // Método para eliminar un producto
    ResponseEntity<ProductoResponse> eliminarProducto(@Valid ProductoDeleteRequest productoDeleteRequest);

    // Método para listar productos con filtros y paginación
    ProductoListResponse listarProductos(@Valid ProductoListRequest productoListRequest);

}