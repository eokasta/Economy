package com.github.eokasta.economy.utils;

import org.bukkit.ChatColor;

public class Helper {

    public static String append(String... strings) {
        return String.join("\n", strings);
    }

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String format(String message, Object... args) {
        return format(String.format(message, args));
    }

}
