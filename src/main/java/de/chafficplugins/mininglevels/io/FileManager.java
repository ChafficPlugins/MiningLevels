package de.chafficplugins.mininglevels.io;

import de.chafficplugins.mininglevels.MiningLevels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public class FileManager {
    private final static MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    public final static String PLAYERS = plugin.getDataFolder() + "/data/players.json";
    public final static String BLOCKS = plugin.getDataFolder() + "/config/blocks.json";
    public final static String LEVELS = plugin.getDataFolder() + "/config/levels.json";

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
            downloadFile(blocksFile, MINING_BLOCKS_JSON);
        }
        File levelsFile = new File(LEVELS);
        if (!levelsFile.exists()) {
            downloadFile(levelsFile, MINING_LEVELS_JSON);
        }
    }

    private void downloadFile(File file, String downloadURL) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            URL url = new URL(DOWNLOAD_URL + downloadURL);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            throw new IOException("Could not download " + file.getName() + "!");
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
}
