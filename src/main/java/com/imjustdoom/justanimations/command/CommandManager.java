package com.imjustdoom.justanimations.command;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.command.subcommand.impl.AnimationCmd;
import com.imjustdoom.justanimations.command.subcommand.impl.CreateCmd;
import com.imjustdoom.justanimations.command.subcommand.impl.DeleteCmd;
import com.imjustdoom.justanimations.command.subcommand.impl.ReloadCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final List<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        subcommands.add(new ReloadCmd());
        subcommands.add(new CreateCmd());
        subcommands.add(new DeleteCmd());
        subcommands.add(new AnimationCmd());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return true;
        }

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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        for (SubCommand subcommand : subcommands) {
            if (subcommand.getName().equalsIgnoreCase(args[0])) {
                return subcommand.getTabCompletions(sender, args);
            } else {
                completions.add(subcommand.getName());
            }
        }

        return completions;
    }

    public String[] getPermission() {
        return new String[]{"justanimations.command", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return subcommands;
    }
}
