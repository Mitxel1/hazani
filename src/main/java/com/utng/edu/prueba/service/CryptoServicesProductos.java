package com.utng.edu.prueba.service;

import com.utng.edu.prueba.model.productos.*;

public interface CryptoServicesProductos {
    // Validación de firma para alta de producto
    Boolean validacionCadenaOriginalAltaProducto(ProductoRequest productoRequest);

    // Validación de firma para actualización de producto
    Boolean validacionCadenaOriginalActualizarProducto(ProductoUpdateRequest productoUpdateRequest);

    // Validación de firma para baja de producto
    Boolean validacionCadenaOriginalBajaProducto(ProductoDeleteRequest productoDeleteRequest);

    // Validación de firma para listar productos
    Boolean validacionCadenaOriginalListarProducto(ProductoListRequest productosListRequest);
}