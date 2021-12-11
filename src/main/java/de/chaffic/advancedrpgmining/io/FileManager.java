package de.chaffic.advancedrpgmining.io;

import de.chaffic.advancedrpgmining.AdvancedRPGMining;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private final static AdvancedRPGMining plugin = AdvancedRPGMining.getPlugin(AdvancedRPGMining.class);
    public final static String PLAYERS = plugin.getDataFolder() + "/data/players.json";
    public final static String BLOCKS = plugin.getDataFolder() + "/config/blocks.json";
    public final static String LEVELS = plugin.getDataFolder() + "/config/levels.json";

    private final File playersFile = new File(PLAYERS);
    private final File blocksFile = new File(BLOCKS);
    private final File levelsFile = new File(LEVELS);

    public FileManager() throws IOException {
        loadFile(playersFile);
        loadFile(blocksFile);
        loadFile(levelsFile);
    }

    public File getPlayersFile() {
        return playersFile;
    }

    public File getBlocksFile() {
        return blocksFile;
    }

    public File getLevelsFile() {
        return levelsFile;
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
}
