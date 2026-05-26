package io.angellsan94.angelatro.logic.persistence;

import io.angellsan94.angelatro.logic.game.HandLevelManager;
import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.JokerEffect;
import io.angellsan94.angelatro.logic.jokers.Rarity;
import io.angellsan94.angelatro.logic.model.DeckType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link SaveManager}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de SaveManager - TDD")
class SaveManagerTest {

    private SaveManager saveManager;
    private GameSession gameSession;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        saveManager = new SaveManager(tempDir.toString());

        JokerEffect dummyEffect = new JokerEffect() {
            @Override
            public int modifyChips(int currentChips, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentChips;
            }

            @Override
            public int modifyMult(int currentMult, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentMult;
            }
        };

        Joker joker = new Joker("J001", "Matador", "+30 chips si la mano es PAREJA", 4, Rarity.COMMON, dummyEffect);

        Map<HandType, Integer> handLevels = new HashMap<>();
        handLevels.put(HandType.PAREJA, 2);
        handLevels.put(HandType.ESCALERA, 1);

        gameSession = new GameSession(
                3,
                "STANDARD",
                10,
                List.of(joker),
                handLevels
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        saveManager.delete();
    }

    // ==================== GUARDAR ====================

    @Test
    @DisplayName("save(gameSession) escribe un JSON válido que incluye el id del DeckType activo")
    void testSaveWritesValidJsonWithDeckTypeId() throws IOException {
        saveManager.save(gameSession);

        File saveFile = new File(tempDir.toFile(), "save.json");
        assertTrue(saveFile.exists());

        Optional<GameSession> loaded = saveManager.load();
        assertTrue(loaded.isPresent());
        assertEquals("STANDARD", loaded.get().getDeckTypeId());
    }

    // ==================== CARGAR ====================

    @Test
    @DisplayName("load() reconstruye la sesión exactamente igual (roundNumber, deckType, wallet, jokers activos, niveles de mano)")
    void testLoadReconstructsSessionExactly() throws IOException {
        saveManager.save(gameSession);

        Optional<GameSession> loaded = saveManager.load();
        assertTrue(loaded.isPresent());

        GameSession loadedSession = loaded.get();
        assertEquals(3, loadedSession.getRoundNumber());
        assertEquals("STANDARD", loadedSession.getDeckTypeId());
        assertEquals(10, loadedSession.getWalletAmount());
        assertEquals(1, loadedSession.getActiveJokers().size());
        assertEquals("J001", loadedSession.getActiveJokers().get(0).getId());
        assertEquals(2, loadedSession.getHandLevels().get(HandType.PAREJA));
        assertEquals(1, loadedSession.getHandLevels().get(HandType.ESCALERA));
    }

    @Test
    @DisplayName("load() usa DeckType.fromId() para reconstruir el mazo — no hardcodea valores")
    void testLoadUsesDeckTypeFromId() throws IOException {
        saveManager.save(gameSession);

        Optional<GameSession> loaded = saveManager.load();
        assertTrue(loaded.isPresent());

        String deckTypeId = loaded.get().getDeckTypeId();
        DeckType deckType = DeckType.fromId(deckTypeId);
        assertEquals(DeckType.STANDARD, deckType);
    }

    @Test
    @DisplayName("load() cuando no existe archivo devuelve Optional.empty()")
    void testLoadWhenNoFileReturnsEmpty() {
        Optional<GameSession> loaded = saveManager.load();
        assertFalse(loaded.isPresent());
    }

    // ==================== ELIMINAR ====================

    @Test
    @DisplayName("delete() elimina el archivo de guardado")
    void testDeleteRemovesSaveFile() throws IOException {
        saveManager.save(gameSession);

        File saveFile = new File(tempDir.toFile(), "save.json");
        assertTrue(saveFile.exists());

        saveManager.delete();
        assertFalse(saveFile.exists());

        Optional<GameSession> loaded = saveManager.load();
        assertFalse(loaded.isPresent());
    }
}
