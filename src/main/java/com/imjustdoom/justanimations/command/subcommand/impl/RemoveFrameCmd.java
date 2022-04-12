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

public class RemoveFrameCmd implements SubCommand {

    public String getName() {
        return "removeframe";
    }

    public String getDescription() {
        return "Removes the animation";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return;
        }

        //TODO: check if frame actually exists
        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);

        if(args.length == 3) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX
                    + AnimationsConfig.Messages.REMOVE));
            return;
        }
        if(animation.getDataStore().getFrame(args[3]) == null) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX
                    + AnimationsConfig.Messages.REMOVE_NOT_EXISTS));
            return;
        }

        animation.stop();
        animation.getDataStore().removeFrame(args[3]);
        animation.reload();

        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.REMOVE_SUCCESS,
                args[1],
                args[3]));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.remove", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        if (JustAnimations.INSTANCE.getAnimations().get(args[1]) == null) return Collections.emptyList();
        List<String> frames = new ArrayList<>();
        int i = 0;
        while(i <= JustAnimations.INSTANCE.getAnimations().get(args[1]).getFrameCount() - 1){
            frames.add(String.valueOf(i));
            i++;
        }
        return frames;
    }
}