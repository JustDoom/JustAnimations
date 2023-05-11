package com.imjustdoom.justanimations.api.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PermissionUtil {

    public static boolean hasPermission(List<String> permissions, Player player) {
        for (String permission : permissions) {
            if (player.hasPermission(permission)) return true;
        }
        return false;
    }

    public static boolean hasPermission(List<String> permissions, CommandSender sender) {
        for (String permission : permissions) {
            if (sender.hasPermission(permission)) return true;
        }
        return false;
    }
}