package com.utng.edu.prueba.service.impl;

import com.utng.edu.prueba.model.productos.*;
import com.utng.edu.prueba.service.CryptoServicesProductos;
import jakarta.validation.constraints.DecimalMin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

@Component(value = "cryptoservicesproductosImpl")
@Slf4j
public class CryptoServiceProductosImpl implements CryptoServicesProductos {

    @Value("${app.crypto.validafirma.file}")
    private String publicKeyFile;

    @Value("${app.crypto.validafirma.password}")
    private String publicKeyPassword;

    // Método para validar la firma de una cadena original con la firma proporcionada
    private boolean validaCadena(String cadenaOriginal, String firma, String empresa) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            PublicKey publicKey = obtieneLlavePublica(empresa); // Obtén la clave pública de la empresa
            if (publicKey == null) {
                log.warn("No se encontró la llave pública para la empresa: {}", empresa);
                return false;
            }
            sign.initVerify(publicKey); // Inicializa la firma para verificar
            sign.update(cadenaOriginal.getBytes(StandardCharsets.UTF_8)); // Actualiza con la cadena original
            return sign.verify(Base64.getDecoder().decode(firma)); // Verifica la firma
        } catch (Exception e) {
            log.error("Error al validar la cadena original para la empresa: {}", empresa, e);
            return false;
        }
    }

    // Obtención de la clave pública desde el archivo keystore
    private PublicKey obtieneLlavePublica(String empresa) {
        try (FileInputStream fileInputStream = new FileInputStream(publicKeyFile)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(fileInputStream, publicKeyPassword.toCharArray());
            Certificate certificate = keyStore.getCertificate(empresa); // Obtiene el certificado de la empresa
            if (certificate == null) {
                certificate = keyStore.getCertificate("service"); // Certificado por defecto
            }
            return certificate.getPublicKey(); // Extrae la clave pública
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            log.error("Error al obtener la llave pública para la empresa: {}", empresa, e);
            return null;
        }
    }

    // Método auxiliar para validar que el request y la firma no sean nulos
    private boolean validarRequestYFirma(Object request, String firma) {
        if (request == null || firma == null || firma.isEmpty()) {
            log.warn("El request o la firma es nula o vacía");
            return false;
        }
        return true;
    }

    // Método auxiliar para construir la cadena original de productos
    private String construirCadenaOriginalProducto(
            String nombreOriginal,
            String nuevoNombre,
            String descripcion,
            BigDecimal precio,
            Integer cantidad,
            String categoria) {
        return String.format("||%s|%s|%s|%s|%s|%s||",
                nombreOriginal,
                nuevoNombre != null ? nuevoNombre : "", // Manejo de nuevoNombre opcional
                descripcion,
                precio,
                cantidad,
                categoria);
    }

    // Validación de firma para alta de producto
    @Override
    public Boolean validacionCadenaOriginalAltaProducto(ProductoRequest productoRequest) {
        if (!validarRequestYFirma(productoRequest, productoRequest.getFirma())) {
            return false;
        }
        String cadenaOriginal = construirCadenaOriginalProducto(
                productoRequest.getNombre(),
                null, // No hay nuevoNombre en el alta
                productoRequest.getDescripcion(),
                productoRequest.getPrecio(),
                productoRequest.getCantidad(),
                productoRequest.getCategoria()
        );
        return validaCadena(cadenaOriginal, productoRequest.getFirma(), "service");
    }

    // Validación de firma para actualización de producto
    @Override
    public Boolean validacionCadenaOriginalActualizarProducto(ProductoUpdateRequest productoUpdateRequest) {
        if (!validarRequestYFirma(productoUpdateRequest, productoUpdateRequest.getFirma())) {
            return false;
        }
        String cadenaOriginal = construirCadenaOriginalProducto(
                productoUpdateRequest.getNombreOriginal(),
                productoUpdateRequest.getNuevoNombre(), // Incluir nuevoNombre
                productoUpdateRequest.getDescripcion(),
                productoUpdateRequest.getPrecio(),
                productoUpdateRequest.getCantidad(),
                productoUpdateRequest.getCategoria()
        );
        return validaCadena(cadenaOriginal, productoUpdateRequest.getFirma(), "service");
    }

    // Validación de firma para baja de producto
    @Override
    public Boolean validacionCadenaOriginalBajaProducto(ProductoDeleteRequest productoDeleteRequest) {
        if (!validarRequestYFirma(productoDeleteRequest, productoDeleteRequest.getFirma())) {
            return false;
        }
        String cadenaOriginal = String.format("||%s||", productoDeleteRequest.getNombre());
        return validaCadena(cadenaOriginal, productoDeleteRequest.getFirma(), "service");
    }

    // Validación de firma para listar productos
    @Override
    public Boolean validacionCadenaOriginalListarProducto(ProductoListRequest productosListRequest) {
        if (!validarRequestYFirma(productosListRequest, productosListRequest.getFirma())) {
            return false;
        }
        String cadenaOriginal = String.format("||%s|%s||",
                productosListRequest.getTamañoPagina(),
                productosListRequest.getPagina());
        return validaCadena(cadenaOriginal, productosListRequest.getFirma(), "service");
    }
}