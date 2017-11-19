package me.matthewe.meprison.chat.rank;

import org.bukkit.ChatColor;

/**
 * Created by Matthew E on 11/18/2017.
 */
public enum Rank {
    NORMAL(0, "Normal", "&7", "&f"),
    IRON(1, "Iron", "&b&lIRON&b ", "&f"),
    GOLD(2,"Gold","&e&lGOLD&e ", "&f"),
    DIAMOND(3,"Diamond", "&b&lDIAMOND&b ", "&f"),
    EMERALD(4, "Emerald", "&a&lEMERALD&a ", "&f");

    private int id;
    private String name;
    private String prefix;
    private String suffix;

    Rank(int id, String name, String prefix, String suffix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String formatMessage(String playerName, String message) {
        return ChatColor.translateAlternateColorCodes('&', prefix + playerName + suffix + ": " + message);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
