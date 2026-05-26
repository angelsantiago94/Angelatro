package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.exceptions.HandLimitExceededException;
import io.angellsan94.angelatro.logic.model.Card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Gestiona la mano del jugador, permitiendo añadir cartas y seleccionar/deseleccionar
 * cartas para jugar o descartar.
 * <p>
 * Tiene una capacidad máxima de 8 cartas.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class PlayerHand {

    private static final int MAX_CAPACITY = 8;
    private static final int MAX_PLAY_AREA_SIZE = 5;

    private final List<Card> cards;
    private final Set<Card> selectedCards;

    /**
     * Constructor que inicializa la mano vacía.
     */
    public PlayerHand() {
        this.cards = new ArrayList<>();
        this.selectedCards = new HashSet<>();
    }

    /**
     * Añade una carta a la mano.
     *
     * @param card la carta a añadir
     * @throws IllegalStateException si la mano está llena (8 cartas)
     */
    public void addCard(Card card) {
        if (cards.size() >= MAX_CAPACITY) {
            throw new IllegalStateException("La mano está llena (máximo 8 cartas)");
        }
        cards.add(card);
    }

    /**
     * Marca una carta como seleccionada para jugar o descartar.
     *
     * @param card la carta a seleccionar
     * @throws HandLimitExceededException si se intentan seleccionar más de 5 cartas
     * @throws IllegalArgumentException si la carta no está en la mano
     */
    public void select(Card card) {
        if (!cards.contains(card)) {
            throw new IllegalArgumentException("La carta no está en la mano");
        }
        if (selectedCards.size() >= MAX_PLAY_AREA_SIZE && !selectedCards.contains(card)) {
            throw new HandLimitExceededException("No se pueden seleccionar más de 5 cartas para el PlayArea");
        }
        selectedCards.add(card);
    }

    /**
     * Desmarca una carta seleccionada.
     *
     * @param card la carta a desmarcar
     * @throws IllegalArgumentException si la carta no está en la mano
     */
    public void deselect(Card card) {
        if (!cards.contains(card)) {
            throw new IllegalArgumentException("La carta no está en la mano");
        }
        selectedCards.remove(card);
    }

    /**
     * Obtiene las cartas seleccionadas.
     *
     * @return lista de cartas seleccionadas
     */
    public List<Card> getSelected() {
        return new ArrayList<>(selectedCards);
    }

    /**
     * Obtiene el número de cartas en la mano.
     *
     * @return número de cartas
     */
    public int size() {
        return cards.size();
    }

    /**
     * Obtiene todas las cartas de la mano.
     *
     * @return lista de todas las cartas
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
}
