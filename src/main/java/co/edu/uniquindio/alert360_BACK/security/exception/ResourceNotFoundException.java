package co.edu.uniquindio.alert360_BACK.security.exception;

/**
 * Excepción personalizada para indicar que un recurso no fue encontrado.
 * Esta excepción se lanza cuando se intenta acceder a un recurso que no existe en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
