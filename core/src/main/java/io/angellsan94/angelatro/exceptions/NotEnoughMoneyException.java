package io.angellsan94.angelatro.exceptions;

/**
 * Excepción lanzada cuando se intenta gastar más dinero del disponible en el monedero.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class NotEnoughMoneyException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message el mensaje de error
     */
    public NotEnoughMoneyException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message el mensaje de error
     * @param cause   la causa de la excepción
     */
    public NotEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}
