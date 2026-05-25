package io.angellsan94.angelatro.logic.game;

/**
 * Enum que representa los tipos de manos en el póker.
 * <p>
 * Los tipos de manos están ordenados de menor a mayor valor.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public enum HandType {

    /**
     * Carta Alta - Ninguna combinación, solo la carta más alta.
     */
    CARTA_ALTA,

    /**
     * Pareja - Dos cartas del mismo rango.
     */
    PAREJA,

    /**
     * Doble Pareja - Dos pares distintos.
     */
    DOBLE_PAREJA,

    /**
     * Trío - Tres cartas del mismo rango.
     */
    TRIO,

    /**
     * Escalera - Cinco cartas con rangos consecutivos de distintos palos.
     */
    ESCALERA,

    /**
     * Color - Cinco cartas del mismo palo sin formar escalera.
     */
    COLOR,

    /**
     * Full House - Un trío y una pareja.
     */
    FULL_HOUSE,

    /**
     * Póker - Cuatro cartas del mismo rango.
     */
    POKER,

    /**
     * Escalera de Color - Cinco cartas consecutivas del mismo palo (no 10-J-Q-K-A).
     */
    ESCALERA_DE_COLOR,

    /**
     * Escalera Real - 10-J-Q-K-A del mismo palo.
     */
    ESCALERA_REAL,

    /**
     * Repoker - Cinco cartas del mismo rango.
     */
    REPOKER,

    /**
     * Full de Color - Trío del mismo palo + pareja del mismo palo.
     */
    FULL_DE_COLOR,

    /**
     * Cinco de color - Cinco cartas del mismo rango y palo.
     */
    CINCO_DE_COLOR;

    /**
     * Obtiene el valor numérico del tipo de mano para comparación.
     * <p>
     * Cuanto mayor sea el valor, mejor es la mano.
     * </p>
     *
     * @return el valor numérico del tipo de mano
     */
    public int getValue() {
        return ordinal();
    }
}
