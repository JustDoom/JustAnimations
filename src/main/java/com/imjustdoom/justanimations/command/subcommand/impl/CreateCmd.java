package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.impl.BlockAnimation;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.storage.DataStore;
import com.imjustdoom.justanimations.storage.impl.MultipleFileFrameStorage;
import com.imjustdoom.justanimations.storage.impl.SingleFileFrameStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateCmd implements SubCommand {

    public String getName() {
        return "create";
    }

    public String getDescription() {
        return "Creates a new animation";
    }

    // TODO: make this better
    public void execute(CommandSender sender, String[] args) {

        if(args.length == 1) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE_INVALID));
            return;
        }
        if (JustAnimations.INSTANCE.getAnimations().get(args[1]) != null) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE_EXISTS));
            return;
        }
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE));
        DataStore dataStore = args.length == 2 || !args[2].equalsIgnoreCase("singlefile") ? new MultipleFileFrameStorage(args[1].toLowerCase()) : new SingleFileFrameStorage(args[1].toLowerCase());
        String frameLoad = args.length > 3 ? args[3].toLowerCase() : "file";
        dataStore.createAnimationData(args[1].toLowerCase(), ((org.bukkit.entity.Player) sender).getWorld(), frameLoad); // TODO: ram load thing
        JustAnimations.INSTANCE.getAnimations().put(args[1].toLowerCase(), new BlockAnimation(((org.bukkit.entity.Player) sender).getWorld(), dataStore, args[1].toLowerCase()));
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE_SUCCESS));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.create", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (Player) sender)) {
            return Collections.emptyList();
        }
        if(args.length == 3) {
            List<String> completions = new ArrayList<>();
            completions.add("singlefile");
            completions.add("multiplefile");
            return completions;
        }
        return Collections.emptyList();
    }
}
