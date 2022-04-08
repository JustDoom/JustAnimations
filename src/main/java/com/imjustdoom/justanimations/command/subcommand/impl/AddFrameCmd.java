package com.imjustdoom.justanimations.command.subcommand.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.api.util.BlockVector;
import com.imjustdoom.justanimations.api.util.TranslationUtil;
import com.imjustdoom.justanimations.command.subcommand.SubCommand;
import com.imjustdoom.justanimations.config.AnimationsConfig;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.session.SessionManager;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFrameCmd implements SubCommand {

    public String getName() {
        return "addframe";
    }

    public String getDescription() {
        return "Adds a frame to an animation";
    }

    public void execute(CommandSender sender, String[] args) {
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        Player actor = BukkitAdapter.adapt(player);
        SessionManager manager = WorldEdit.getInstance().getSessionManager();
        LocalSession localSession = manager.get(actor);
        Map<BlockVector, BlockData> frame = new HashMap<>();
        ConfigurationSection section = new MemoryConfiguration();
        try {
            for (int i = localSession.getSelection().getMinimumPoint().getBlockX(); i <= localSession.getSelection().getMaximumPoint().getBlockX(); i++) {
                for (int j = localSession.getSelection().getMinimumPoint().getBlockY(); j <= localSession.getSelection().getMaximumPoint().getBlockY(); j++) {
                    for (int k = localSession.getSelection().getMinimumPoint().getBlockZ(); k <= localSession.getSelection().getMaximumPoint().getBlockZ(); k++) {
                        Block block = player.getWorld().getBlockAt(i, j, k);
                        frame.put(new BlockVector(i, j, k), block.getBlockData());
                        section.createSection(i + "." + j + "." + k);
                        section.set(i + "." + j + "." + k, block.getBlockData().getAsString().replaceFirst("minecraft:", ""));
                    }
                }
            }
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        }

        JustAnimations.INSTANCE.getAnimations().get(args[0]).setFrameCount(JustAnimations.INSTANCE.getAnimations().get(args[0]).getFrameCount() + 1);
        if(JustAnimations.INSTANCE.getAnimations().get(args[0]).getFrameCount() == 0) JustAnimations.INSTANCE.getAnimations().get(args[0]).addFrame("0", new AnimationFrame(frame, args.length < 3 ? 20 : Integer.parseInt(args[2])));
        JustAnimations.INSTANCE.getAnimations().get(args[0]).getDataStore().saveFrame(args[0], section, args.length < 3 ? 20 : Integer.parseInt(args[2]));
        sender.sendMessage(TranslationUtil.translatePlaceholders(AnimationsConfig.PREFIX + AnimationsConfig.Messages.ADDFRAME));
    }

    public String[] getPermission() {
        return new String[]{"justanimations.addframe", "justanimations.admin"};
    }

    public List<SubCommand> getSubCommands() {
        return null;
    }
}