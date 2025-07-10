package ds.evaluacion.dsegovia.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id_usuario", updatable = false, nullable = false)
    private UUID idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @CreationTimestamp
    @Column(name = "creado", nullable = false, updatable = false)
    private LocalDateTime creado;

    @UpdateTimestamp
    @Column(name = "modificado")
    private LocalDateTime modificado;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column
    private String token;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private List<Telefono> telefonos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (ultimoLogin == null) {
            ultimoLogin = LocalDateTime.now();
        }
    }

    public void addTelefono(Telefono telefono) {
        telefono.setUsuario(this);
        this.telefonos.add(telefono);
    }

    public void clearTelefonos() {
        if (telefonos == null) {
            telefonos = new ArrayList<>();
            return;
        }
        telefonos.forEach(t -> t.setUsuario(null));
        telefonos.clear();
    }
}
