package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class StopCmd implements SubCommand {

    public String getName() {
        return "stop";
    }

    public String getDescription() {
        return "Stops the current animation";
    }

    public void execute(CommandSender sender, String[] args) {

        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);

        if (!animation.isRunning()) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.STOP_ANIMATION_NOT_RUNNING,
                    args[1], ""));
            return;
        }
        animation.stop();
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.STOP_ANIMATION,
                args[1], ""));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.stop", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}