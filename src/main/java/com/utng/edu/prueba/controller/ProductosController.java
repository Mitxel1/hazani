package com.utng.edu.prueba.controller;

import com.utng.edu.prueba.model.productos.*;
import com.utng.edu.prueba.service.CryptoServicesProductos;
import com.utng.edu.prueba.service.ProductosService;
import com.utng.edu.prueba.service.impl.ProductosServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

@RestController
@RequestMapping("/API/v1/producto")
public class ProductosController {

    private static final Logger logger = LoggerFactory.getLogger(ProductosController.class);

    @Autowired
    private ProductosService productosService;

    @Autowired
    private CryptoServicesProductos cryptoServicesProductos;

    // Método privado para manejar validación de firma
    private boolean validarFirma(Object request, String tipoOperacion) {

        try {
            switch (tipoOperacion) {
                case "CREAR":
                    if (request instanceof ProductoRequest) {
                        return cryptoServicesProductos.validacionCadenaOriginalAltaProducto((ProductoRequest) request);
                    }
                    break;
                case "ACTUALIZAR":
                    if (request instanceof ProductoUpdateRequest) {
                        return cryptoServicesProductos.validacionCadenaOriginalActualizarProducto((ProductoUpdateRequest) request);
                    }
                    break;
                case "ELIMINAR":
                    if (request instanceof ProductoDeleteRequest) {
                        return cryptoServicesProductos.validacionCadenaOriginalBajaProducto((ProductoDeleteRequest) request);
                    }
                    break;
                case "LISTAR":
                    if (request instanceof ProductoListRequest) {
                        return cryptoServicesProductos.validacionCadenaOriginalListarProducto((ProductoListRequest) request);
                    }
                    break;
                default:

                    return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @PostMapping("/crearProducto")
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest productoRequest) {
        if (!validarFirma(productoRequest, "CREAR")) {
            logger.error("Firma inválida para la creación del producto: {}", productoRequest.getNombre());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProductoResponse(1, "Firma inválida"));
        }

        try {
            ProductoResponse productoResponse = productosService.crearProducto(productoRequest);
            return ResponseEntity.status(
                    productoResponse.getCodigo() == 0 ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST
            ).body(productoResponse);
        } catch (Exception e) {
            logger.error("Error al crear producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProductoResponse(1, "Error interno del servidor"));
        }
    }

    // Obtener Producto por Nombre
    @GetMapping("/obtenerProductoPorNombre/{nombre}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorNombre(@PathVariable String nombre) {
        try {
            ProductoResponse productoResponse = productosService.obtenerProductoPorNombre(nombre);
            return productoResponse != null
                    ? ResponseEntity.ok(productoResponse)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductoResponse(1, "Producto no encontrado"));
        } catch (Exception e) {
            logger.error("Error al obtener producto por nombre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ProductoResponse> actualizarProducto(
            @Valid @RequestBody ProductoUpdateRequest productoUpdateRequest) {
        // Validar la firma
        if (!validarFirma(productoUpdateRequest, "ACTUALIZAR")) {
            logger.warn("Firma inválida para la actualización del producto: {}", productoUpdateRequest.getNombreOriginal());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProductoResponse(1, "Firma inválida"));
        }

        try {
            // Actualizar el producto
            ProductoResponse productoResponse = productosService.actualizarProducto(productoUpdateRequest);

            // Verificar si la actualización fue exitosa
            if (productoResponse.getCodigo() == 0) {
                logger.info("Producto actualizado correctamente: {}", productoUpdateRequest.getNombreOriginal());
                return ResponseEntity.ok(productoResponse);
            } else {
                logger.warn("Error al actualizar el producto: {}", productoResponse.getMensaje());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productoResponse);
            }
        } catch (ProductosServiceImpl.ProductoNoEncontradoException | ProductosServiceImpl.NombreDuplicadoException e) {
            logger.error("Error de validación al actualizar el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProductoResponse(1, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al actualizar el producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProductoResponse(1, "Error interno del servidor"));
        }
    }
    @PostMapping("/eliminar")
    public ResponseEntity<ProductoResponse> eliminarProducto(@Valid @RequestBody ProductoDeleteRequest productoDeleteRequest) {
        if (!validarFirma(productoDeleteRequest, "ELIMINAR")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProductoResponse(1, "Firma inválida"));
        }

        try {
            ResponseEntity<ProductoResponse> response = productosService.eliminarProducto(productoDeleteRequest);
            return response;
        } catch (Exception e) {
            logger.error("Error al eliminar producto", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProductoResponse(1, "Error interno del servidor"));
        }
    }
    // Listar Productos por Nombre y Fecha
    @PostMapping("/listar")
    public ResponseEntity<ProductoListResponse> listarProductos(@Valid @RequestBody ProductoListRequest productoListRequest) {
        if (!validarFirma(productoListRequest, "LISTAR")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ProductoListResponse(1, "Firma inválida"));
        }

        try {
            ProductoListResponse productoListResponse = productosService.listarProductos(productoListRequest);
            return productoListResponse.getCodigo() == 0
                    ? ResponseEntity.ok(productoListResponse)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productoListResponse);
        } catch (Exception e) {
            logger.error("Error al listar productos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ProductoListResponse(1, "Error interno del servidor"));
        }
    }
}