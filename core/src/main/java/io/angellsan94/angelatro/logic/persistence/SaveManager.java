package io.angellsan94.angelatro.logic.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Gestiona el guardado y carga de sesiones de juego.
 * <p>
 * Usa Gson para serializar/deserializar GameSession a JSON.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class SaveManager {

    private static final String SAVE_FILE_NAME = "save.json";
    private final String saveDirectory;
    private final Gson gson;

    /**
     * Constructor de SaveManager.
     *
     * @param saveDirectory el directorio donde se guardará el archivo
     */
    public SaveManager(String saveDirectory) {
        this.saveDirectory = saveDirectory;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Guarda la sesión de juego en un archivo JSON.
     *
     * @param gameSession la sesión de juego a guardar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public void save(GameSession gameSession) throws IOException {
        Path savePath = Paths.get(saveDirectory, SAVE_FILE_NAME);

        // Crear directorio si no existe
        File directory = savePath.toFile().getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(savePath.toFile())) {
            gson.toJson(gameSession, writer);
        }
    }

    /**
     * Carga la sesión de juego desde el archivo JSON.
     *
     * @return Optional con la sesión cargada, o empty si no existe el archivo
     */
    public Optional<GameSession> load() {
        Path savePath = Paths.get(saveDirectory, SAVE_FILE_NAME);

        if (!Files.exists(savePath)) {
            return Optional.empty();
        }

        try (FileReader reader = new FileReader(savePath.toFile())) {
            GameSession gameSession = gson.fromJson(reader, GameSession.class);
            return Optional.ofNullable(gameSession);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Elimina el archivo de guardado.
     *
     * @throws IOException si ocurre un error al eliminar el archivo
     */
    public void delete() throws IOException {
        Path savePath = Paths.get(saveDirectory, SAVE_FILE_NAME);

        if (Files.exists(savePath)) {
            Files.delete(savePath);
        }
    }
}
