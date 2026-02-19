package de.chafficplugins.mininglevels.utils;

import de.chafficplugins.mininglevels.MiningLevels;
import io.github.chafficui.CrucialLib.Utils.localization.LocalizedFromYaml;

import java.io.IOException;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.LOCALIZED_IDENTIFIER;

public class CustomMessages extends LocalizedFromYaml {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    public CustomMessages() throws IOException {
        super(LOCALIZED_IDENTIFIER, plugin.getDataFolder(), "messages.yml");
    }

    @Override
    public String getLocalizedString(String key) {
        return super.getLocalizedString(key);
    }
}
