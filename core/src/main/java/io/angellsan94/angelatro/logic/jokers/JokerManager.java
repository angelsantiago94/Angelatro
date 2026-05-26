package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.exceptions.JokerLimitExceededException;
import io.angellsan94.angelatro.logic.economy.Wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona los jokers activos del jugador.
 * <p>
 * Permite añadir, eliminar y vender jokers, con un límite máximo de 6 jokers activos.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class JokerManager {

    private static final int MAX_ACTIVE_JOKERS = 6;

    private final List<Joker> activeJokers;

    /**
     * Constructor que inicializa el gestor de jokers.
     */
    public JokerManager() {
        this.activeJokers = new ArrayList<>();
    }

    /**
     * Añade un joker a la lista de jokers activos.
     *
     * @param joker el joker a añadir
     * @throws JokerLimitExceededException si ya hay 6 jokers activos
     */
    public void add(Joker joker) {
        if (activeJokers.size() >= MAX_ACTIVE_JOKERS) {
            throw new JokerLimitExceededException("No se pueden tener más de " + MAX_ACTIVE_JOKERS + " jokers activos");
        }
        activeJokers.add(joker);
    }

    /**
     * Vende un joker activo, eliminándolo de la lista y devolviendo la mitad de su precio.
     *
     * @param joker  el joker a vender
     * @param wallet el monedero donde se añadirá el reembolso
     * @return el reembolso (floor(precio / 2))
     * @throws IllegalArgumentException si el joker no está activo
     */
    public int sell(Joker joker, Wallet wallet) {
        if (!activeJokers.contains(joker)) {
            throw new IllegalArgumentException("El joker no está activo");
        }

        int refund = joker.getPrice() / 2;
        activeJokers.remove(joker);
        wallet.earn(refund);
        return refund;
    }

    /**
     * Obtiene la lista de jokers activos.
     *
     * @return lista de jokers activos
     */
    public List<Joker> getActiveJokers() {
        return new ArrayList<>(activeJokers);
    }

    /**
     * Obtiene el número de jokers activos.
     *
     * @return número de jokers activos
     */
    public int getActiveJokersCount() {
        return activeJokers.size();
    }
}
