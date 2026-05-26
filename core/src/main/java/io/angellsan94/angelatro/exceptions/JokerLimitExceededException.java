package io.angellsan94.angelatro.exceptions;

/**
 * Excepción lanzada cuando se intenta añadir más jokers del límite permitido.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class JokerLimitExceededException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message el mensaje de error
     */
    public JokerLimitExceededException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message el mensaje de error
     * @param cause   la causa de la excepción
     */
    public JokerLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
