package ds.evaluacion.dsegovia.exception;

public class EmailYaExisteException extends RuntimeException {
    public EmailYaExisteException(String mensaje) {
        super(mensaje);
    }
}