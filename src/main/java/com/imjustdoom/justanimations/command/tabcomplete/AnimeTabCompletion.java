package com.imjustdoom.justanimations.command.tabcomplete;

import com.imjustdoom.justanimations.JustAnimations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AnimeTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();

        if(args.length == 1){
            result.add("reload");
            result.add("createanimation");
            result.add("removeanimation");
            result.addAll(JustAnimations.INSTANCE.getAnimations().keySet());
            return result;
        }

        if(args[0].equalsIgnoreCase("removeanimation")){
            return new ArrayList<>(JustAnimations.INSTANCE.getAnimations().keySet());
        }

        if(args.length == 2 && !args[0].equalsIgnoreCase("createanimation")
                && !args[0].equalsIgnoreCase("removeanimation")
                && !args[0].equalsIgnoreCase("reload")){
            result.add("addframe");
            result.add("play");
            result.add("stop");
            result.add("gotoframe");
            return result;
        }

        switch (args[1].toLowerCase()){
            case "gotoframe":
                return JustAnimations.INSTANCE.getAnimations().get(args[0]).getFrames().keySet().stream().map(String::valueOf).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }

        return null;
    }
}