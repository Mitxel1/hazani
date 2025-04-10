package com.utng.edu.prueba.service;
import com.utng.edu.prueba.model.productos.*;

public interface GenerarFirmaProductoService {
    String generarFirmaAltaProducto(ProductoRequest productoRequest);

    String generarFirmaActualizarProducto(ProductoUpdateRequest productoUpdateRequest);

    String generarFirmaBajaProducto(ProductoDeleteRequest productoDeleteRequest);

    String generarFirmaListarProducto(ProductoListRequest productoListRequest);
}
