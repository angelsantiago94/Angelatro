package io.angellsan94.angelatro.logic.game;

import java.util.EnumMap;
import java.util.Map;

/**
 * Gestiona los niveles de las manos de póker y calcula los chips y multiplicadores.
 * <p>
 * Cada {@link HandType} tiene un nivel que empieza en 0. Al comprar una mejora, sube 1.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class HandLevelManager {

    private final Map<HandType, Integer> levels;

    /**
     * Constructor que inicializa los niveles de todas las manos a 0.
     */
    public HandLevelManager() {
        this.levels = new EnumMap<>(HandType.class);
        for (HandType handType : HandType.values()) {
            levels.put(handType, 0);
        }
    }

    /**
     * Obtiene el nivel actual de una mano.
     *
     * @param handType el tipo de mano
     * @return el nivel actual (mínimo 0)
     */
    public int getLevel(HandType handType) {
        return levels.get(handType);
    }

    /**
     * Mejora el nivel de una mano en 1.
     *
     * @param handType el tipo de mano a mejorar
     */
    public void upgrade(HandType handType) {
        levels.put(handType, levels.get(handType) + 1);
    }

    /**
     * Calcula los chips base para un tipo de mano según su nivel actual.
     *
     * @param handType el tipo de mano
     * @return los chips base
     */
    public int getChips(HandType handType) {
        int level = getLevel(handType);
        return getChips(handType, level);
    }

    /**
     * Calcula los chips base para un tipo de mano en un nivel específico.
     *
     * @param handType el tipo de mano
     * @param level    el nivel
     * @return los chips base
     */
    public int getChips(HandType handType, int level) {
        int initialChips = getInitialChips(handType);
        int incrementChips = getIncrementChips(handType);
        return initialChips + (incrementChips * level);
    }

    /**
     * Calcula el multiplicador base para un tipo de mano según su nivel actual.
     *
     * @param handType el tipo de mano
     * @return el multiplicador base
     */
    public int getMult(HandType handType) {
        int level = getLevel(handType);
        return getMult(handType, level);
    }

    /**
     * Calcula el multiplicador base para un tipo de mano en un nivel específico.
     *
     * @param handType el tipo de mano
     * @param level    el nivel
     * @return el multiplicador base
     */
    public int getMult(HandType handType, int level) {
        int initialMult = getInitialMult(handType);
        int incrementMult = getIncrementMult(handType);
        return initialMult + (incrementMult * level);
    }

    /**
     * Obtiene los chips iniciales para un tipo de mano.
     *
     * @param handType el tipo de mano
     * @return los chips iniciales
     */
    private int getInitialChips(HandType handType) {
        return switch (handType) {
            case CARTA_ALTA -> 5;
            case PAREJA -> 20;
            case DOBLE_PAREJA -> 30;
            case TRIO -> 40;
            case ESCALERA -> 30;
            case COLOR -> 35;
            case FULL_HOUSE -> 40;
            case POKER -> 60;
            case ESCALERA_DE_COLOR -> 100;
            case ESCALERA_REAL -> 100;
            case REPOKER -> 120;
            case FULL_DE_COLOR -> 140;
            case CINCO_DE_COLOR -> 160;
        };
    }

    /**
     * Obtiene el multiplicador inicial para un tipo de mano.
     *
     * @param handType el tipo de mano
     * @return el multiplicador inicial
     */
    private int getInitialMult(HandType handType) {
        return switch (handType) {
            case CARTA_ALTA -> 1;
            case PAREJA -> 2;
            case DOBLE_PAREJA -> 2;
            case TRIO -> 3;
            case ESCALERA -> 4;
            case COLOR -> 4;
            case FULL_HOUSE -> 4;
            case POKER -> 7;
            case ESCALERA_DE_COLOR -> 8;
            case ESCALERA_REAL -> 8;
            case REPOKER -> 12;
            case FULL_DE_COLOR -> 14;
            case CINCO_DE_COLOR -> 16;
        };
    }

    /**
     * Obtiene el incremento de chips por nivel para un tipo de mano.
     *
     * @param handType el tipo de mano
     * @return el incremento de chips
     */
    private int getIncrementChips(HandType handType) {
        return switch (handType) {
            case CARTA_ALTA -> 10;
            case PAREJA -> 15;
            case DOBLE_PAREJA -> 20;
            case TRIO -> 20;
            case ESCALERA -> 30;
            case COLOR -> 30;
            case FULL_HOUSE -> 25;
            case POKER -> 30;
            case ESCALERA_DE_COLOR -> 40;
            case ESCALERA_REAL -> 40;
            case REPOKER -> 35;
            case FULL_DE_COLOR -> 40;
            case CINCO_DE_COLOR -> 50;
        };
    }

    /**
     * Obtiene el incremento de multiplicador por nivel para un tipo de mano.
     *
     * @param handType el tipo de mano
     * @return el incremento de multiplicador
     */
    private int getIncrementMult(HandType handType) {
        return switch (handType) {
            case CARTA_ALTA -> 1;
            case PAREJA -> 1;
            case DOBLE_PAREJA -> 1;
            case TRIO -> 2;
            case ESCALERA -> 3;
            case COLOR -> 3;
            case FULL_HOUSE -> 2;
            case POKER -> 3;
            case ESCALERA_DE_COLOR -> 4;
            case ESCALERA_REAL -> 4;
            case REPOKER -> 3;
            case FULL_DE_COLOR -> 4;
            case CINCO_DE_COLOR -> 3;
        };
    }
}
