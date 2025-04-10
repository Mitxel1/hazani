package com.utng.edu.prueba.entity.empresa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUsuarios")
    @SequenceGenerator(name = "seqUsuarios", sequenceName = "usuarios_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "correo", nullable = false)
    private String correo; // Ya no es único

    @Column(name = "rol", nullable = false)
    private String rol; // Puede ser "admin", "usuario", etc.

    @Column(name = "estatus")
    private boolean estatus;

    // Nuevo campo para la autenticación de doble paso
    @Column(name = "doble_paso_activado")
    private boolean doblePasoActivado = true; // Campo para saber si la autenticación está activada
}
