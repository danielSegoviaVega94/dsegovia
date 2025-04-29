package ds.evaluacion.dsegovia.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "telefono")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefono {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTelefono;

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
