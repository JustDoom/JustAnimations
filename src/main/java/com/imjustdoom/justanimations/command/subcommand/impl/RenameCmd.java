package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RenameCmd implements SubCommand {

    public String getName() {
        return "rename";
    }

    public String getDescription() {
        return "Renames the animation";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return;
        }

        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);

        if(args.length == 3) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX
                    + AnimationsConfig.Messages.RENAME_NO_VALUE));
            return;
        }

        if(JustAnimations.INSTANCE.getAnimations().containsKey(args[3].toLowerCase())){
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX
                    + AnimationsConfig.Messages.RENAME_EXISTS));
            return;
        }

        animation.setName(args[3].toLowerCase());
        JustAnimations.INSTANCE.getAnimations().remove(args[1]);
        JustAnimations.INSTANCE.getAnimations().put(animation.getName(), animation);

        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.RENAME_SUCCESS,
                args[1],
                args[3]));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.rename", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}