package io.angellsan94.angelatro.exceptions;

/**
 * Excepción lanzada cuando se intenta realizar una operación sobre una baraja vacía.
 *
 * @author Ángel San94
 * @version 1.0
 * @since 0.1
 */
public class DeckEmptyException extends RuntimeException {

    /**
     * Constructor por defecto con mensaje de error predeterminado.
     */
    public DeckEmptyException() {
        super("La baraja está vacía. No se pueden realizar operaciones.");
    }

    /**
     * Constructor que acepta un mensaje de error personalizado.
     *
     * @param message mensaje de error personalizado
     */
    public DeckEmptyException(String message) {
        super(message);
    }

    /**
     * Constructor que acepta una causa raíz.
     *
     * @param cause causa de la excepción
     */
    public DeckEmptyException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor que acepta un mensaje de error y una causa raíz.
     *
     * @param message mensaje de error personalizado
     * @param cause causa de la excepción
     */
    public DeckEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}