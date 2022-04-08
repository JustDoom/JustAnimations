package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GoToFrameCmd implements SubCommand {

    public String getName() {
        return "gotoframe";
    }

    public String getDescription() {
        return "Sets the current frame of an animation";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]))) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GO_TO_FRAME_NOT_EXISTS,
                    args[0],
                    args[2]));
            return;
        }
        JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]));
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GO_TO_FRAME,
                args[0],
                args[2]));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.gotoframe", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }
}