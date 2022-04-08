package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConvertCmd implements SubCommand {

    public String getName() {
        return "convert";
    }

    public String getDescription() {
        return "Converts the current animations file save system";
    }

    public void execute(CommandSender sender, String[] args) {
        // TODO: add warning and confirmation command
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CONVERTING));
        JustAnimations.INSTANCE.getAnimations().get(args[0]).stop();
        Bukkit.getScheduler().runTaskAsynchronously(JustAnimations.INSTANCE, () -> {
            JustAnimations.INSTANCE.getAnimations().get(args[0]).setDataStore(
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).getDataStore().convertFrames());
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CONVERTING_SUCCESS));
        });
    }

    public String[] getPermission() {
        return new String[]{"justanimations.convert", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }
}