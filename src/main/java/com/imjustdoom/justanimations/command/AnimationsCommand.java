package com.imjustdoom.justanimations.command;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.BlockAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.imjustdoom.justanimations.storage.YamlStorage;
import com.imjustdoom.justanimations.util.BlockVector;
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
        if (args.length == 0) return true;

        switch (args[0].toLowerCase()) {
            case "reload":
                AnimationsConfig.load();
                sender.sendMessage("Config reloaded");
                return true;
            case "create":
                if (JustAnimations.INSTANCE.getAnimations().get(args[1]) != null) {
                    sender.sendMessage("Animation with this name already exists");
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().put(args[1].toLowerCase(), new BlockAnimation(((org.bukkit.entity.Player) sender).getWorld()));
                YamlStorage.createAnimationData(args[1].toLowerCase(), ((org.bukkit.entity.Player) sender).getWorld());
                sender.sendMessage("Animation created");
                return true;
            case "delete":
                if (JustAnimations.INSTANCE.getAnimations().get(args[1]) == null) {
                    sender.sendMessage("Animation with this name doesn't exist");
                    return true;
                }
                if (JustAnimations.INSTANCE.getAnimations().get(args[1]).isRunning()) {
                    JustAnimations.INSTANCE.getAnimations().get(args[1]).stop();
                }
                JustAnimations.INSTANCE.getAnimations().remove(args[1].toLowerCase());
                YamlStorage.deleteAnimation(args[1].toLowerCase());
                sender.sendMessage("Animation removed");
                return true;
        }

        if(!JustAnimations.INSTANCE.getAnimations().containsKey(args[0].toLowerCase())) {
            sender.sendMessage("Animation with this name doesn't exist");
            return true;
        } else if(args.length == 1) {
            sender.sendMessage("Please specify an option\naddframe - add frame to animation\nplay - start animation\nstop - stop animation etc");
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
                            sender.sendMessage("Reverse toggled to " + JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                            return true;
                        case "setworld":
                            if (args.length < 4 || Bukkit.getWorld(args[3]) == null) {
                                sender.sendMessage("World does not exist");
                                return true;
                            }
                            if (Bukkit.getWorld(args[3]).getUID().equals(JustAnimations.INSTANCE.getAnimations().get(args[0]).getWorld().getUID())) {
                                sender.sendMessage("Animation is already in this world");
                                return true;
                            }
                            JustAnimations.INSTANCE.getAnimations().get(args[0]).setWorld(Bukkit.getWorld(args[3]));
                            YamlStorage.setWorld(args[0], Bukkit.getWorld(args[3]));
                            sender.sendMessage("World set to " + args[3]);
                            return true;
                    }
                }
                sender.sendMessage("Settings for animation\nWorld: "
                        + JustAnimations.INSTANCE.getAnimations().get(args[0].toLowerCase()).getWorld().getName()
                        + "\nFrames: " + JustAnimations.INSTANCE.getAnimations().get(args[0].toLowerCase()).getFrames().size()
                        + "\nRunning: " + JustAnimations.INSTANCE.getAnimations().get(args[0].toLowerCase()).isRunning() + "\nReverse: "
                        + JustAnimations.INSTANCE.getAnimations().get(args[0].toLowerCase()).isReverse());
                return true;
            case "getframe":
                sender.sendMessage("The current frame of the animation is " + JustAnimations.INSTANCE.getAnimations().get(args[0].toLowerCase()).getFrame());
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
                sender.sendMessage("Frame added");
                return true;
            case "play":
                if (JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    sender.sendMessage("Animation is already running");
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).play();
                sender.sendMessage("Animation started");
                return true;
            case "stop":
                if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    sender.sendMessage("Animation is not running");
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).stop();
                sender.sendMessage("Animation stopped");
                return true;
            case "removeframe":
                // TODO: make this work
                JustAnimations.INSTANCE.getAnimations().get(args[0]).removeFrame(Integer.parseInt(args[2]));
                sender.sendMessage("Frame removed");
                return true;
            case "editframe":
                return true;
            case "gotoframe":
                if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]))) {
                    sender.sendMessage("Frame does not exist");
                    return true;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]));
                sender.sendMessage("Frame changed");
                return true;
        }


        return false;
    }
}