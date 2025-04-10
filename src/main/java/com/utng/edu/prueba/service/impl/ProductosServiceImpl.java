package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.entity.empresa.Productos;
import com.utng.edu.prueba.model.productos.*;
import com.utng.edu.prueba.repositories.empresa.ProductosRepositories;
import com.utng.edu.prueba.service.ProductosService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Validated
public class ProductosServiceImpl implements ProductosService {

    @Autowired
    private ProductosRepositories productosRepositories;

    @Override
    public ProductoResponse crearProducto(@Valid ProductoRequest productoRequest) {
        try {

            if (productosRepositories.existsByNombre(productoRequest.getNombre())) {
                throw new IllegalArgumentException("El nombre ya está registrado. ✅");
            }


            if (productoRequest.getNombre() == null || productoRequest.getNombre().length() > 100) {
                throw new IllegalArgumentException("El nombre no puede ser nulo y debe tener una longitud máxima de 100 caracteres.❌");
            }


            if (productoRequest.getPrecio() == null || productoRequest.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio no puede ser nulo y debe ser mayor que cero.🔴");
            }
            if (productoRequest.getCantidad() < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa.🔴");
            }
            Productos productos = new Productos();
            BeanUtils.copyProperties(productoRequest, productos);
            productos.setFechaCreacion(LocalDateTime.now());
            productosRepositories.save(productos);

            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setCodigo(0);
            productoResponse.setMensaje("Producto creado correctamente. ✅");
            BeanUtils.copyProperties(productos, productoResponse);
            return productoResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el producto 🔴: " + e.getMessage(), e);
        }
    }

    public class ProductoNoEncontradoException extends RuntimeException {
        public ProductoNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    public class NombreDuplicadoException extends RuntimeException {
        public NombreDuplicadoException(String mensaje) {
            super(mensaje);
        }
    }

    @Override
    public ProductoResponse actualizarProducto(@Valid ProductoUpdateRequest productoRequest) {
        try {

            Productos producto = productosRepositories.findByNombre(productoRequest.getNombreOriginal())
                    .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado.🔴"));

            if (productoRequest.getNuevoNombre() != null && !productoRequest.getNuevoNombre().isBlank()
                    && !productoRequest.getNuevoNombre().equals(productoRequest.getNombreOriginal())) {

                if (productosRepositories.existsByNombre(productoRequest.getNuevoNombre())) {
                    throw new NombreDuplicadoException("El nuevo nombre ya está registrado.✅");
                }
                producto.setNombre(productoRequest.getNuevoNombre());
            }


            if (productoRequest.getNuevoNombre() != null && productoRequest.getNuevoNombre().length() > 100) {
                throw new IllegalArgumentException("El nombre no puede ser nulo y debe tener una longitud máxima de 100 caracteres.🔴");
            }


            if (productoRequest.getPrecio() != null && productoRequest.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio no puede ser nulo y debe ser mayor que cero.🔴");
            }


            if (productoRequest.getCantidad() != null && productoRequest.getCantidad() < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa.🔴");
            }


            if (productoRequest.getDescripcion() != null && !productoRequest.getDescripcion().isBlank()
                    && !"string".equalsIgnoreCase(productoRequest.getDescripcion())) {
                producto.setDescripcion(productoRequest.getDescripcion());
            }

            if (productoRequest.getPrecio() != null && productoRequest.getPrecio().compareTo(BigDecimal.ZERO) > 0) {
                producto.setPrecio(productoRequest.getPrecio());
            }

            if (productoRequest.getCantidad() != null && productoRequest.getCantidad() >= 0) {
                producto.setCantidad(productoRequest.getCantidad());
            }

            if (productoRequest.getCategoria() != null && !productoRequest.getCategoria().isBlank()
                    && !"string".equalsIgnoreCase(productoRequest.getCategoria())) {
                producto.setCategoria(productoRequest.getCategoria());
            }


            productosRepositories.save(producto);


            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setCodigo(0);
            productoResponse.setMensaje("Producto actualizado correctamente.✅");
            BeanUtils.copyProperties(producto, productoResponse);
            return productoResponse;
        } catch (ProductoNoEncontradoException | NombreDuplicadoException e) {
            throw e;
        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Error de concurrencia, intenta nuevamente.🔴", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el producto 🔴: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<ProductoResponse> eliminarProducto(@Valid ProductoDeleteRequest request) {
        try {
            Productos productos = productosRepositories.findByNombre(request.getNombre())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado.🔴"));

            productosRepositories.delete(productos);

            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setCodigo(0);
            productoResponse.setMensaje("Producto eliminado correctamente.✅");
            return ResponseEntity.ok(productoResponse);
        } catch (IllegalArgumentException e) {
            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setCodigo(1);
            productoResponse.setMensaje(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(productoResponse);
        } catch (Exception e) {

            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setCodigo(1);
            productoResponse.setMensaje("Error interno del servidor 🔴");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(productoResponse);
        }
    }

    @Override
    public ProductoResponse obtenerProductoPorNombre(String nombre) {
        try {
            Productos productos = productosRepositories.findByNombre(nombre)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado 🔴"));

            ProductoResponse productoResponse = new ProductoResponse();
            BeanUtils.copyProperties(productos, productoResponse);
            return productoResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el producto 🔴: " + e.getMessage(), e);
        }
    }

    @Override
    public ProductoListResponse listarProductos(ProductoListRequest productosListRequest) {
        ProductoListResponse productosResponse = new ProductoListResponse();
        try {
            LocalDate startDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                startDate = LocalDate.parse(productosListRequest.getFecha_creacion(), formatter);
            } catch (DateTimeParseException e) {
                productosResponse.setCodigo(400);
                productosResponse.setMensaje("Formato de fecha inválido. El formato correcto es yyyy-MM-dd. 🔴");
                return productosResponse;
            }
            if (startDate.isBefore(LocalDate.of(2024, 1, 1)) || startDate.isAfter(LocalDate.now())) {
                productosResponse.setCodigo(400);
                productosResponse.setMensaje("La fecha debe estar entre 2024-01-01 y la fecha actual.");
                return productosResponse;
            }

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = startDateTime.plusDays(1);

            Pageable pageable = PageRequest.of(
                    Math.toIntExact(productosListRequest.getPagina()),
                    Math.toIntExact(productosListRequest.getTamañoPagina()),
                    Sort.by("nombre").ascending()
            );
            System.out.println("Rango de búsqueda: " + startDateTime + " a " + endDateTime);
            System.out.println("name:" + productosListRequest.getNombre());
            Page<Productos> productoList;
            if (productosListRequest.getNombre() != null && !productosListRequest.getNombre().trim().isEmpty()) {
                productoList = productosRepositories.findByNombreContainingIgnoreCaseAndFechaCreacionBetween(
                        productosListRequest.getNombre().trim(),
                        startDateTime,
                        endDateTime,
                        pageable);
            } else {
                productoList = productosRepositories.findByFechaCreacionBetween(
                        startDateTime,
                        endDateTime,
                        pageable);
            }

            if (!productoList.isEmpty()) {
                productosResponse.setProductos(productoList.getContent());
                productosResponse.setTotalElements(productoList.getTotalElements());
                productosResponse.setTotalPaginas(productoList.getTotalPages());
                productosResponse.setCodigo(0);
                productosResponse.setMensaje("Éxito ✅");
            } else {
                productosResponse.setCodigo(1);
                productosResponse.setMensaje("No existen registros 🔴.");
            }

        } catch (Exception e) {
            productosResponse.setCodigo(1);
            productosResponse.setMensaje("Error al listar los productos 🔴: " + e.getMessage());
        }
        return productosResponse;
    }
}