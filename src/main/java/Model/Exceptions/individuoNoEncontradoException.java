package Model.Exceptions;

/**
 * Se lanza cuando se busca un tutor o estudiante por su id y no existe ninguno con ese id.
 */
public class individuoNoEncontradoException extends RuntimeException {
    /**
     * @param message mensaje descriptivo de a quién no se encontró
     */
    public individuoNoEncontradoException(String message) {
        super(message);
    }
}
