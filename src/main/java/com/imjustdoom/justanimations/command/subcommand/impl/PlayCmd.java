package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PlayCmd implements SubCommand {

    public String getName() {
        return "play";
    }

    public String getDescription() {
        return "Plays the current animation";
    }

    public void execute(CommandSender sender, String[] args) {
        if (JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.PLAY_ANIMATION_RUNNING));
            return;
        }
        JustAnimations.INSTANCE.getAnimations().get(args[0]).play();
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.PLAY_ANIMATION));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.play", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }
}