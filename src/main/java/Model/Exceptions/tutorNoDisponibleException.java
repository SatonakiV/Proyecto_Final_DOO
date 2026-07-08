package Model.Exceptions;

/**
 * Se lanza cuando se intenta agendar una clase con un tutor que no tiene disponibilidad
 * en el día y horario pedidos.
 */
public class tutorNoDisponibleException extends RuntimeException {
    /**
     * @param message mensaje descriptivo de la no disponibilidad del tutor
     */
    public tutorNoDisponibleException(String message) {
        super(message);
    }
}
