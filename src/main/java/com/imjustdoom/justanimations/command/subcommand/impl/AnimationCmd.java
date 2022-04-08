package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
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

public class AnimationCmd implements SubCommand {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public AnimationCmd() {
        subCommands.add(new SettingsCmd());
        subCommands.add(new GetFrameCmd());
        subCommands.add(new AddFrameCmd());
        subCommands.add(new PlayCmd());
        subCommands.add(new StopCmd());
        subCommands.add(new ConvertCmd());
        subCommands.add(new GoToFrameCmd());
        subCommands.add(new FrameSelectionCmd());
    }

    public String getName() {
        return "animation";
    }

    public String getDescription() {
        return "An animation";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (org.bukkit.entity.Player) sender)) {
            return;
        }

        if(!JustAnimations.INSTANCE.getAnimations().containsKey(args[1])) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.ANIMATION_NOT_EXISTS));
            return;
        }
        if(args.length > 2) {
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[2])) {
                    subCommand.execute(sender, args);
                    return;
                }
            }
        } else {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.SETTINGS, args[1], ""));
        }
    }

    public String[] getPermission() {
        return new String[]{"justanimations.animation", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        List<String> completions = new ArrayList<>();
        if(args.length > 2) {
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[2])) {
                    return subCommand.getTabCompletions(sender, args);
                } else {
                    completions.add(subCommand.getName());
                }
            }
        } else {
            return new ArrayList<>(JustAnimations.INSTANCE.getAnimations().keySet());
        }
        return completions;
    }
}
