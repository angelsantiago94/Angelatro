package io.angellsan94.angelatro.logic.economy;

import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link ShopGenerator}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de ShopGenerator - TDD")
class ShopGeneratorTest {

    private ShopGenerator shopGenerator;

    @BeforeEach
    void setUp() {
        shopGenerator = new ShopGenerator();
    }

    // ==================== GENERACIÓN DE JOKERS ====================

    @Test
    @DisplayName("Genera exactamente 2 jokers por visita")
    void testGeneratesExactly2JokersPerVisit() {
        List<Joker> jokers = shopGenerator.generateJokers();
        assertEquals(2, jokers.size());
    }

    @Test
    @DisplayName("Los jokers generados pertenecen al pool desbloqueado")
    void testGeneratedJokersBelongToUnlockedPool() {
        List<Joker> jokers = shopGenerator.generateJokers();

        for (Joker joker : jokers) {
            assertNotNull(joker.getId());
            assertNotNull(joker.getName());
            assertNotNull(joker.getDescription());
            assertTrue(joker.getPrice() > 0);
            assertNotNull(joker.getRarity());
            assertNotNull(joker.getEffect());
        }
    }

    @Test
    @DisplayName("La distribución de rareza respeta las probabilidades (test estadístico con N grande)")
    void testRarityDistributionFollowsProbabilities() {
        int iterations = 1000;
        int commonCount = 0;
        int uncommonCount = 0;
        int rareCount = 0;

        for (int i = 0; i < iterations; i++) {
            List<Joker> jokers = shopGenerator.generateJokers();
            for (Joker joker : jokers) {
                switch (joker.getRarity()) {
                    case COMMON -> commonCount++;
                    case UNCOMMON -> uncommonCount++;
                    case RARE -> rareCount++;
                }
            }
        }

        int totalJokers = iterations * 2;
        double commonRatio = (double) commonCount / totalJokers;
        double uncommonRatio = (double) uncommonCount / totalJokers;
        double rareRatio = (double) rareCount / totalJokers;

        // Probabilidades esperadas: COMMON 70%, UNCOMMON 25%, RARE 5%
        // Permitimos una desviación del 10%
        assertTrue(commonRatio >= 0.60 && commonRatio <= 0.80,
                "COMMON ratio: " + commonRatio + ", expected ~0.70");
        assertTrue(uncommonRatio >= 0.15 && uncommonRatio <= 0.35,
                "UNCOMMON ratio: " + uncommonRatio + ", expected ~0.25");
        assertTrue(rareRatio >= 0.00 && rareRatio <= 0.10,
                "RARE ratio: " + rareRatio + ", expected ~0.05");
    }

    // ==================== GENERACIÓN DE MEJORAS DE MANO ====================

    @Test
    @DisplayName("Genera exactamente 2 mejoras de mano aleatorias")
    void testGeneratesExactly2HandUpgrades() {
        List<HandType> upgrades = shopGenerator.generateHandUpgrades();
        assertEquals(2, upgrades.size());
    }

    @Test
    @DisplayName("El precio de una mejora de mano es 6 monedas")
    void testHandUpgradePriceIs6() {
        int price = shopGenerator.getHandUpgradePrice();
        assertEquals(6, price);
    }
}
