package com.github.eokasta.economy.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.text.DecimalFormat;

public class Helper {

    private static final String[] FORMATS = { "-", "-", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR",
            "QT", "QN", "SD", "SSD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QVG", "QVN", "SEV", "SPV", "OVG", "NVG",
            "TG" };

    public static String formatBalance(Object value) {
        try {
            final String val = (new DecimalFormat("#,###")).format(value).replace(".", ",");
            final int ii = val.indexOf(","), i = val.split(",").length;
            if (ii == -1)
                return val;

            return (val.substring(0, ii + 2) + FORMATS[i]).replace(",0", "");
        } catch (Exception e) {
            final String val = (new DecimalFormat("#,###")).format(value).replace(".", ",");
            final int ii = val.indexOf(",");
            if (ii == -1)
                return val;

            final String num = val.substring(0, 1);
            final String finalVal = val.substring(1).replace(",", "");
            return num + "e" + finalVal.length();
        }
    }


    public static String serializeLocation(Location location) {
        return (location.getWorld().getName() +
                ";" + location.getX() +
                ";" + location.getY() +
                ";" + location.getZ() +
                ";" + location.getPitch() +
                ";" + location.getYaw()).replace(".", "/");
    }

    public static Location deserializeLocation(String string) {
        String[] split = string.split(";");

        final World world = Bukkit.getWorld(split[0]);
        final double x = Double.parseDouble(split[1].replace("/", "."));
        final double y = Double.parseDouble(split[2].replace("/", "."));
        final double z = Double.parseDouble(split[3].replace("/", "."));

        final float pitch = Float.parseFloat(split[4].replace("/", "."));
        final float yaw = Float.parseFloat(split[5].replace("/", "."));

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String DateRemainingFormat(long value) {
        final long var = System.currentTimeMillis() - value;
        final long seconds = var / 1000 % 60 * -1;
        final long minutes = var / 60000 % 60 * -1;
        final long hours = var / 3600000 % 24 * -1;
        final long days = var / 86400000 % 30 * -1;

        if (days == 0 && hours == 0 && minutes == 0)
            return seconds + "s";

        if (days == 0 && hours == 0)
            return minutes + "m " + seconds + "s";

        if (days == 0)
            return hours + "h " + minutes + "m " + seconds + "s";

        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }

    public static String DateRemainingFormat(long value, long value2) {
        final long var = value - value2;
        final long seconds = var / 1000 % 60 * -1;
        final long minutes = var / 60000 % 60 * -1;
        final long hours = var / 3600000 % 24 * -1;
        final long days = var / 86400000 % 30 * -1;

        if (days == 0 && hours == 0 && minutes == 0)
            return seconds + "s";

        if (days == 0 && hours == 0)
            return minutes + "m " + seconds + "s";

        if (days == 0)
            return hours + "h " + minutes + "m " + seconds + "s";

        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }

    public static String dateFormat(long value) {
        final long seconds = value / 1000 % 60;
        final long minutes = value / 60000 % 60;
        final long hours = value / 3600000 % 24;
        final long days = value / 86400000 % 30;

        if (days == 0 && hours == 0 && minutes == 0)
            return seconds + "s";

        if (days == 0 && hours == 0)
            return minutes + "m " + seconds + "s";

        if (days == 0)
            return hours + "h " + minutes + "m " + seconds + "s";

        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }

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
