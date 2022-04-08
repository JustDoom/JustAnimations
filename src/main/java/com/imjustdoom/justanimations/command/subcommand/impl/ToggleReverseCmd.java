package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ToggleReverseCmd implements SubCommand {

    public String getName() {
        return "togglereverse";
    }

    public String getDescription() {
        return "Toggles if the animations should play in reverse when they get to the end instead of resetting";
    }

    public void execute(CommandSender sender, String[] args) {
        // TODO: add speed multiplier
        JustAnimations.INSTANCE.getAnimations().get(args[0]).getDataStore().saveSetting("reverse", !JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
        JustAnimations.INSTANCE.getAnimations().get(args[0]).setGoingReverse(false);
        JustAnimations.INSTANCE.getAnimations().get(args[0]).setReverse(!JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.TOGGLE_REVERSE,
                args[0],
                JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse()));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.togglereverse", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }
}
