package utng.edu.mx.prueba.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utng.edu.mx.prueba.entity.empresa.Productos;
import utng.edu.mx.prueba.exception.ProductoNotFoundException;
import utng.edu.mx.prueba.model.ProductoRequest;
import utng.edu.mx.prueba.repositories.empresa.ProductoRepositories;
import utng.edu.mx.prueba.service.ProductoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepositories productoRepositories;

    @Autowired
    private FirmaDigitalService firmaDigitalService;

    @Override
    public Productos crearProducto(ProductoRequest productoRequest, String firma) {
        try {
            // Validar la firma
            if (!firmaDigitalService.validarFirmaConTiempo("crearProducto", firma)) {
                throw new IllegalArgumentException("Firma no válida para crear producto");
            }

            // Resto de la lógica para crear el producto...
            if (productoRequest.getNombre() == null || productoRequest.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
            }

            if (productoRequest.getPrecio() == null || productoRequest.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor que cero");
            }

            if (productoRequest.getCantidad() == null || productoRequest.getCantidad() < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa");
            }

            if (productoRequest.getCategoria() == null || productoRequest.getCategoria().trim().isEmpty()) {
                throw new IllegalArgumentException("La categoría no puede ser nula o vacía");
            }

            if (!productoRequest.getCategoria().matches("^[a-zA-Z]+$")) {
                throw new IllegalArgumentException("La categoría solo puede contener letras");
            }

            if (productoRepositories.existsByNombre(productoRequest.getNombre())) {
                throw new IllegalArgumentException("Ya existe un producto con el nombre: " + productoRequest.getNombre());
            }

            Productos producto = new Productos();
            producto.setNombre(productoRequest.getNombre());
            producto.setDescripcion(productoRequest.getDescripcion());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setCantidad(productoRequest.getCantidad());
            producto.setCategoria(productoRequest.getCategoria());
            producto.setFechaCreacion(LocalDateTime.now());

            log.info("Creando nuevo producto: {}", productoRequest.getNombre());
            return productoRepositories.save(producto);
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear producto: " + e.getMessage());
        }
    }

    //Listar Productos
    @Override
    public List<Productos> listarProductos() {
        log.info("Listando todos los productos");
        try {
            return productoRepositories.findAll();
        } catch (Exception e) {
            log.error("Error al listar productos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al listar productos: " + e.getMessage());
        }
    }

    //Obtener producto por ID
    @Override
    public Optional<Productos> obtenerProductoPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        try {
            return productoRepositories.findById(id);
        } catch (Exception e) {
            log.error("Error al obtener producto por ID: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener producto por ID: " + e.getMessage());
        }
    }

    //Actualizar Producto
    @Override
    public Productos actualizarProducto(String nombre, ProductoRequest productoRequest,String firma) {
        try {
            // Validar la firma usando el nuevo método
            if (!firmaDigitalService.validarFirmaConTiempo("actualizar", firma)) {
                throw new IllegalArgumentException("Firma no válida para actualizar producto");
            }

            // Resto de la lógica para actualizar el producto...
            Productos producto = productoRepositories.findByNombreContainingIgnoreCase(nombre)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con nombre: " + nombre));

            if (productoRequest.getNombre() != null) {
                producto.setNombre(productoRequest.getNombre());
            }

            if (productoRequest.getDescripcion() != null) {
                producto.setDescripcion(productoRequest.getDescripcion());
            }

            if (productoRequest.getPrecio() != null) {
                if (productoRequest.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El precio debe ser mayor que cero");
                }
                producto.setPrecio(productoRequest.getPrecio());
            }

            if (productoRequest.getCantidad() != null) {
                if (productoRequest.getCantidad() < 0) {
                    throw new IllegalArgumentException("La cantidad no puede ser negativa");
                }
                producto.setCantidad(productoRequest.getCantidad());
            }

            if (productoRequest.getCategoria() != null) {
                producto.setCategoria(productoRequest.getCategoria());
            }

            return productoRepositories.save(producto);
        } catch (ProductoNotFoundException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar producto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage());
        }
    }


    //Eliminar producto
    @Override
    @Transactional
    public void eliminarProducto(String nombre, String firma) {
        try {
            // Validar la firma usando el nuevo método
            if (!firmaDigitalService.validarFirmaConTiempo("eliminar", firma)) {
                throw new IllegalArgumentException("Firma no válida para eliminar producto");
            }

            // Resto de la lógica para eliminar el producto...
            List<Productos> productos = productoRepositories.findByNombreContainingIgnoreCase(nombre);

            if (productos.isEmpty()) {
                throw new ProductoNotFoundException("No se encontró ningún producto con el nombre: " + nombre);
            }

            for (Productos producto : productos) {
                if (!productoRepositories.existsById(producto.getId())) {
                    throw new ProductoNotFoundException("El producto con ID " + producto.getId() + " no existe.");
                }
            }

            productoRepositories.eliminarPorNombre(nombre);
            log.info("Productos eliminados con éxito.");
        } catch (ProductoNotFoundException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage());
        }
    }

    //
    @Override
    public List<Productos> buscarProductosPorNombreYFechaCreacion(
            String nombre,
            LocalDateTime fechaCreacion,
            int pagina,
            int tamanio,
            String firma) {
        try {
            if (!firmaDigitalService.validarFirmaConTiempo("buscar", firma)) {
                throw new IllegalArgumentException("Firma no válida para buscar productos");
            }

            // Resto de la lógica para buscar productos...
            Pageable pageable = PageRequest.of(pagina, tamanio, Sort.by("nombre").ascending());

            LocalDateTime startDateTime = fechaCreacion != null ? fechaCreacion.toLocalDate().atStartOfDay() : null;
            LocalDateTime endDateTime = fechaCreacion != null ? fechaCreacion.toLocalDate().plusDays(1).atStartOfDay() : null;

            List<Productos> productos;

            if (nombre != null && !nombre.trim().isEmpty() && fechaCreacion != null) {
                productos = productoRepositories.findByNombreContainingIgnoreCaseAndFechaCreacionBetween(
                        nombre, startDateTime, endDateTime, pageable);
            } else if (nombre != null && !nombre.trim().isEmpty()) {
                productos = productoRepositories.findByNombreContainingIgnoreCase(nombre, pageable);
            } else if (fechaCreacion != null) {
                productos = productoRepositories.findByFechaCreacionBetween(startDateTime, endDateTime, pageable);
            } else {
                productos = productoRepositories.findAll(pageable).getContent();
            }

            if (productos.isEmpty()) {
                log.warn("No se encontraron productos con los filtros aplicados. Se devolverá la lista completa.");
                return productoRepositories.findAll(pageable).getContent();
            }

            return productos;
        } catch (Exception e) {
            log.error("Error al buscar productos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar productos: " + e.getMessage());
        }
    }

}
