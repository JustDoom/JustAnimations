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

public class DeleteCmd implements SubCommand {

    public String getName() {
        return "delete";
    }

    public String getDescription() {
        return "Deletes an animation";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (org.bukkit.entity.Player) sender)) {
            return;
        }

        if(args.length == 1) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE_NOT_EXISTS,
                    "invalid", ""));
            return;
        }

        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);

        if (animation == null) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE_NOT_EXISTS));
            return;
        }
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE));
        animation.stop();
        animation.getDataStore().deleteAnimation(args[1].toLowerCase());
        JustAnimations.INSTANCE.getAnimations().remove(args[1].toLowerCase());
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE_SUCCESS));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.delete", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        if(args.length > 2) return Collections.emptyList();
        return new ArrayList<>(JustAnimations.INSTANCE.getAnimations().keySet());
    }
}
