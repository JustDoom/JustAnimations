package com.imjustdoom.justanimations.command;

import com.imjustdoom.justanimations.AnimationFrame;
import com.imjustdoom.justanimations.BlockAnimation;
import com.imjustdoom.justanimations.JustAnimations;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

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
                if (JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).stop();
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
            case "addframe":
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                Player actor = BukkitAdapter.adapt(player);
                SessionManager manager = WorldEdit.getInstance().getSessionManager();
                LocalSession localSession = manager.get(actor);
                List<BlockVector> frame = new ArrayList<>();
                FileConfiguration config = YamlStorage.getFrame(args[0]);
                try {
                    for (int i = localSession.getSelection().getMinimumPoint().getBlockX(); i <= localSession.getSelection().getMaximumPoint().getBlockX(); i++) {
                        for (int j = localSession.getSelection().getMinimumPoint().getBlockY(); j <= localSession.getSelection().getMaximumPoint().getBlockY(); j++) {
                            for (int k = localSession.getSelection().getMinimumPoint().getBlockZ(); k <= localSession.getSelection().getMaximumPoint().getBlockZ(); k++) {
                                Block block = player.getWorld().getBlockAt(i, j, k);
                                frame.add(new BlockVector(i, j, k, block.getBlockData()));
                                config.createSection("blocks." + i + "." + j + "." + k);
                                config.set("blocks." + i + "." + j + "." + k, block.getBlockData().getAsString());
                            }
                        }
                    }
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).addFrame(new AnimationFrame(frame, args.length < 3 ? 20 : Integer.parseInt(args[2])));
                YamlStorage.saveFrame(args[0], config, args.length < 3 ? 20 : Integer.parseInt(args[2]));
                sender.sendMessage("Frame added");
                break;
            case "play":
                if (JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    sender.sendMessage("Animation is already running");
                    break;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).play();
                sender.sendMessage("Animation started");
                break;
            case "stop":
                if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).isRunning()) {
                    sender.sendMessage("Animation is not running");
                    break;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).stop();
                sender.sendMessage("Animation stopped");
                break;
            case "removeframe":
                // TODO: make this work
                JustAnimations.INSTANCE.getAnimations().get(args[0]).removeFrame(Integer.parseInt(args[2]));
                sender.sendMessage("Frame removed");
                break;
            case "editframe":
                break;
            case "gotoframe":
                if (!JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]))) {
                    sender.sendMessage("Frame does not exist");
                    break;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]));
                sender.sendMessage("Frame changed");
                break;
            case "togglereverse":
                // TODO: add speed multiplier
                YamlStorage.toggleReverse(args[0], !JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                JustAnimations.INSTANCE.getAnimations().get(args[0]).setGoingReverse(false);
                JustAnimations.INSTANCE.getAnimations().get(args[0]).setReverse(!JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                sender.sendMessage("Reverse toggled to " + JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                break;
            case "setworld":
                if(args.length < 3 || Bukkit.getWorld(args[2]) == null) {
                    sender.sendMessage("World does not exist");
                    break;
                }
                if(Bukkit.getWorld(args[2]).getUID().equals(JustAnimations.INSTANCE.getAnimations().get(args[0]).getWorld().getUID())) {
                    sender.sendMessage("Animation is already in this world");
                    break;
                }
                JustAnimations.INSTANCE.getAnimations().get(args[0]).setWorld(Bukkit.getWorld(args[2]));
                YamlStorage.setWorld(args[0], Bukkit.getWorld(args[2]));
                sender.sendMessage("World set to " + args[2]);
                break;
        }


        return false;
    }
}