package io.angellsan94.angelatro.logic.model;

import lombok.Getter;

import java.util.Optional;

/**
 * Enum que encapsula la configuración inicial de una partida.
 * Es la única fuente de verdad para los valores de arranque; ninguna otra clase
 * debe tener esos números hardcodeados.
 *
 * @since 1.0
 */

//TODO añadir los links de javadoc
@Getter
public enum DeckType {

    /**
     * Configuración estándar de la partida.
     * - Saldo inicial: 4
     * - Bonus chips: 0
     * - Bonus mult: 0
     */
    STANDARD(4, 0, 0, "STANDARD"),

    /**
     * Configuración para partidas con mayor riqueza inicial.
     * - Saldo inicial: 8
     * - Bonus chips: 0
     * - Bonus mult: 0
     */
    WEALTHY(8, 0, 0, "WEALTHY"),

    /**
     * Configuración para partidas con bonus de chips elevado.
     * - Saldo inicial: 2
     * - Bonus chips: 10
     * - Bonus mult: 0
     */
    POWERED(2, 10, 0, "POWERED"),

    /**
     * Configuración para partidas con bonus de multiplicador elevado.
     * - Saldo inicial: 4
     * - Bonus chips: 0
     * - Bonus mult: 2
     */
    MULTIBASE(4, 0, 2, "MULTIBASE");

    /**
     * Saldo inicial de {Wallet}.
     */
    private final int initialMoney;

    /**
     * Chips extra que {ScoreEngine} suma a cada mano durante toda la partida.
     */
    private final int bonusChips;

    /**
     * Mult extra que {ScoreEngine} suma a cada mano durante toda la partida.
     */
    private final int bonusMult;

    /**
     * Identificador de cadena para persistencia en JSON.
     */
    private final String id;

    /**
     * Constructor de enum.
     *
     * @param initialMoney saldo inicial de {Wallet}
     * @param bonusChips chips extra para {ScoreEngine}
     * @param bonusMult mult extra para {ScoreEngine}
     * @param id identificador de cadena único
     */
    DeckType(final int initialMoney, final int bonusChips, final int bonusMult, final String id) {
        this.initialMoney = initialMoney;
        this.bonusChips = bonusChips;
        this.bonusMult = bonusMult;
        this.id = id;
    }

    /**
     * Obtiene el {@link DeckType} correspondiente a un identificador de cadena.
     *
     * @param id identificador de cadena (ej. "STANDARD")
     * @return {@link DeckType} correspondiente
     * @throws IllegalArgumentException si el identificador no existe
     */
    public static DeckType fromId(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("El identificador no puede ser null");
        }
        for (DeckType deckType : DeckType.values()) {
            if (deckType.id.equalsIgnoreCase(id)) {
                return deckType;
            }
        }
        throw new IllegalArgumentException("No existe un DeckType con el identificador: " + id);
    }

    /**
     * Obtiene un {@link Optional} con el {@link DeckType} correspondiente a un identificador.
     *
     * @param id identificador de cadena (ej. "STANDARD")
     * @return {@link Optional} con el {@link DeckType} o {@link Optional#empty()} si no existe
     */
    public static Optional<DeckType> fromIdOptional(final String id) {
        if (id == null) {
            return Optional.empty();
        }
        for (DeckType deckType : DeckType.values()) {
            if (deckType.id.equalsIgnoreCase(id)) {
                return Optional.of(deckType);
            }
        }
        return Optional.empty();
    }
}
