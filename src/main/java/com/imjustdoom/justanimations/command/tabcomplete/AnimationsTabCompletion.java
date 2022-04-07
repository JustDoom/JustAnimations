package com.imjustdoom.justanimations.command.tabcomplete;

import com.imjustdoom.justanimations.JustAnimations;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.List;

public class AnimationsTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();

        if(args.length == 1){
            result.add("reload");
            result.add("create");
            result.add("delete");
            result.addAll(JustAnimations.INSTANCE.getAnimations().keySet());
            return result;
        }

        if(args[0].equalsIgnoreCase("delete")){
            return new ArrayList<>(JustAnimations.INSTANCE.getAnimations().keySet());
        }

        if(args.length == 3){
            switch (args[0].toLowerCase()){
                case "create":
                    result.add("singlefile");
                    result.add("multiplefile");
                    return result;
            }
        }

        if(args.length == 2 && !args[0].equalsIgnoreCase("create")
                && !args[0].equalsIgnoreCase("delete")
                && !args[0].equalsIgnoreCase("reload") && JustAnimations.INSTANCE.getAnimations().get(args[0]) != null){
            result.add("addframe");
            result.add("play");
            result.add("stop");
            result.add("settings");
            result.add("gotoframe");
            result.add("getframe");
            result.add("convert");
            return result;
        }

        if(args.length == 3) {
            switch (args[1].toLowerCase()) {
                case "settings":
                    result.add("togglereverse");
                    result.add("setworld");
                    return result;
                case "gotoframe":
                    if (JustAnimations.INSTANCE.getAnimations().get(args[0]) == null) return null;
                    List<String> frames = new ArrayList<>();
                    int i = 0;
                    while(i <= JustAnimations.INSTANCE.getAnimations().get(args[0]).getFrameCount() - 1){
                        frames.add(String.valueOf(i));
                        i++;
                    }
                    return frames;
            }
        }

        if(args.length == 4) {
            switch (args[2].toLowerCase()) {
                case "setworld":
                    return Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            }
        }

        return null;
    }
}