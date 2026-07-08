package Model.Exceptions;

/**
 * Se lanza cuando ya se alcanzó el cupo máximo de estudiantes de una materia para un
 * tutor y bloque de horario dados.
 */
public class cupoMaximoException extends RuntimeException {
    /**
     * @param message mensaje descriptivo del error de cupo
     */
    public cupoMaximoException(String message) {
        super(message);
    }
}
