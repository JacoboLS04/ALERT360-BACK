package co.edu.uniquindio.alert360_BACK.security.exception;

/**
 * Excepción personalizada para manejar errores de tiempo de espera en operaciones.
 * Esta excepción se lanza cuando una operación tarda demasiado tiempo en completarse.
 */
public class OperationTimeoutException extends RuntimeException {
    private final long timeoutMillis;

    public OperationTimeoutException(String message, long timeoutMillis) {
        super(message);
        this.timeoutMillis = timeoutMillis;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }
}