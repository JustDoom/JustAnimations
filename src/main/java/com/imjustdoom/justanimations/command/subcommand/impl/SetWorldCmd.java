package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetWorldCmd implements SubCommand {

    public String getName() {
        return "setworld";
    }

    public String getDescription() {
        return "Sets the world the animations are played in";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (org.bukkit.entity.Player) sender)) {
            return;
        }

        if (args.length == 4 || Bukkit.getWorld(args[4]) == null) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_NOT_EXISTS,
                    JustAnimations.INSTANCE.getAnimations().get(args[1]).getWorld().getName())); // TODO: make this not say the world name of animation when no world is input
            return;
        }
        if (Bukkit.getWorld(args[4]).getUID().equals(JustAnimations.INSTANCE.getAnimations().get(args[1]).getWorld().getUID())) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_IN_USE,
                    JustAnimations.INSTANCE.getAnimations().get(args[1]).getWorld().getName()));
            return;
        }
        JustAnimations.INSTANCE.getAnimations().get(args[1]).setWorld(Bukkit.getWorld(args[4]));
        JustAnimations.INSTANCE.getAnimations().get(args[1]).getDataStore().saveSetting("world", Bukkit.getWorld(args[4]).getUID().toString());
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_CHANGE,
                JustAnimations.INSTANCE.getAnimations().get(args[1]).getWorld().getName()));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.setworld", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        return Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
