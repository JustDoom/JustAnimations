package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GetFrameCmd implements SubCommand {

    public String getName() {
        return "getframe";
    }

    public String getDescription() {
        return "Gets the current frame of an animation";
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GETFRAME));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.getframe", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }
}