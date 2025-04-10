package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.model.productos.*;
import com.utng.edu.prueba.service.GenerarFirmaProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

@Component(value = "generarFirmaProducto")
@Slf4j
public class GenerarFirmaProducto implements GenerarFirmaProductoService {

    @Value("${app.crypto.validafirma.file}")
    private String privateKeyFile;

    @Value("${app.crypto.validafirma.password}")
    private String privateKeyPassword;

    // Generación de la firma para productos
    private String generarFirma(String cadenaOriginal, String empresa) {
        try {
            PrivateKey privateKey = obtieneLlavePrivada(empresa);
            if (privateKey == null) {
                log.warn("No se encontró la llave privada para la empresa: {}", empresa);
                return null;
            }

            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(cadenaOriginal.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = sign.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            log.error("Error al generar la firma para productos: {}", e.getMessage());
            return null;
        }
    }

    // Obtención de la clave privada desde el archivo keystore
    private PrivateKey obtieneLlavePrivada(String empresa) {
        try (FileInputStream fileInputStream = new FileInputStream(privateKeyFile)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fileInputStream, privateKeyPassword.toCharArray());
            return (PrivateKey) keyStore.getKey(empresa, privateKeyPassword.toCharArray());
        } catch (Exception e) {
            log.error("Error al obtener la llave privada para productos: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String generarFirmaAltaProducto(ProductoRequest productoRequest) {
        String cadenaOriginal = "||" + productoRequest.getNombre() + "|"
                + productoRequest.getDescripcion() + "|"
                + productoRequest.getPrecio() + "|"
                + productoRequest.getCantidad() + "|"
                + productoRequest.getCategoria() + "||";
        return generarFirma(cadenaOriginal, "productos");
    }

    @Override
    public String generarFirmaActualizarProducto(ProductoUpdateRequest productoUpdateRequest) {
        String cadenaOriginal = "||" + productoUpdateRequest.getNombreOriginal() + "|"
                + productoUpdateRequest.getNuevoNombre() + "|"
                + productoUpdateRequest.getDescripcion() + "|"
                + productoUpdateRequest.getPrecio() + "|"
                + productoUpdateRequest.getCantidad() + "|"
                + productoUpdateRequest.getCategoria() + "||";
        return generarFirma(cadenaOriginal, "productos");
    }

    @Override
    public String generarFirmaBajaProducto(ProductoDeleteRequest productoDeleteRequest) {
        String cadenaOriginal = "||" + productoDeleteRequest.getNombre() + "||";
        return generarFirma(cadenaOriginal, "productos");
    }

    @Override
    public String generarFirmaListarProducto(ProductoListRequest productoListRequest) {
        String cadenaOriginal = "||" + productoListRequest.getTamañoPagina() + "|"
                + productoListRequest.getPagina() + "||";
        return generarFirma(cadenaOriginal, "productos");
    }
}
