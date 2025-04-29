package ds.evaluacion.dsegovia.dto;
import ds.evaluacion.dsegovia.constantes.ValidacionConstantes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoDTO {

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(
            regexp = ValidacionConstantes.TELEFONOS_REGEX,
            message = ValidacionConstantes.TELEFONOS_REGEX_MENSAJE
    )
    private String numero;

    @NotBlank(message = "El código de ciudad es obligatorio")
    private String codigoCiudad;

    @NotBlank(message = "El código de país es obligatorio")
    private String codigoPais;
}
