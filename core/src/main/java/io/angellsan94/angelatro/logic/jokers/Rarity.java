package io.angellsan94.angelatro.logic.jokers;

import lombok.Getter;

/**
 * Enum que representa la rareza de un Joker.
 * <p>
 * Cada rareza tiene una probabilidad de aparición en la tienda y un precio base.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum Rarity {

    /**
     * Rareza común.
     * Probabilidad: 70%
     * Precio: 4 monedas
     */
    COMMON(70, 4, "COMMON"),

    /**
     * Rareza poco común.
     * Probabilidad: 25%
     * Precio: 6 monedas
     */
    UNCOMMON(25, 6, "UNCOMMON"),

    /**
     * Rareza rara.
     * Probabilidad: 5%
     * Precio: 8 monedas
     */
    RARE(5, 8, "RARE");

    /**
     * Probabilidad de aparición en la tienda (en porcentaje).
     */
    private final int probability;

    /**
     * Precio base del joker.
     */
    private final int price;

    /**
     * Identificador de cadena para persistencia.
     */
    private final String id;

    /**
     * Constructor del enum.
     *
     * @param probability la probabilidad de aparición
     * @param price       el precio base
     * @param id          el identificador de cadena
     */
    Rarity(int probability, int price, String id) {
        this.probability = probability;
        this.price = price;
        this.id = id;
    }

    /**
     * Obtiene la rareza correspondiente a un identificador de cadena.
     *
     * @param id el identificador de cadena
     * @return la rareza correspondiente
     * @throws IllegalArgumentException si el identificador no existe
     */
    public static Rarity fromId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("El identificador no puede ser null");
        }
        for (Rarity rarity : Rarity.values()) {
            if (rarity.id.equalsIgnoreCase(id)) {
                return rarity;
            }
        }
        throw new IllegalArgumentException("No existe una Rarity con el identificador: " + id);
    }
}
