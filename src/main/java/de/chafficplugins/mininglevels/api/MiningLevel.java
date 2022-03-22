package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.io.FileManager;
import de.chafficplugins.mininglevels.io.Json;

import java.io.IOException;
import java.util.ArrayList;

public class MiningLevel {
    private final String name;
    private final int nextLevelXP;
    private final int ordinal;
    private float instantBreakProbability = 0;
    private float extraOreProbability = 0;
    private float maxExtraOre = 0;
    private int hasteLevel = 0;

    public MiningLevel(String name, int nextLevelXP, int ordinal) {
        this.name = name;
        this.nextLevelXP = nextLevelXP;
        this.ordinal = ordinal;
    }

    public String getName() {
        return name;
    }

    public int getNextLevelXP() {
        return nextLevelXP;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public float getInstantBreakProbability() {
        return instantBreakProbability;
    }

    public void setInstantBreakProbability(float instantBreakProbability) {
        this.instantBreakProbability = instantBreakProbability;
    }

    public float getExtraOreProbability() {
        return extraOreProbability;
    }

    public void setExtraOreProbability(float extraOreProbability) {
        this.extraOreProbability = extraOreProbability;
    }

    public float getMaxExtraOre() {
        return maxExtraOre;
    }

    public void setMaxExtraOre(float maxExtraOre) {
        this.maxExtraOre = maxExtraOre;
    }

    public int getHasteLevel() {
        return hasteLevel;
    }

    public void setHasteLevel(int hasteLevel) {
        this.hasteLevel = hasteLevel;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof MiningLevel) {
            return ((MiningLevel) object).ordinal == this.ordinal;
        }
        return false;
    }

    //Static
    public static ArrayList<MiningLevel> miningLevels = new ArrayList<>();

    public static void init() throws IOException {
        miningLevels = Json.loadFile(FileManager.LEVELS, new TypeToken<ArrayList<MiningLevel>>(){}.getType());
    }

    public static void save() throws IOException {
        if(miningLevels != null) {
            Json.saveFile(FileManager.LEVELS, miningLevels);
        }
    }

    public static void reload() throws IOException {
        init();
    }

    public static MiningLevel get(int index) {
        return miningLevels.get(index);
    }
}
