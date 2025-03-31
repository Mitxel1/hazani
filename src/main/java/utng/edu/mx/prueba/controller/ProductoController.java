package utng.edu.mx.prueba.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.mvstore.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utng.edu.mx.prueba.entity.empresa.Productos;
import utng.edu.mx.prueba.exception.ProductoNotFoundException;
import utng.edu.mx.prueba.model.ProductoRequest;
import utng.edu.mx.prueba.model.ProductoResponse;
import utng.edu.mx.prueba.service.ProductoService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping(value = "/crearProducto", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest request) {
        try {
            log.info("Solicitud para crear producto recibida: {}", request);

            // Llamamos al servicio para crear el producto
            Productos producto = productoService.crearProducto(request, request.getFirma());

            // Crear la respuesta
            ProductoResponse productoResponse = mapearProductoAResponse(producto);
            productoResponse.setCodigo("200");
            productoResponse.setMensaje("Producto creado exitosamente");

            return new ResponseEntity<>(productoResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al crear producto: {}", e.getMessage());
            ProductoResponse errorResponse = new ProductoResponse();
            errorResponse.setCodigo("400");
            errorResponse.setMensaje("Error de validación: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage(), e);
            ProductoResponse errorResponse = new ProductoResponse();
            errorResponse.setCodigo("500");
            errorResponse.setMensaje("Ha ocurrido un error al agregar el producto: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



//    @GetMapping
//    public ResponseEntity<List<Productos>> listarProductos() {
//        try {
//            log.info("Solicitud para listar todos los productos");
//            return ResponseEntity.ok(productoService.listarProductos());
//        } catch (Exception e) {
//            log.error("Error al listar productos: {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @GetMapping("/obtener/{id}")
//    public ResponseEntity<ProductoResponse> obtenerProducto(@PathVariable Long id) {
//        try {
//            log.info("Solicitud para obtener producto con ID: {}", id);
//
//            // Validar que el ID sea válido
//            if (id == null || id <= 0) {
//                ProductoResponse badRequestResponse = new ProductoResponse();
//                badRequestResponse.setCodigo("400");
//                badRequestResponse.setMensaje("El ID del producto debe ser un número positivo");
//                return new ResponseEntity<>(badRequestResponse, HttpStatus.BAD_REQUEST);
//            }
//
//            return productoService.obtenerProductoPorId(id)
//                    .map(producto -> {
//                        ProductoResponse response = mapearProductoAResponse(producto);
//                        response.setCodigo("200");
//                        response.setMensaje("Producto encontrado");
//                        return new ResponseEntity<>(response, HttpStatus.OK);
//                    })
//                    .orElseGet(() -> {
//                        ProductoResponse notFoundResponse = new ProductoResponse();
//                        notFoundResponse.setCodigo("404");
//                        notFoundResponse.setMensaje("Producto no encontrado con ID: " + id);
//                        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//                    });
//        } catch (IllegalArgumentException e) {
//            log.error("Error de validación al obtener producto: {}", e.getMessage());
//            ProductoResponse errorResponse = new ProductoResponse();
//            errorResponse.setCodigo("400");
//            errorResponse.setMensaje("Error de validación: " + e.getMessage());
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            log.error("Error al obtener producto: {}", e.getMessage(), e);
//            ProductoResponse errorResponse = new ProductoResponse();
//            errorResponse.setCodigo("500");
//            errorResponse.setMensaje("Ha ocurrido un error al consultar el producto: " + e.getMessage());
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PutMapping("/actualizar/{nombre}")
    public ResponseEntity<ProductoResponse> actualizarProducto(
            @PathVariable String nombre,
            @Valid @RequestBody ProductoRequest productoRequest) {
        try {
            log.info("Solicitud para actualizar producto con nombre: {}", nombre);

            // Llamamos al servicio para actualizar el producto
            Productos productoActualizado = productoService.actualizarProducto(nombre, productoRequest, productoRequest.getFirma());

            // Crear la respuesta
            ProductoResponse response = mapearProductoAResponse(productoActualizado);
            response.setCodigo("200");
            response.setMensaje("Producto actualizado exitosamente");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al actualizar producto: {}", e.getMessage());
            ProductoResponse errorResponse = new ProductoResponse();
            errorResponse.setCodigo("400");
            errorResponse.setMensaje("Error de validación: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (ProductoNotFoundException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            ProductoResponse notFoundResponse = new ProductoResponse();
            notFoundResponse.setCodigo("404");
            notFoundResponse.setMensaje(e.getMessage());
            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al actualizar producto: {}", e.getMessage(), e);
            ProductoResponse errorResponse = new ProductoResponse();
            errorResponse.setCodigo("500");
            errorResponse.setMensaje("Ha ocurrido un error al actualizar el producto: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Eliminar
    @DeleteMapping("/eliminar/")
    public ResponseEntity<ProductoResponse> eliminarProducto(
            @RequestParam String nombre,
            @RequestParam String firma) {
        try {
            log.info("Solicitud para eliminar productos con nombre: {}", nombre);

            // Llamamos al servicio para eliminar el producto
            productoService.eliminarProducto(nombre, firma);

            // Crear el ProductoResponse para indicar éxito
            ProductoResponse response = new ProductoResponse();
            response.setCodigo("200");
            response.setMensaje("Productos eliminados exitosamente.");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductoNotFoundException e) {
            log.error("Producto no encontrado: {}", e.getMessage());
            ProductoResponse notFoundResponse = new ProductoResponse();
            notFoundResponse.setCodigo("404");
            notFoundResponse.setMensaje(e.getMessage());
            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage(), e);
            ProductoResponse errorResponse = new ProductoResponse();
            errorResponse.setCodigo("500");
            errorResponse.setMensaje("Ha ocurrido un error al eliminar el producto: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaCreacion,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam String firma) {
        try {
            log.info("Solicitud para buscar productos - Nombre: {} | Fecha: {}", nombre, fechaCreacion);

            // Llamamos al servicio para buscar productos
            List<Productos> productos = productoService.buscarProductosPorNombreYFechaCreacion(
                    nombre, fechaCreacion != null ? fechaCreacion.atStartOfDay() : null, pagina, tamanio, firma);

            boolean seEncontroProductos = productos.stream()
                    .anyMatch(p -> (nombre == null || p.getNombre().toLowerCase().contains(nombre.toLowerCase())) &&
                            (fechaCreacion == null || p.getFechaCreacion().toLocalDate().equals(fechaCreacion)));

            String mensaje = seEncontroProductos ? "Búsqueda realizada con éxito" :
                    "No se encontraron productos con los filtros aplicados, se muestra la lista completa.";

            return ResponseEntity.ok(Map.of(
                    "productos", productos,
                    "paginaActual", pagina,
                    "tamanio", tamanio,
                    "totalElementos", productos.size(),
                    "codigo", "200",
                    "mensaje", mensaje
            ));
        } catch (Exception e) {
            log.error("Error al buscar productos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("codigo", "500", "mensaje", "Error al buscar productos: " + e.getMessage()));
        }
    }


    // Métodos privados de utilidad

    private ProductoResponse mapearProductoAResponse(Productos producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setCantidad(producto.getCantidad());
        response.setCategoria(producto.getCategoria());
        response.setFechaCreacion(producto.getFechaCreacion());
        return response;
    }

    private void validarTiposDeDatosProductoRequest(ProductoRequest request) {
        // Estas validaciones son una capa adicional de seguridad para casos donde
        // la deserialización de Spring no detecte correctamente tipos incorrectos

        if (request.getPrecio() != null) {
            try {
                // Verificar que el precio sea realmente un número decimal
                request.getPrecio().doubleValue();
            } catch (Exception e) {
                throw new IllegalArgumentException("El precio debe ser un valor numérico válido");
            }
        }

        if (request.getCantidad() != null) {
            if (!(request.getCantidad() instanceof Integer)) {
                throw new IllegalArgumentException("La cantidad debe ser un valor entero");
            }
        }
    }
}
