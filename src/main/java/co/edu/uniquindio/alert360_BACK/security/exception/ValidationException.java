package co.edu.uniquindio.alert360_BACK.security.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Excepción personalizada para manejar errores de validación de datos.
 * Esta excepción permite almacenar múltiples errores de validación en un mapa.
 */
public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public void addError(String field, String message) {
        this.errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}