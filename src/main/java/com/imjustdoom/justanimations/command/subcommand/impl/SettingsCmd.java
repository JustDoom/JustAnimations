package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SettingsCmd implements SubCommand {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public SettingsCmd() {
        subCommands.add(new SetWorldCmd());
        subCommands.add(new ToggleReverseCmd());
    }

    public String getName() {
        return "settings";
    }

    public String getDescription() {
        return "Settings for an animation";
    }

    public void execute(CommandSender sender, String[] args) {
        if(args.length == 3) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.SETTINGS, args[1], ""));
        } else if(args.length > 3) {
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[3])) {
                    subCommand.execute(sender, args);
                }
            }
        }
    }

    public String[] getPermission() {
        return new String[]{"justanimations.settings", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {

        List<String> completions = new ArrayList<>();
        if(args.length > 3) {
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[3])) {
                    return subCommand.getTabCompletions(sender, args);
                } else {
                    completions.add(subCommand.getName());
                }
            }
        }
        return completions;
    }
}