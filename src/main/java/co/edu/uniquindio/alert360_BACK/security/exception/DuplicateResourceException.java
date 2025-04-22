package co.edu.uniquindio.alert360_BACK.security.exception;

/**
 * Excepción personalizada para manejar errores de recursos duplicados.
 * Esta excepción se lanza cuando se intenta crear o actualizar un recurso
 * que ya existe en el sistema.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
