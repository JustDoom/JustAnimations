package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetDelayCmd implements SubCommand {

    public String getName() {
        return "setdelay";
    }

    public String getDescription() {
        return "Sets the delay for the animation frame";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return;
        }

        if (args.length == 4) {
            sender.sendMessage("Please specify a valid frame and delay value");
            return;
        }

        int delay = args.length == 5 ? 20 : Integer.parseInt(args[5]);
        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);

        animation.getDataStore().setFrameSetting(args[4], "delay", delay);

        animation.reload();

        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_CHANGE,
                args[1],
                JustAnimations.INSTANCE.getAnimations().get(args[1]).getWorld().getName()));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.setdelay", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        List<String> frames = new ArrayList<>();
        int i = 0;
        while(i <= JustAnimations.INSTANCE.getAnimations().get(args[1]).getFrameCount() - 1){
            frames.add(String.valueOf(i));
            i++;
        }
        return frames;
    }
}
