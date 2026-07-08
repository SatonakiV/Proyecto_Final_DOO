package Model.Exceptions;

/**
 * Se lanza cuando un estudiante intenta reservar una clase que choca en el tiempo con
 * otra reserva activa que ya tiene.
 */
public class conflictoHorarioException extends RuntimeException {
    /**
     * @param message mensaje descriptivo del conflicto de horario
     */
    public conflictoHorarioException(String message) {
        super(message);
    }
}
