package io.angellsan94.angelatro.logic.model;

import io.angellsan94.angelatro.exceptions.DeckEmptyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Representa un mazo de cartas para el juego Angelatro.
 * Contiene 52 cartas estándar y proporciona métodos para barajar y sacar cartas.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class Deck {

    private final List<Card> cards;
    private final Random random;

    /**
     * Crea un nuevo mazo con todas las cartas estándar.
     */
    public Deck() {
        this.cards = new ArrayList<>();
        this.random = new Random();
        initializeDeck();
    }

    /**
     * Inicializa el mazo con todas las cartas estándar.
     */
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Baraja el mazo aleatoriamente.
     */
    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    /**
     * Saca una carta del mazo.
     *
     * @return La carta sacada del mazo
     * @throws DeckEmptyException Si el mazo está vacío
     */
    public Card draw() {
        if (cards.isEmpty()) {
            throw new DeckEmptyException("No quedan cartas en el mazo");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Obtiene una copia de la lista de cartas del mazo.
     *
     * @return Una copia de la lista de cartas
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Verifica si el mazo está vacío.
     *
     * @return true si el mazo está vacío, false en caso contrario
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Obtiene el número de cartas en el mazo.
     *
     * @return El número de cartas en el mazo
     */
    public int size() {
        return cards.size();
    }
}
