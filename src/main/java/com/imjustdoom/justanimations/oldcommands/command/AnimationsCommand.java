package com.imjustdoom.justanimations.oldcommands.command;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnimationsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.HELP));
            return true;
        }

        if(args.length == 1) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.HELP));
            return true;
        } else if(!JustAnimations.INSTANCE.getAnimations().containsKey(args[0].toLowerCase())) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.ANIMATION_NOT_EXISTS));
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "removeframe":
                // TODO: make this work
                JustAnimations.INSTANCE.getAnimations().get(args[0]).removeFrame(Integer.parseInt(args[2]));
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.REMOVE_FRAME,
                        args[0],
                        args[2]));
                return true;
        }

        return false;
    }
}