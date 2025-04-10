package com.utng.edu.prueba.entity.empresa;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString

@Entity
@Table(name = "productos")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqProducto")
    @SequenceGenerator(name = "seqProducto", sequenceName = "producto_id_seq", allocationSize = 1)
    @Column(name = "id_product")
    private Integer id_product;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private BigDecimal precio;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;
}