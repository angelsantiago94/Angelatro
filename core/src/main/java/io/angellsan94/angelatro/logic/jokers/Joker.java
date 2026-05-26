package io.angellsan94.angelatro.logic.jokers;

import lombok.Getter;

/**
 * Representa un Joker que modifica la puntuación según su efecto.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class Joker {

    private final String id;
    private final String name;
    private final String description;
    private final int price;
    private final Rarity rarity;
    private final JokerEffect effect;

    /**
     * Constructor de Joker.
     *
     * @param id          el identificador único del joker
     * @param name        el nombre del joker
     * @param description la descripción del efecto del joker
     * @param price       el precio del joker
     * @param rarity      la rareza del joker
     * @param effect      el efecto del joker
     */
    public Joker(String id, String name, String description, int price, Rarity rarity, JokerEffect effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rarity = rarity;
        this.effect = effect;
    }
}
