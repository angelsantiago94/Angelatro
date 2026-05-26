package io.angellsan94.angelatro.logic.economy;

import io.angellsan94.angelatro.exceptions.NotEnoughMoneyException;
import io.angellsan94.angelatro.logic.model.DeckType;
import lombok.Getter;

/**
 * Gestiona el saldo del jugador.
 * <p>
 * El saldo inicial depende del tipo de mazo seleccionado.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class Wallet {

    private int amount;

    /**
     * Constructor que inicializa el monedero con el saldo según el tipo de mazo.
     *
     * @param deckType el tipo de mazo que determina el saldo inicial
     */
    public Wallet(DeckType deckType) {
        this.amount = deckType.getInitialMoney();
    }

    /**
     * Gasta una cantidad del saldo.
     *
     * @param amount la cantidad a gastar
     * @throws NotEnoughMoneyException si no hay suficiente saldo
     */
    public void spend(int amount) {
        if (this.amount < amount) {
            throw new NotEnoughMoneyException("Saldo insuficiente: " + this.amount + ", necesario: " + amount);
        }
        this.amount -= amount;
    }

    /**
     * Añade una cantidad al saldo.
     *
     * @param amount la cantidad a añadir
     */
    public void earn(int amount) {
        this.amount += amount;
    }
}
