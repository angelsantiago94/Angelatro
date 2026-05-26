package io.angellsan94.angelatro.logic.economy;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;
import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.JokerEffect;
import io.angellsan94.angelatro.logic.jokers.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generador de contenido para la tienda.
 * <p>
 * Genera jokers y mejoras de mano disponibles para compra.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class ShopGenerator {

    private static final int JOKERS_PER_VISIT = 2;
    private static final int HAND_UPGRADES_PER_VISIT = 2;
    private static final int HAND_UPGRADE_PRICE = 6;
    private static final Random random = new Random();

    /**
     * Genera una lista de jokers disponibles para compra.
     *
     * @return lista de jokers generados
     */
    public List<Joker> generateJokers() {
        List<Joker> jokers = new ArrayList<>();

        for (int i = 0; i < JOKERS_PER_VISIT; i++) {
            Rarity rarity = generateRandomRarity();
            Joker joker = createJokerForRarity(rarity, i);
            jokers.add(joker);
        }

        return jokers;
    }

    /**
     * Genera una rareza aleatoria según las probabilidades definidas.
     *
     * @return la rareza generada
     */
    private Rarity generateRandomRarity() {
        int roll = random.nextInt(100);

        if (roll < 70) {
            return Rarity.COMMON;
        } else if (roll < 95) {
            return Rarity.UNCOMMON;
        } else {
            return Rarity.RARE;
        }
    }

    /**
     * Crea un joker con efecto dummy para la rareza especificada.
     *
     * @param rarity la rareza del joker
     * @param index  índice para generar IDs únicos
     * @return el joker creado
     */
    private Joker createJokerForRarity(Rarity rarity, int index) {
        JokerEffect dummyEffect = new JokerEffect() {
            @Override
            public int modifyChips(int currentChips, HandEvaluationContext context) {
                return currentChips;
            }

            @Override
            public int modifyMult(int currentMult, HandEvaluationContext context) {
                return currentMult;
            }
        };

        return new Joker(
                "SHOP_" + rarity.name() + "_" + index,
                "Joker " + rarity.name() + " " + index,
                "Descripción del joker " + rarity.name(),
                rarity.getPrice(),
                rarity,
                dummyEffect
        );
    }

    /**
     * Genera una lista de mejoras de mano disponibles para compra.
     *
     * @return lista de tipos de mano mejorables
     */
    public List<HandType> generateHandUpgrades() {
        List<HandType> upgrades = new ArrayList<>();
        HandType[] allHandTypes = HandType.values();

        for (int i = 0; i < HAND_UPGRADES_PER_VISIT; i++) {
            int randomIndex = random.nextInt(allHandTypes.length);
            upgrades.add(allHandTypes[randomIndex]);
        }

        return upgrades;
    }

    /**
     * Obtiene el precio de una mejora de mano.
     *
     * @return el precio en monedas
     */
    public int getHandUpgradePrice() {
        return HAND_UPGRADE_PRICE;
    }
}
