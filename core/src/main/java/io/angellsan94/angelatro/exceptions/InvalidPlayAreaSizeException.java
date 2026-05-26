package io.angellsan94.angelatro.exceptions;

/**
 * Excepción lanzada cuando el PlayArea tiene un tamaño inválido.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class InvalidPlayAreaSizeException extends RuntimeException {

    /**
     * Constructor con mensaje de error.
     *
     * @param message el mensaje de error
     */
    public InvalidPlayAreaSizeException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message el mensaje de error
     * @param cause   la causa de la excepción
     */
    public InvalidPlayAreaSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
