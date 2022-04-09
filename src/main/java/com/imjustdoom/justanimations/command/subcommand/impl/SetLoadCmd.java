package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetLoadCmd implements SubCommand {

    public String getName() {
        return "setload";
    }

    public String getDescription() {
        return "Sets the loading system for the animation";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return;
        }

        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);

        if (args.length == 4) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.SETLOAD,
                    animation.getName(), animation.getSaveToRam() ? "RAM" : "FILE"));
            return;
        }

        animation.stop();

        if(animation.getSaveToRam() && args[4].equalsIgnoreCase("ram")) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.SETLOAD_ALREADY_SET,
                    animation.getName(), args[4]));
            return;
        }

        animation.setSaveToRam(args[4].equalsIgnoreCase("ram"));
        animation.getDataStore().saveSetting("frame-load", args[4]);

        JustAnimations.INSTANCE.getAnimations().remove(animation.getName());
        JustAnimations.INSTANCE.getAnimations().put(animation.getName(), AnimationUtil.loadAnimation(
                new File(JustAnimations.INSTANCE.getAnimationDataFolder(), animation.getName())));

        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.SETLOAD_SUCCESS,
                animation.getName(), args[4]));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.setload", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(Arrays.asList("ram", "file"));
    }
}