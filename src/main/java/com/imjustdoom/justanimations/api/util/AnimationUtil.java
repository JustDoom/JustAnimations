package com.imjustdoom.justanimations.api.util;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.animation.impl.BlockAnimation;
import com.imjustdoom.justanimations.storage.impl.MultipleFileFrameStorage;
import com.imjustdoom.justanimations.storage.impl.SingleFileFrameStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimationUtil {

    public static Location blockVectorToLocation(IAnimation animation, BlockVector vector) {
        return new Location(animation.getWorld(), vector.getX(), vector.getY(), vector.getZ());
    }

    public static AnimationFrame getFrame(IAnimation animation, String frame) {
        ConfigurationSection newFrame = animation.getDataStore().getFrame(frame);
        if(newFrame == null) return null;
        Map<BlockVector, BlockData> blockVectors = new HashMap<>();

        if (newFrame.contains("blocks")) {
            for (String key : newFrame.getConfigurationSection("blocks").getKeys(false)) {
                for (String key1 : newFrame.getConfigurationSection("blocks." + key).getKeys(false)) {
                    for (String key2 : newFrame.getConfigurationSection("blocks." + key + "." + key1).getKeys(false)) {
                        BlockData blockData;
                        try {
                            blockData = Bukkit.getServer().createBlockData(newFrame.getString("blocks." + key + "." + key1 + "." + key2));
                        } catch (Exception e) {
                            blockData = Material.AIR.createBlockData();
                        }
                        blockVectors.put(new BlockVector(Integer.valueOf(key), Integer.valueOf(key1), Integer.valueOf(key2)), blockData);
                    }
                }
            }
        }

        return new AnimationFrame(blockVectors, newFrame.getInt("delay"));
    }

    public static IAnimation loadAnimation(File animation) {
        File settings = new File(animation.getPath(), "settings.yml");
        FileConfiguration settingsYml = YamlConfiguration.loadConfiguration(settings);

        IAnimation blockAnimation = new BlockAnimation();
        blockAnimation.setSaveToRam(settingsYml.getString("frame-load").equalsIgnoreCase("ram"));

        blockAnimation.setDataStore(new File(animation.getPath(), "frames.yml").exists()
                ? new SingleFileFrameStorage(animation.getName())
                : new MultipleFileFrameStorage(animation.getName()));
        blockAnimation.setFrameCount(blockAnimation.getDataStore().getFrameCount());
        blockAnimation.setName(animation.getName().replace(".yml", ""));
        blockAnimation.setReverse(settingsYml.getBoolean("reverse"));
        blockAnimation.setRandomFrame(settingsYml.getBoolean("random-frame"));
        if(Bukkit.getWorld(UUID.fromString(settingsYml.getString("world"))) == null) {
            JustAnimations.INSTANCE.getLogger().warning("World " + settingsYml.getString("world") + " not found for animation " + animation.getName());
            return null;
        }
        blockAnimation.setWorld(Bukkit.getWorld(UUID.fromString(settingsYml.getString("world"))));
        getFrames(blockAnimation, animation);

        if (blockAnimation.getFrames().size() > 0) blockAnimation.play();

        return blockAnimation;
    }

    public static void getFrames(IAnimation blockAnimation, File animation) {
        if(!blockAnimation.isRandomFrame()) {
            // TODO: combined these two?
            if(blockAnimation.getDataStore() instanceof SingleFileFrameStorage) {
                for(int i = 0; i < blockAnimation.getFrameCount(); i++) {
                    blockAnimation.getFrames().put(i, getFrame(blockAnimation, String.valueOf(i)));
                }
            } else {
                for (File frame : animation.listFiles()) {
                    if (!frame.getName().endsWith(".yml")
                            || frame.getName().startsWith("settings")
                            || frame.getName().startsWith("frames")) continue;

                    String frameName = frame.getName().replace(".yml", "");
                    blockAnimation.addFrame(frameName, getFrame(blockAnimation, frameName));
                }
            }
        } else {
            AnimationFrame animationFrame = AnimationUtil.getFrame(blockAnimation, "0");
            if (animationFrame != null) blockAnimation.addFrame("0", animationFrame);
        }
    }
}