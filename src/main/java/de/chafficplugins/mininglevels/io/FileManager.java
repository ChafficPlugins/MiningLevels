package de.chafficplugins.mininglevels.io;

import de.chafficplugins.mininglevels.MiningLevels;
import io.github.chafficui.CrucialLib.io.Json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileManager {
    private static MiningLevels plugin;
    private static MiningLevels getPlugin() {
        if (plugin == null) plugin = MiningLevels.getPlugin(MiningLevels.class);
        return plugin;
    }

    public static String PLAYERS;
    public static String BLOCKS;
    public static String LEVELS;

    static {
        MiningLevels p = getPlugin();
        PLAYERS = p.getDataFolder() + "/data/players.json";
        BLOCKS = p.getDataFolder() + "/config/blocks.json";
        LEVELS = p.getDataFolder() + "/config/levels.json";
    }

    public FileManager() throws IOException {
        setupFiles();
        File playersFile = new File(PLAYERS);
        loadFile(playersFile);
        File blocksFile = new File(BLOCKS);
        loadFile(blocksFile);
        File levelsFile = new File(LEVELS);
        loadFile(levelsFile);
    }

    private void setupFiles() throws IOException {
        File playersFile = new File(PLAYERS);
        if (!playersFile.exists()) {
            File dir = playersFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            playersFile.createNewFile();
        }
        File blocksFile = new File(BLOCKS);
        if (!blocksFile.exists()) {
            copyDefault("defaults/blocks.json", blocksFile);
        }
        File levelsFile = new File(LEVELS);
        if (!levelsFile.exists()) {
            copyDefault("defaults/levels.json", levelsFile);
        }
    }

    private void copyDefault(String resourcePath, File destination) throws IOException {
        File dir = destination.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (InputStream in = getPlugin().getResource(resourcePath)) {
            if (in == null) {
                throw new IOException("Bundled default resource not found: " + resourcePath);
            }
            Files.copy(in, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void loadFile(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (file.createNewFile()) {
                return;
            }
            throw new IOException("Could not create " + file.getAbsolutePath() + ".");
        }
    }

    public static void saveFile(String filename, Object object) throws IOException {
        Json.saveFile(Json.toJson(object), filename);
    }
}
