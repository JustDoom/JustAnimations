package com.imjustdoom.justanimations.command;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.command.subcommand.impl.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private final List<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        subcommands.add(new ReloadCmd());
        subcommands.add(new CreateCmd());
        subcommands.add(new DeleteCmd());
        subcommands.add(new AnimationCmd());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage("§c/justanimations reload");
        } else {
            if(JustAnimations.INSTANCE.getAnimations().containsKey(args[0])) {
                for(SubCommand subcommand : subcommands) { // TODO: probably a better way to do this
                    if(subcommand.getName().equalsIgnoreCase("animation")) {
                        subcommand.execute(sender, args);
                        return true;
                    }
                }
            } else {
                for (SubCommand subcommand : subcommands) {
                    if (subcommand.getName().equalsIgnoreCase(args[0])) {
                        subcommand.execute(sender, args);
                        return true;
                    }
                }
                sender.sendMessage("§cUnknown subcommand");
            }
        }
        return false;
    }

    public List<SubCommand> getSubCommands() {
        return subcommands;
    }
}
