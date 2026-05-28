package io.angellsan94.angelatro.logic.jokers;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio centralizado de todos los jokers disponibles en el juego.
 * <p>
 * Proporciona acceso a los jokers definidos según las especificaciones v3.
 * Los jokers J001-J004 están implementados inicialmente (limitación KISS).
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class JokerRepository {

    /**
     * Obtiene todos los jokers disponibles en el juego.
     *
     * @return lista de todos los jokers
     */
    public static List<Joker> getAllJokers() {
        List<Joker> jokers = new ArrayList<>();
        jokers.add(createJ001());
        jokers.add(createJ002());
        jokers.add(createJ003());
        jokers.add(createJ004());
        return jokers;
    }

    /**
     * Obtiene los jokers de una rareza específica.
     *
     * @param rarity la rareza deseada
     * @return lista de jokers de esa rareza
     */
    public static List<Joker> getJokersByRarity(Rarity rarity) {
        return getAllJokers().stream()
                .filter(joker -> joker.getRarity() == rarity)
                .toList();
    }

    /**
     * Crea el Joker J001 - Matador.
     * <p>
     * Efecto: +30 chips si la mano es Pareja.
     * </p>
     *
     * @return el joker Matador
     */
    private static Joker createJ001() {
        return new Joker(
                "J001",
                "Matador",
                "+30 chips si la mano es Pareja",
                4,
                Rarity.COMMON,
                new PairChipsEffect()
        );
    }

    /**
     * Crea el Joker J002 - Corazón Ardiente.
     * <p>
     * Efecto: Cada carta de Corazones aporta +2 mult.
     * </p>
     *
     * @return el joker Corazón Ardiente
     */
    private static Joker createJ002() {
        return new Joker(
                "J002",
                "Corazón Ardiente",
                "Cada carta de Corazones aporta +2 mult",
                4,
                Rarity.COMMON,
                new HeartMultEffect()
        );
    }

    /**
     * Crea el Joker J003 - Memorioso.
     * <p>
     * Efecto: +1 mult por cada vez que has jugado esta mano en la partida.
     * </p>
     *
     * @return el joker Memorioso
     */
    private static Joker createJ003() {
        return new Joker(
                "J003",
                "Memorioso",
                "+1 mult por cada vez que has jugado esta mano en la partida",
                6,
                Rarity.UNCOMMON,
                new CumulativeMultEffect()
        );
    }

    /**
     * Crea el Joker J004 - Escalador.
     * <p>
     * Efecto: +15 chips si la mano es Escalera o superior.
     * </p>
     *
     * @return el joker Escalador
     */
    private static Joker createJ004() {
        return new Joker(
                "J004",
                "Escalador",
                "+15 chips si la mano es Escalera o superior",
                4,
                Rarity.COMMON,
                new StairChipsEffect()
        );
    }
}
