package de.chafficplugins.mininglevels.utils;

public class ConfigStrings {
    public final static int SPIGOT_ID = 100886;
    public final static int BSTATS_ID = 14709;
    public final static String CRUCIAL_API_VERSION = "2.1.5";
    public final static String LOCALIZED_IDENTIFIER = "mininglevels";

    public static String PREFIX = "§8[§6ML§8] §r";

    public final static String DOWNLOAD_URL = "https://drive.google.com/uc?export=download&id=";
    public final static String MINING_LEVELS_JSON = "145W6qtWM2-PA3vyDlOpa3nnYrn0bUVyb";
    public final static String MINING_BLOCKS_JSON = "1W1EN0NIJKH69cokExNKVglOw6YPbEBYT";

    //Permissions
    public final static String PERMISSION_ADMIN = "mininglevels.*";
    public final static String PERMISSION_SET_LEVEL = "mininglevels.setlevel";
    public final static String PERMISSION_SET_XP = "mininglevels.setxp";
    public final static String PERMISSION_LEVEL = "mininglevels.level";
    public final static String PERMISSION_RELOAD = "mininglevels.reload";
    public final static String PERMISSION_EDITOR = "mininglevels.editor";
    public final static String PERMISSIONS_LEADERBOARD = "mininglevels.leaderboard";

    //Configuration
    public final static String LVL_UP_SOUND = "levelup_sound";
    public final static String MAX_LEVEL_XP_DROPS = "max_level_xp_drops";
    public final static String LEVEL_WITH_PLAYER_PLACED_BLOCKS = "level_with.player_placed_blocks";
    public final static String LEVEL_WITH_GENERATED_BLOCKS = "level_with.generated_blocks";
    public final static String LEVEL_PROGRESSION_MESSAGES = "level_progression_messages";
    public final static String MINING_ITEMS = "mining_items";

    //Messages
    public final static String NO_PERMISSION = "no_permission";
    public final static String NEW_LEVEL = "new_level"; //You are now level {0}.
    public final static String PLAYER_NOT_EXIST = "player_not_exist"; //Player {0} does not exist.
    public final static String XP_RECEIVED = "xp_received"; //You received {0} XP.
    public final static String LEVEL_OF = "level_of"; //{0} is level {1}.
    public final static String LEVEL_UNLOCKED = "level_unlocked"; //You've unlocked level {0}.
    public final static String HASTELVL_CHANGE = "hastelvl_change"; //Haste Level: {0} -> {1}.
    public final static String INSTANT_BREAK_CHANGE = "instant_break_change"; //Instant Break Probability: {0} -> {1}.
    public final static String EXTRA_ORE_CHANGE = "extra_ore_change"; //Extra Ore Probability: {0} -> {1}.
    public final static String MAX_EXTRA_ORE_CHANGE = "max_extra_ore_change"; //Max Extra Ores: {0} -> {1}.
    public final static String REWARDS_LIST = "rewards_list"; //Rewards:
    public final static String CLAIM_YOUR_REWARD = "claim_your_reward"; //You can claim your reward by typing /miningrewards.
    public final static String LEVEL_DROPPED = "level_dropped"; //Your level has dropped to {0}.
    public final static String XP_GAINED = "xp_gained"; //Level {0}: [{1}/{2}]
    public final static String RELOAD_SUCCESSFUL = "reload_successful"; //Successfully reloaded config.
    public final static String ERROR_OCCURRED = "error_occurred"; //An error occurred.
    public final static String NO_CONSOLE_COMMAND = "no_console_command"; //You can't execute this command from console.
    public final static String CURRENT_LEVEL = "current_level"; //Current Level: {0}
    public final static String CURRENT_XP = "current_xp"; //Current XP: [{0}/{1}]
    public final static String CURRENT_HASTE_LEVEL = "current_haste_level"; //Current Haste Level: {0}
    public final static String CURRENT_INSTANT_BREAK_LEVEL = "current_instant_break_level"; //Current Instant Break Level: {0}
    public final static String CURRENT_EXTRA_ORE_LEVEL = "current_extra_ore_level"; //Current Extra Ore Level: {0}
    public final static String CURRENT_MAX_EXTRA_ORE = "current_max_extra_ore"; //Current Max Extra Ores: {0}
    public final static String ONLY_BLOCKS_ALLOWED = "only_blocks_allowed"; //You can only add blocks to the material list.
    public final static String CANT_DELETE_LEVEL = "cant_delete_level"; //You can only delete the last level.
    public final static String NO_REWARDS = "no_rewards"; //You have no rewards.
    public final static String REWARDS_CLAIMED = "rewards_claimed"; //You have claimed your reward.
    public final static String NO_MORE_SPACE = "no_more_space"; //You don't have enough space in your inventory.
    public final static String LEVEL_NEEDED = "level_needed"; //You need to be level {0} to break this block.
    public final static String LEADERBOARD_HEADER = "leaderboard_header"; //Leaderboard
    //Usages
    public final static String USAGE_SET_LEVEL = "usage_set_level"; //Usage: /ml setlevel <player> <level>
    public final static String USAGE_SET_XP = "usage_set_xp"; //Usage: /ml setxp <player> <xp>
    public final static String USAGE_LEVEL = "usage_level"; //Usage: /ml level <player>
}
