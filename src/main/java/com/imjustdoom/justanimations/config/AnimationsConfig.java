package com.imjustdoom.justanimations.config;

import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.animation.BlockAnimation;
import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.storage.YamlStorage;
import com.imjustdoom.justanimations.util.BlockVector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class AnimationsConfig {

    public static FileConfiguration config;

    public static class Settings {
        public static int FRAMES_LOADED;
    }

    public static void load() {

        JustAnimations.INSTANCE.reloadConfig();
        config = JustAnimations.INSTANCE.getConfig();

        Settings.FRAMES_LOADED = config.getInt("settings.frames-loaded");

        for(IAnimation animation : JustAnimations.INSTANCE.getAnimations().values()) {
            animation.stop();
        }

        JustAnimations.INSTANCE.getAnimations().clear(); // TODO: make sure it stops animations
        if(new File(YamlStorage.dataFolder).exists()) {
            for (File animation : new File(YamlStorage.dataFolder).listFiles()) {
                BlockAnimation blockAnimation = new BlockAnimation();

                blockAnimation.setAnimationDir(animation);
                blockAnimation.setFrameCount(animation.listFiles().length);

                File settings = new File(animation.getPath() + "/settings.yml");
                FileConfiguration settingsYml = YamlConfiguration.loadConfiguration(settings);
                blockAnimation.setReverse(settingsYml.getBoolean("reverse"));
                blockAnimation.setWorld(Bukkit.getWorld(UUID.fromString(settingsYml.getString("world"))));

                int i = 0;
                for (int j = 0; j < animation.listFiles().length; j++) {
                    File frame = new File(animation.getPath() + "/" + j + ".yml");
                    if(frame.getName().startsWith("settings")) continue;
                    if(i >= Settings.FRAMES_LOADED) break;
                    i++;
                    FileConfiguration config = YamlConfiguration.loadConfiguration(frame);
                    Map<BlockVector, BlockData> blockVectors = new HashMap<>();

                    if(!config.contains("blocks")) continue;
                    for (String key : config.getConfigurationSection("blocks").getKeys(false)) {
                        for (String key1 : config.getConfigurationSection("blocks." + key).getKeys(false)) {
                            for (String key2 : config.getConfigurationSection("blocks." + key + "." + key1).getKeys(false)) {
                                BlockData blockData;
                                try {
                                    blockData = Bukkit.getServer().createBlockData(config.getString("blocks." + key + "." + key1 + "." + key2));
                                } catch (Exception e) {
                                    blockData = Material.AIR.createBlockData();
                                }
                                blockVectors.put(new BlockVector(Integer.valueOf(key), Integer.valueOf(key1), Integer.valueOf(key2)), blockData);
                            }
                        }
                    }

                    AnimationFrame animationFrame = new AnimationFrame(blockVectors);
                    animationFrame.setDelay(config.getInt("delay"));
                    blockAnimation.addFrame(j, animationFrame);
                }
                blockAnimation.setLoadedFrameCount(i + 1);
                JustAnimations.INSTANCE.getAnimations().put(animation.getName(), blockAnimation);
                if(blockAnimation.getFrames().size() > 0) blockAnimation.play();
            }
        }
    }
}