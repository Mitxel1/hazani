package com.utng.edu.prueba.entity.empresa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString

@Entity
@Table(name="cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqClientes")
    @SequenceGenerator(name="seqClientes", sequenceName = "clientes_id_seq", allocationSize = 1)
    @Column(name = "id_cliente")
    private Integer id_cliente;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "edad")
    private String edad;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fecha_creacion;

    @PrePersist
    public void prePersist() {
        // Establece la fecha y hora actual antes de guardar el cliente
        this.fecha_creacion = LocalDateTime.now();
    }
}
