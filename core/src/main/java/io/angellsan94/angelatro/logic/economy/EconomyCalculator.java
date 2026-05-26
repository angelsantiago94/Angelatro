package io.angellsan94.angelatro.logic.economy;

/**
 * Calcula las ganancias económicas al final de una ronda.
 * <p>
 * Las ganancias se calculan como:
 * ganancia = (manosRestantes * 2) + (descartesRestantes * 1) + interés
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class EconomyCalculator {

    private static final int HANDS_BONUS = 2;
    private static final int DISCARDS_BONUS = 1;
    private static final int INTEREST_DIVISOR = 5;
    private static final int MAX_INTEREST = 5;

    /**
     * Calcula las ganancias al final de una ronda.
     *
     * @param handsRemaining    número de manos restantes
     * @param discardsRemaining número de descartes restantes
     * @param currentBalance    saldo actual del jugador
     * @return las ganancias calculadas
     */
    public int calculateEarnings(int handsRemaining, int discardsRemaining, int currentBalance) {
        // El interés se calcula sobre el saldo actual (antes de sumar bonos)
        int interest = calculateInterest(currentBalance);

        // Bono por manos y descartes restantes
        int handsBonus = handsRemaining * HANDS_BONUS;
        int discardsBonus = discardsRemaining * DISCARDS_BONUS;

        return interest + handsBonus + discardsBonus;
    }

    /**
     * Calcula el interés basado en el saldo actual.
     * <p>
     * Por cada 5 monedas ahorradas, se añade 1 moneda de interés.
     * Máximo 5 monedas de interés por ronda.
     * </p>
     *
     * @param balance el saldo actual
     * @return el interés calculado
     */
    private int calculateInterest(int balance) {
        int interest = balance / INTEREST_DIVISOR;
        return Math.min(interest, MAX_INTEREST);
    }
}
