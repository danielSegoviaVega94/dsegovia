package ds.evaluacion.dsegovia.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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