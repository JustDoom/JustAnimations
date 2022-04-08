package com.imjustdoom.justanimations.command.subcommand;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    String getName();

    String getDescription();

    void execute(CommandSender sender, String[] args);

    String[] getPermission();

    List<SubCommand> getSubCommands();
}
