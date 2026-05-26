package io.angellsan94.angelatro.logic.economy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link EconomyCalculator}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de EconomyCalculator - TDD")
class EconomyCalculatorTest {

    private EconomyCalculator economyCalculator;

    @BeforeEach
    void setUp() {
        economyCalculator = new EconomyCalculator();
    }

    // ==================== CÁLCULO DE GANANCIAS ====================

    @Test
    @DisplayName("2 manos restantes + 1 descarte restante + 10 monedas → ganancia = 2*2 + 1*1 + floor(10/5) = 7")
    void testCalculateEarningsWithHandsDiscardsAndInterest() {
        int handsRemaining = 2;
        int discardsRemaining = 1;
        int currentBalance = 10;

        int earnings = economyCalculator.calculateEarnings(handsRemaining, discardsRemaining, currentBalance);
        assertEquals(7, earnings);
    }

    @Test
    @DisplayName("Interés máximo de 5 monedas (25+ monedas guardadas)")
    void testMaxInterestOf5Coins() {
        int handsRemaining = 0;
        int discardsRemaining = 0;
        int currentBalance = 30;

        int earnings = economyCalculator.calculateEarnings(handsRemaining, discardsRemaining, currentBalance);
        // interés = floor(30/5) = 6, pero máximo es 5
        assertEquals(5, earnings);
    }

    @Test
    @DisplayName("El interés se aplica sobre el saldo antes de sumar el bono de manos/descartes")
    void testInterestAppliedBeforeHandsDiscardsBonus() {
        int handsRemaining = 2;
        int discardsRemaining = 1;
        int currentBalance = 10;

        int earnings = economyCalculator.calculateEarnings(handsRemaining, discardsRemaining, currentBalance);
        // interés = floor(10/5) = 2
        // bono manos = 2*2 = 4
        // bono descartes = 1*1 = 1
        // total = 2 + 4 + 1 = 7
        assertEquals(7, earnings);
    }
}
