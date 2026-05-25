package io.angellsan94.angelatro.logic.model;

import java.util.Objects;

/**
 * Representa una carta de baraja.
 * <p>
 * Una carta se define por su rango y palo.
 * </p>
 * <p>
 * Las cartas delegan en {@link Rank} para obtener el valor en chips.
 * </p>
 * <p>
 * Dos cartas son iguales si tienen el mismo rango y palo.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public record Card(Rank rank, Suit suit) {

    /**
     * Obtiene el valor de la carta en chips.
     * <p>
     * Delega en {@link Rank#getValueInChips()} para obtener el valor.
     * </p>
     *
     * @return el valor de la carta en chips
     */
    public int getRankChips() {
        return rank.getValueInChips();
    }

    /**
     * Obtiene el nombre de la carta.
     * <p>
     * Combina el nombre del rango y el palo para obtener el nombre completo.
     * </p>
     *
     * @return el nombre de la carta
     */
    public String getName() {
        return rank.name() + " de " + suit.name();
    }

    /**
     * Obtiene el valor hash de la carta.
     * <p>
     * Implementación estándar de hashCode para records.
     * </p>
     *
     * @return el hash code de la carta
     */
    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    /**
     * Verifica la igualdad de dos cartas.
     * <p>
     * Dos cartas son iguales si tienen el mismo rango y palo.
     * </p>
     *
     * @param other la otra carta a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Card)) {
            return false;
        }
        Card card = (Card) other;
        return Objects.equals(rank, card.rank) && Objects.equals(suit, card.suit);
    }

    /**
     * Obtiene la representación en cadena de la carta.
     * <p>
     * Devuelve el nombre de la carta.
     * </p>
     *
     * @return la representación en cadena de la carta
     */
    @Override
    public String toString() {
        return getName();
    }
}
