package com.imjustdoom.justanimations.api.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationUtil {

    public static Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]){6}");
    public static Pattern HEX_PATTERN_2 = Pattern.compile("#([A-Fa-f0-9]){6}");

    public static String translatePlaceholders(String message) {

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(null, message);

        return translate(message);
    }

    public static String translatePlaceholders(String message, String name, Object value) {
        message = message.replaceAll("%animation%", name);
        message = message.replaceAll("%value%", value.toString());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(null, message);

        return translate(message);
    }

    public static String translatePlaceholders(String message, Object value) {
        message = message.replaceAll("%value%", value.toString());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            message = PlaceholderAPI.setPlaceholders(null, message);

        return translate(message);
    }

    public static String translate(String message) {

        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace("&#", "x");

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = HEX_PATTERN.matcher(message);
        }

        matcher = HEX_PATTERN_2.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = HEX_PATTERN_2.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
