package utng.edu.mx.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utng.edu.mx.prueba.service.impl.FirmaDigitalService;

@RestController
@RequestMapping("/firmas")
public class FirmaController {

    @Autowired
    private FirmaDigitalService firmaDigitalService;

    // Generar firma para crearProducto
    @GetMapping("/generar-crear")
    public String generarFirmaCrearProducto() throws Exception {
        String operacion = "crearProducto";
        // 300000 ms = 5 minutos
        return firmaDigitalService.generarFirmaConTiempo(operacion, 300000);
    }

    // Generar firma para actualizar
    @GetMapping("/generar-actualizar")
    public String generarFirmaActualizarProducto() throws Exception {
        String operacion = "actualizar";
        return firmaDigitalService.generarFirmaConTiempo(operacion, 300000);
    }

    // Generar firma para eliminar
    @GetMapping("/generar-eliminar")
    public String generarFirmaEliminarProducto() throws Exception {
        String operacion = "eliminar";
        return firmaDigitalService.generarFirmaConTiempo(operacion, 300000);
    }

    // Generar firma para buscar
    @GetMapping("/generar-buscar")
    public String generarFirmaBuscarProducto() throws Exception {
        String operacion = "buscar";
        return firmaDigitalService.generarFirmaConTiempo(operacion, 300000);
    }
}