package com.imjustdoom.justanimations.config;

import com.imjustdoom.justanimations.AnimationFrame;
import com.imjustdoom.justanimations.BlockAnimation;
import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.storage.YamlStorage;
import com.imjustdoom.justanimations.util.BlockVector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AnimationsConfig {

    public static String WEBHOOK_URL;

    public static class Messages {
        public static String STAR_DESTROYED;
        public static String PLAYER_ELIMINATED;
    }

    public static void load() {

        if(new File(YamlStorage.dataFolder).exists()) {
            for (File animation : new File(YamlStorage.dataFolder).listFiles()) {
                BlockAnimation blockAnimation = new BlockAnimation();

                File[] frames = animation.listFiles();
                Arrays.sort(frames);
                for (File frame : frames) {
                    if(frame.getName().startsWith("settings")) {
                        FileConfiguration settings = YamlConfiguration.loadConfiguration(frame);
                        blockAnimation.setReverse(settings.getBoolean("reverse"));
                        blockAnimation.setWorld(Bukkit.getWorld(UUID.fromString(settings.getString("world"))));
                        continue;
                    }
                    FileConfiguration config = YamlConfiguration.loadConfiguration(frame);
                    List<BlockVector> blockVectors = new ArrayList<>();

                    if(!config.contains("blocks")) continue;
                    for (String key : config.getConfigurationSection("blocks").getKeys(false)) {
                        for (String key1 : config.getConfigurationSection("blocks." + key).getKeys(false)) {
                            for (String key2 : config.getConfigurationSection("blocks." + key + "." + key1).getKeys(false)) {
                                BlockData blockData = null;
                                try {
                                    blockData = Bukkit.getServer().createBlockData(config.getString("blocks." + key + "." + key1 + "." + key2));
                                } catch (Exception e) {
                                    blockData = Material.AIR.createBlockData();
                                }
                                blockVectors.add(new BlockVector(Integer.valueOf(key), Integer.valueOf(key1), Integer.valueOf(key2),
                                        blockData));
                            }
                        }
                    }

                    AnimationFrame animationFrame = new AnimationFrame(blockVectors);
                    animationFrame.setDelay(config.getInt("delay"));
                    blockAnimation.addFrame(animationFrame);
                }
                JustAnimations.INSTANCE.getAnimations().put(animation.getName(), blockAnimation);
                if(blockAnimation.getFrames().size() > 0) blockAnimation.play();
            }
        }
    }
}