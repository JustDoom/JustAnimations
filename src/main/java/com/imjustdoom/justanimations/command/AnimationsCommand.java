package com.imjustdoom.justanimations.command;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.BlockAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.storage.YamlStorage;
import com.imjustdoom.justanimations.api.util.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.session.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class AnimationsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.HELP));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.RELOAD));
                AnimationsConfig.load();
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.RELOAD_SUCCESS));
                return true;
            case "create":
                if (JustAnimations.INSTANCE.getAnimations().get(args[1]) != null) {
                    sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE_EXISTS));
                    return true;
                }
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE));
                JustAnimations.INSTANCE.getAnimations().put(args[1].toLowerCase(), new BlockAnimation(((org.bukkit.entity.Player) sender).getWorld()));
                YamlStorage.createAnimationData(args[1].toLowerCase(), ((org.bukkit.entity.Player) sender).getWorld());
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.CREATE_SUCCESS));
                return true;
            case "delete":
                if (JustAnimations.INSTANCE.getAnimations().get(args[1]) == null) {
                    sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE_NOT_EXISTS));
                    return true;
                }
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE));
                if (JustAnimations.INSTANCE.getAnimations().get(args[1]).isRunning()) {
                    JustAnimations.INSTANCE.getAnimations().get(args[1]).stop();
                }
                JustAnimations.INSTANCE.getAnimations().remove(args[1].toLowerCase());
                YamlStorage.deleteAnimation(args[1].toLowerCase());
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.DELETE_SUCCESS));
                return true;
        }

        if(args.length == 1) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.HELP));
            return true;
        } else if(!JustAnimations.INSTANCE.getAnimations().containsKey(args[0].toLowerCase())) {
            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.ANIMATION_NOT_EXISTS));
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "settings":
                if(args.length > 2) {
                    switch (args[2].toLowerCase()) {
                        case "togglereverse":
                            // TODO: add speed multiplier
                            YamlStorage.toggleReverse(args[0], !JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                            JustAnimations.INSTANCE.getAnimations().get(args[0]).setGoingReverse(false);
                            JustAnimations.INSTANCE.getAnimations().get(args[0]).setReverse(!JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.TOGGLE_REVERSE,
                                    JustAnimations.INSTANCE.getAnimations().get(args[0]),
                                    JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse()));
                            return true;
                        case "setworld":
                            if (args.length < 4 || Bukkit.getWorld(args[3]) == null) {
                                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_NOT_EXISTS,
                                        JustAnimations.INSTANCE.getAnimations().get(args[0]).getWorld().getName()));
                                return true;
                            }
                            if (Bukkit.getWorld(args[3]).getUID().equals(JustAnimations.INSTANCE.getAnimations().get(args[0]).getWorld().getUID())) {
                                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_IN_USE,
                                        JustAnimations.INSTANCE.getAnimations().get(args[0]).getWorld().getName()));
                                return true;
                            }
                            JustAnimations.INSTANCE.getAnimations().get(args[0]).setWorld(Bukkit.getWorld(args[3]));
                            YamlStorage.setWorld(args[0], Bukkit.getWorld(args[3]));
                            sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.WORLD_CHANGE,
                                    JustAnimations.INSTANCE.getAnimations().get(args[0]).getWorld().getName()));
                            return true;
                    }
                }
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.SETTINGS));
                return true;
            case "getframe":
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GETFRAME));
                return true;
            case "addframe":
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                Player actor = BukkitAdapter.adapt(player);
                SessionManager manager = WorldEdit.getInstance().getSessionManager();
                LocalSession localSession = manager.get(actor);
                Map<BlockVector, BlockData> frame = new HashMap<>();
                FileConfiguration config = YamlStorage.getFrame(args[0]);
                try {
                    for (int i = localSession.getSelection().getMinimumPoint().getBlockX(); i <= localSession.getSelection().getMaximumPoint().getBlockX(); i++) {
                        for (int j = localSession.getSelection().getMinimumPoint().getBlockY(); j <= localSession.getSelection().getMaximumPoint().getBlockY(); j++) {
                            for (int k = localSession.getSelection().getMinimumPoint().getBlockZ(); k <= localSession.getSelection().getMaximumPoint().getBlockZ(); k++) {
                                Block block = player.getWorld().getBlockAt(i, j, k);
                                frame.put(new BlockVector(i, j, k), block.getBlockData());
                                config.createSection("blocks." + i + "." + j + "." + k);
                                config.set("blocks." + i + "." + j + "." + k, block.getBlockData().getAsString());
                            }
                        }
                    }
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).addFrame(JustAnimations.INSTANCE.getAnimations().get(args[0]).getFrames().size(), new AnimationFrame(frame, args.length < 3 ? 20 : Integer.parseInt(args[2])));
                YamlStorage.saveFrame(args[0], config, args.length < 3 ? 20 : Integer.parseInt(args[2]));
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.ADDFRAME));
                return true;
            case "play":
                if (JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.PLAY_ANIMATION_RUNNING));
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).play();
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.PLAY_ANIMATION));
                return true;
            case "stop":
                if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.STOP_ANIMATION_NOT_RUNNING));
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).stop();
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.STOP_ANIMATION));
                return true;
            case "removeframe":
                // TODO: make this work
                JustAnimations.INSTANCE.getAnimations().get(args[0]).removeFrame(Integer.parseInt(args[2]));
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.REMOVE_FRAME,
                        JustAnimations.INSTANCE.getAnimations().get(args[0]),
                        args[2]));
                return true;
            case "editframe":
                return true;
            case "gotoframe":
                if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]))) {
                    sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GO_TO_FRAME_NOT_EXISTS,
                            JustAnimations.INSTANCE.getAnimations().get(args[0]),
                            args[2]));
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]));
                sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.GO_TO_FRAME,
                        JustAnimations.INSTANCE.getAnimations().get(args[0]),
                        args[2]));
                return true;
        }

        return false;
    }
}