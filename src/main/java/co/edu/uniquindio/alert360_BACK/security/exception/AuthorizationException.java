package co.edu.uniquindio.alert360_BACK.security.exception;

/**
 * Excepción personalizada para manejar errores de autorización.
 * Esta excepción se lanza cuando un usuario intenta realizar una acción para la cual no tiene permisos.
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}