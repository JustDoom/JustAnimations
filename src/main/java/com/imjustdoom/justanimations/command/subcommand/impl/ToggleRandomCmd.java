package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ToggleRandomCmd implements SubCommand {

    public String getName() {
        return "togglerandom";
    }

    public String getDescription() {
        return "Toggles if the animations should play in reverse when they get to the end instead of resetting";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (org.bukkit.entity.Player) sender)) {
            return;
        }

        JustAnimations.INSTANCE.getAnimations().get(args[1]).getDataStore().saveSetting("random-frame", !JustAnimations.INSTANCE.getAnimations().get(args[1]).isRandomFrame());
        JustAnimations.INSTANCE.getAnimations().get(args[1]).setRandomFrame(!JustAnimations.INSTANCE.getAnimations().get(args[1]).isRandomFrame());
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.RANDOM_SUCCESS,
                args[1],
                JustAnimations.INSTANCE.getAnimations().get(args[1]).isRandomFrame()));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.togglerandom", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
