package io.angellsan94.angelatro.logic.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link HandLevelManager}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de HandLevelManager - TDD")
class HandLevelManagerTest {

    private HandLevelManager handLevelManager;

    /**
     * Configuración inicial para cada prueba.
     */
    @BeforeEach
    void setUp() {
        handLevelManager = new HandLevelManager();
    }

    // ==================== NIVEL INICIAL ====================

    @Test
    @DisplayName("Nivel inicial: cualquier mano empieza en nivel 0")
    void testInitialLevelIsZero() {
        assertEquals(0, handLevelManager.getLevel(HandType.CARTA_ALTA));
        assertEquals(0, handLevelManager.getLevel(HandType.PAREJA));
        assertEquals(0, handLevelManager.getLevel(HandType.POKER));
        assertEquals(0, handLevelManager.getLevel(HandType.ESCALERA_REAL));
    }

    // ==================== CHIPS ====================

    @Test
    @DisplayName("getChips(PAREJA) nivel 0 → 20")
    void testGetChipsParejaLevel0() {
        assertEquals(20, handLevelManager.getChips(HandType.PAREJA));
    }

    @Test
    @DisplayName("getChips(PAREJA) nivel 3 → 65")
    void testGetChipsParejaLevel3() {
        handLevelManager.upgrade(HandType.PAREJA);
        handLevelManager.upgrade(HandType.PAREJA);
        handLevelManager.upgrade(HandType.PAREJA);
        assertEquals(65, handLevelManager.getChips(HandType.PAREJA));
    }

    @Test
    @DisplayName("getChips(CARTA_ALTA) nivel 0 → 5")
    void testGetChipsCartaAltaLevel0() {
        assertEquals(5, handLevelManager.getChips(HandType.CARTA_ALTA));
    }

    // ==================== MULTIPLICADOR ====================

    @Test
    @DisplayName("getMult(PAREJA) nivel 3 → 5")
    void testGetMultParejaLevel3() {
        handLevelManager.upgrade(HandType.PAREJA);
        handLevelManager.upgrade(HandType.PAREJA);
        handLevelManager.upgrade(HandType.PAREJA);
        assertEquals(5, handLevelManager.getMult(HandType.PAREJA));
    }

    @Test
    @DisplayName("getMult(CARTA_ALTA) nivel 1 → 2")
    void testGetMultCartaAltaLevel1() {
        handLevelManager.upgrade(HandType.CARTA_ALTA);
        assertEquals(2, handLevelManager.getMult(HandType.CARTA_ALTA));
    }

    @Test
    @DisplayName("getMult(CARTA_ALTA) nivel 2 → 3")
    void testGetMultCartaAltaLevel2() {
        handLevelManager.upgrade(HandType.CARTA_ALTA);
        handLevelManager.upgrade(HandType.CARTA_ALTA);
        assertEquals(3, handLevelManager.getMult(HandType.CARTA_ALTA));
    }

    // ==================== UPGRADE ====================

    @Test
    @DisplayName("upgrade(handType) incrementa el nivel en 1")
    void testUpgradeIncrementsLevelByOne() {
        assertEquals(0, handLevelManager.getLevel(HandType.PAREJA));
        handLevelManager.upgrade(HandType.PAREJA);
        assertEquals(1, handLevelManager.getLevel(HandType.PAREJA));
        handLevelManager.upgrade(HandType.PAREJA);
        assertEquals(2, handLevelManager.getLevel(HandType.PAREJA));
    }
}
