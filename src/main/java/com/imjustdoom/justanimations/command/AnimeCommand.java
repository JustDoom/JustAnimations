package com.imjustdoom.justanimations.command;

import com.imjustdoom.justanimations.AnimationFrame;
import com.imjustdoom.justanimations.BlockAnimation;
import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.config.AnimeConfig;
import com.imjustdoom.justanimations.storage.YamlStorage;
import com.imjustdoom.justanimations.util.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.session.SessionManager;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class AnimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (args.length == 0) return true;

            switch (args[0].toLowerCase()) {
                case "reload":
                    AnimeConfig.load();
                    sender.sendMessage("Config reloaded");
                    break;
                case "createanimation":
                    JustAnimations.INSTANCE.getAnimations().put(args[1].toLowerCase(), new BlockAnimation(((org.bukkit.entity.Player) sender).getWorld()));
                    YamlStorage.createAnimationData(args[1].toLowerCase());
                    sender.sendMessage("Animation created");
                    break;
                case "removeanimation":
                    JustAnimations.INSTANCE.getAnimations().remove(args[1].toLowerCase());
                    sender.sendMessage("Animation removed");
                    break;
            }

            switch (args[1].toLowerCase()) {
                case "addframe":
                    org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                    Player actor = BukkitAdapter.adapt(player);
                    SessionManager manager = WorldEdit.getInstance().getSessionManager();
                    LocalSession localSession = manager.get(actor);
                    List<BlockVector> frame = new ArrayList<>();
                    FileConfiguration config = YamlStorage.getFrame(args[1]);
                    try {
                        for (int i = localSession.getSelection().getMinimumPoint().getBlockX(); i <= localSession.getSelection().getMaximumPoint().getBlockX(); i++) {
                            for (int j = localSession.getSelection().getMinimumPoint().getBlockY(); j <= localSession.getSelection().getMaximumPoint().getBlockY(); j++) {
                                for (int k = localSession.getSelection().getMinimumPoint().getBlockZ(); k <= localSession.getSelection().getMaximumPoint().getBlockZ(); k++) {
                                    Block block = player.getWorld().getBlockAt(i, j, k);
                                    frame.add(new BlockVector(i, j, k, BukkitAdapter.adapt(localSession.getSelectionWorld()), block.getBlockData()));
                                    config.createSection("blocks." + i + "." + j + "." + k);
                                    config.set("blocks." + i + "." + j + "." + k, block.getBlockData().getAsString());
                                }
                            }
                        }
                    } catch (IncompleteRegionException e) {
                        e.printStackTrace();
                    }
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).addFrame(new AnimationFrame(frame, args.length < 3 ? 20 : Integer.parseInt(args[2])));
                    YamlStorage.saveFrame(args[0], config, args.length < 3 ? 20 : Integer.parseInt(args[2]), BukkitAdapter.adapt(localSession.getSelectionWorld()));
                    sender.sendMessage("Frame added");
                    break;
                case "play":
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).play();
                    sender.sendMessage("Animation started");
                    break;
                case "stop":
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).stop();
                    sender.sendMessage("Animation stopped");
                    break;
                case "removeframe":
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).removeFrame(Integer.parseInt(args[2]));
                    sender.sendMessage("Frame removed");
                    break;
                case "editframe":
                    break;
                case "gotoframe":
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).gotoFrame(Integer.parseInt(args[2]));
                    sender.sendMessage("Frame changed");
                    break;
                case "togglereverse":
                    JustAnimations.INSTANCE.getAnimations().get(args[0]).setReverse(!JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                    sender.sendMessage("Reverse toggled to " + JustAnimations.INSTANCE.getAnimations().get(args[0]).isReverse());
                    break;
            }


        return false;
    }
}