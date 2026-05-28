package io.angellsan94.angelatro.logic.economy;

import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.JokerRepository;
import io.angellsan94.angelatro.logic.jokers.Rarity;
import io.angellsan94.angelatro.logic.persistence.UnlockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Generador de contenido para la tienda.
 * <p>
 * Genera jokers y mejoras de mano disponibles para compra.
 * Los jokers se seleccionan del pool de jokers desbloqueados según {@link UnlockService}.
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

    private final UnlockService unlockService;

    /**
     * Constructor que inicializa el generador con el servicio de desbloqueos.
     *
     * @param unlockService el servicio de desbloqueos
     */
    public ShopGenerator(UnlockService unlockService) {
        this.unlockService = unlockService;
    }

    /**
     * Constructor por defecto para compatibilidad (usa UnlockService vacío).
     */
    public ShopGenerator() {
        this.unlockService = new UnlockService();
    }

    /**
     * Genera una lista de jokers disponibles para compra.
     * <p>
     * Selecciona jokers del pool de jokers desbloqueados según las probabilidades de rareza.
     * Un joker ya activo puede aparecer en la tienda (no se filtra el pool).
     * </p>
     *
     * @return lista de jokers generados
     */
    public List<Joker> generateJokers() {
        List<Joker> jokers = new ArrayList<>();
        List<Joker> availableJokers = getUnlockedJokers();

        for (int i = 0; i < JOKERS_PER_VISIT; i++) {
            if (availableJokers.isEmpty()) {
                break;
            }
            Joker joker = selectRandomJoker(availableJokers);
            jokers.add(joker);
        }

        return jokers;
    }

    /**
     * Obtiene la lista de jokers desbloqueados.
     *
     * @return lista de jokers desbloqueados
     */
    private List<Joker> getUnlockedJokers() {
        return JokerRepository.getAllJokers().stream()
                .filter(joker -> unlockService.getUnlockedJokerIds().contains(joker.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Selecciona un joker aleatorio de la lista disponible.
     *
     * @param availableJokers la lista de jokers disponibles
     * @return un joker aleatorio
     */
    private Joker selectRandomJoker(List<Joker> availableJokers) {
        int index = random.nextInt(availableJokers.size());
        return availableJokers.get(index);
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
