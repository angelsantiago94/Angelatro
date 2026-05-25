package io.angellsan94.angelatro.logic.model;

/**
 * Enum que representa los palos de una carta.
 */
public enum Suit {

    /**
     * Palo de corazones.
     */
    HEARTS,

    /**
     * Palo de diamantes.
     */
    DIAMONDS,

    /**
     * Palo de tréboles.
     */
    CLUBS,

    /**
     * Palo de picas.
     */
    SPADES;

    /**
     * Obtiene el nombre del palo en minúsculas.
     *
     * @return nombre del palo en minúsculas
     */
    public String getName() {
        return name().toLowerCase();
    }
}