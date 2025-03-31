package utng.edu.mx.prueba.service;

import org.h2.mvstore.Page;
import utng.edu.mx.prueba.entity.empresa.Productos;
import utng.edu.mx.prueba.model.ProductoRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Productos crearProducto(ProductoRequest productoRequest, String firma);
    List<Productos> listarProductos();
    Optional<Productos> obtenerProductoPorId(Long id);
    Productos actualizarProducto(String nombre, ProductoRequest productoRequest,  String firma);
    void eliminarProducto( String nombre,  String firma);
    List<Productos> buscarProductosPorNombreYFechaCreacion(
            String nombre,
            LocalDateTime fechaCreacion,
            int pagina,
            int tamanio,
            String firma
    );
}
