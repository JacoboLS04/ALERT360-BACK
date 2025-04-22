package co.edu.uniquindio.alert360_BACK.security.exception;

/**
 * Excepción personalizada para manejar errores en servicios externos.
 * Esta excepción se lanza cuando ocurre un problema al comunicarse con un servicio externo.
 */
public class ExternalServiceException extends RuntimeException {
    private final String serviceName;

    public ExternalServiceException(String message, String serviceName) {
        super(message);
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}