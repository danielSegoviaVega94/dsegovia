package ds.evaluacion.dsegovia.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "telefono")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefono {


    @Id
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id_telefono", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID idTelefono;
    @Column(nullable = false)
    private String numero;

    @Column(name = "codigo_ciudad", nullable = false)
    private String codigoCiudad;

    @Column(name = "codigo_pais", nullable = false)
    private String codigoPais;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
