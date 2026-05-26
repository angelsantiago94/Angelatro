package io.angellsan94.angelatro.logic.economy;

import io.angellsan94.angelatro.exceptions.NotEnoughMoneyException;
import io.angellsan94.angelatro.logic.model.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link Wallet}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de Wallet - TDD")
class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet(DeckType.STANDARD);
    }

    // ==================== CONSTRUCTOR ====================

    @Test
    @DisplayName("new Wallet(DeckType.STANDARD) → saldo inicial 4")
    void testConstructorStandardDeck() {
        assertEquals(4, wallet.getAmount());
    }

    @Test
    @DisplayName("new Wallet(DeckType.WEALTHY) → saldo inicial 8")
    void testConstructorWealthyDeck() {
        Wallet wealthyWallet = new Wallet(DeckType.WEALTHY);
        assertEquals(8, wealthyWallet.getAmount());
    }

    // ==================== GASTAR ====================

    @Test
    @DisplayName("spend(amount) reduce el saldo")
    void testSpendReducesBalance() {
        wallet.spend(2);
        assertEquals(2, wallet.getAmount());
    }

    @Test
    @DisplayName("spend() con saldo insuficiente lanza NotEnoughMoneyException")
    void testSpendWithInsufficientBalanceThrowsException() {
        assertThrows(NotEnoughMoneyException.class, () -> wallet.spend(5));
    }

    // ==================== GANAR ====================

    @Test
    @DisplayName("earn(amount) aumenta el saldo")
    void testEarnIncreasesBalance() {
        wallet.earn(3);
        assertEquals(7, wallet.getAmount());
    }

    // ==================== CONSULTAR ====================

    @Test
    @DisplayName("getAmount() devuelve el saldo actual")
    void testGetAmountReturnsCurrentBalance() {
        assertEquals(4, wallet.getAmount());
        wallet.earn(5);
        assertEquals(9, wallet.getAmount());
    }
}
