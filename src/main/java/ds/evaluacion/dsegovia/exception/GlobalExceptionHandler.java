package ds.evaluacion.dsegovia.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private HttpHeaders createUtf8Headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (errors.size() == 1) {
            return ResponseEntity
                    .badRequest()
                    .headers(createUtf8Headers())
                    .body(Map.of("mensaje", errors.values().iterator().next()));
        }

        return ResponseEntity
                .badRequest()
                .headers(createUtf8Headers())
                .body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensaje;

        if (ex.getRequiredType() != null && ex.getRequiredType().equals(UUID.class)) {
            mensaje = String.format("ID inválido: '%s' no es un UUID válido", ex.getValue());
        } else {
            mensaje = String.format("Valor inválido para el parámetro '%s': %s", ex.getName(), ex.getValue());
        }

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("campo", ex.getName());
        response.put("valorRecibido", String.valueOf(ex.getValue()));

        return ResponseEntity
                .badRequest()
                .headers(createUtf8Headers())
                .body(response);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioNoEncontradoException(UsuarioNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(createUtf8Headers())
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(EmailYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleEmailYaExisteException(EmailYaExisteException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(createUtf8Headers())
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(createUtf8Headers())
                .body(Map.of("mensaje", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(createUtf8Headers())
                .body(Map.of("mensaje", "Error interno del servidor"));
    }
}