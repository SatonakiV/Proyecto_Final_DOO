package Model.Exceptions;

/**
 * Se lanza cuando faltan datos obligatorios o un dato no cumple una regla básica de
 * validez (por ejemplo, un valor negativo donde se espera uno positivo).
 */
public class datosInvalidosException extends RuntimeException {
    /**
     * @param message mensaje descriptivo del dato inválido
     */
    public datosInvalidosException(String message) {
        super(message);
    }
}
