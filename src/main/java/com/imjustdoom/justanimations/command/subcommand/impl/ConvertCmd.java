package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.api.util.PermissionUtil;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.storage.impl.MultipleFileFrameStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConvertCmd implements SubCommand {

    public String getName() {
        return "convert";
    }

    public String getDescription() {
        return "Converts the current animations file save system";
    }

    public void execute(CommandSender sender, String[] args) {

        if(!PermissionUtil.hasPermission(Arrays.asList(getPermission()), (org.bukkit.entity.Player) sender)) {
            return;
        }

        if(JustAnimations.INSTANCE.getConverting().contains(args[1])) {
            sender.sendMessage("§cAlready converting!");
            return;
        }

        // TODO: add warning and confirmation command
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CONVERTING,
                args[1],
                JustAnimations.INSTANCE.getAnimations().get(args[1]).getDataStore() instanceof MultipleFileFrameStorage ? "singlefile" : "multiplefile"));

        JustAnimations.INSTANCE.getAnimations().get(args[1]).stop();

        IAnimation animation = JustAnimations.INSTANCE.getAnimations().get(args[1]);
        JustAnimations.INSTANCE.getAnimations().remove(args[1]);

        JustAnimations.INSTANCE.getConverting().add(animation.getName());

        Bukkit.getScheduler().runTaskAsynchronously(JustAnimations.INSTANCE, () -> {

            animation.setDataStore(
                    animation.getDataStore().convertFrames(animation));

            String path = animation.getDataStore().getDataFolder();

            JustAnimations.INSTANCE.getAnimations().put(args[1],
                    AnimationUtil.loadAnimation(new File(path)));

            JustAnimations.INSTANCE.getConverting().remove(args[1]);

            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CONVERTING_SUCCESS,
                    args[1],
                    JustAnimations.INSTANCE.getAnimations().get(args[1]).getDataStore() instanceof MultipleFileFrameStorage ? "multiplefile" : "singlefile"));
        });
    }

    public String[] getPermission() {
        return new String[]{"justanimations.convert", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }

    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}