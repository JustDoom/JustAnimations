package com.imjustdoom.justanimations.config;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.BlockAnimation;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.storage.YamlStorage;
import com.imjustdoom.justanimations.util.BlockVector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimationsConfig {

    public static FileConfiguration config;

    public static class Settings {
    }

    public static void load() {

        JustAnimations.INSTANCE.reloadConfig();
        config = JustAnimations.INSTANCE.getConfig();

        for (IAnimation animation : JustAnimations.INSTANCE.getAnimations().values()) {
            animation.stop();
        }

        JustAnimations.INSTANCE.getAnimations().clear(); // TODO: make sure it stops animations
        if (new File(YamlStorage.dataFolder).exists()) {
            for (File animation : new File(YamlStorage.dataFolder).listFiles()) {
                BlockAnimation blockAnimation = new BlockAnimation();

                blockAnimation.setAnimationDir(animation);
                blockAnimation.setFrameCount(animation.listFiles().length);

                File settings = new File(animation.getPath() + "/settings.yml");
                FileConfiguration settingsYml = YamlConfiguration.loadConfiguration(settings);
                blockAnimation.setReverse(settingsYml.getBoolean("reverse"));
                blockAnimation.setWorld(Bukkit.getWorld(UUID.fromString(settingsYml.getString("world"))));

                File frame = new File(animation.getPath() + "/0.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(frame);
                Map<BlockVector, BlockData> blockVectors = new HashMap<>();

                if (!config.contains("blocks")) continue;
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
                blockAnimation.addFrame(0, animationFrame);

                JustAnimations.INSTANCE.getAnimations().put(animation.getName(), blockAnimation);
                if (blockAnimation.getFrames().size() > 0) blockAnimation.play();
            }
        }
    }
}