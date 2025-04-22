package co.edu.uniquindio.alert360_BACK.security.exception;

/**
 * Excepción personalizada para manejar errores de formato inválido en datos.
 * Esta excepción se lanza cuando los datos proporcionados no cumplen con el formato esperado.
 */
public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String message) {
        super(message);
    }
}