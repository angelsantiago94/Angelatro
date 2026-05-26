package io.angellsan94.angelatro.exceptions;

/**
 * Excepción lanzada cuando se intenta jugar más de 5 cartas en el PlayArea.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class HandLimitExceededException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message el mensaje de error
     */
    public HandLimitExceededException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message el mensaje de error
     * @param cause   la causa de la excepción
     */
    public HandLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
