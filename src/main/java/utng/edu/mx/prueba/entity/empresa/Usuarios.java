package utng.edu.mx.prueba.entity.empresa;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@Table(name = "usuarios")
@Entity
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seqUsuario")
    @SequenceGenerator(name = "seqUsuario", sequenceName = "usuario_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private Boolean estatus;

    @Column(name = "role", nullable = false)
    private String role;

}
