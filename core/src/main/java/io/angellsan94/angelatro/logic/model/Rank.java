package io.angellsan94.angelatro.logic.model;

import java.util.Arrays;
import java.util.List;

/**
 * Representa los rangos de las cartas de baraja.
 * <p>
 * Cada rango tiene un valor en chips asociado.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public enum Rank {

    /**
     * As
     */
    AS(11),

    /**
     * Rey
     */
    REY(10),

    /**
     * Reina
     */
    REINA(10),

    /**
     * Sota
     */
    JOTA(10),

    /**
     * 10
     */
    DIEZ(10),

    /**
     * 9
     */
    NUEVE(9),

    /**
     * 8
     */
    OCHO(8),

    /**
     * 7
     */
    SIETE(7),

    /**
     * 6
     */
    SEIS(6),

    /**
     * 5
     */
    CINCO(5),

    /**
     * 4
     */
    CUATRO(4),

    /**
     * 3
     */
    TRES(3),

    /**
     * 2
     */
    DOS(2);

    /**
     * Lista de todos los rangos ordenados por valor.
     */
    private static final List<Rank> RANKS_BY_VALUE = Arrays.stream(values())
            .sorted((a, b) -> Integer.compare(b.value, a.value))
            .toList();

    /**
     * Valor en chips del rango.
     */
    private final int value;

    /**
     * Constructor de enum.
     *
     * @param value el valor en chips
     */
    Rank(int value) {
        this.value = value;
    }

    /**
     * Obtiene el valor en chips del rango.
     *
     * @return el valor en chips
     */
    public int getValueInChips() {
        return value;
    }

    /**
     * Obtiene el rango con el mayor valor.
     *
     * @return el rango con el mayor valor
     */
    public static Rank getHighestRank() {
        return RANKS_BY_VALUE.get(0);
    }

    /**
     * Obtiene el rango con el menor valor.
     *
     * @return el rango con el menor valor
     */
    public static Rank getLowestRank() {
        return RANKS_BY_VALUE.get(RANKS_BY_VALUE.size() - 1);
    }

    /**
     * Obtiene el rango con el valor especificado.
     *
     * @param chips el valor en chips
     * @return el rango con el valor especificado, o null si no existe
     */
    public static Rank findByValueInChips(int chips) {
        return Arrays.stream(values())
                .filter(rank -> rank.value == chips)
                .findFirst()
                .orElse(null);
    }
}
