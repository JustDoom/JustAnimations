package com.imjustdoom.justanimations.config;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.BlockAnimation;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.storage.YamlStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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

                AnimationFrame animationFrame = AnimationUtil.getFrame(blockAnimation, 0);
                blockAnimation.addFrame(0, animationFrame);

                JustAnimations.INSTANCE.getAnimations().put(animation.getName(), blockAnimation);
                if (blockAnimation.getFrames().size() > 0) blockAnimation.play();
            }
        }
    }
}