package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class GetFrameCmd implements SubCommand {

    public String getName() {
        return "getframe";
    }

    public String getDescription() {
        return "Gets the current frame of an animation";
    }

    public void execute(CommandSender sender, String[] args) {
        // TODO: actually get the frame of the animation
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GETFRAME,
                args[1], JustAnimations.INSTANCE.getAnimations().get(args[1]).getFrame()));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.getframe", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}