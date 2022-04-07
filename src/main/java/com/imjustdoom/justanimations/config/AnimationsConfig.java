package com.imjustdoom.justanimations.config;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.impl.BlockAnimation;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.storage.impl.MultipleFileFrameStorage;
import com.imjustdoom.justanimations.storage.impl.SingleFileFrameStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class AnimationsConfig {

    public static FileConfiguration config;

    public static String PREFIX;

    public static class Settings {

    }

    public static class Messages {
           public static String RELOAD, RELOAD_ERROR, RELOAD_SUCCESS;
           public static String CREATE, CREATE_ERROR, CREATE_SUCCESS, CREATE_EXISTS;
           public static String DELETE, DELETE_ERROR, DELETE_SUCCESS, DELETE_NOT_EXISTS;
           public static String ANIMATION_NOT_EXISTS;
           public static String HELP;
           public static String TOGGLE_REVERSE;
           public static String WORLD_CHANGE, WORLD_NOT_EXISTS, WORLD_IN_USE;
           public static String SETTINGS;
           public static String GETFRAME;
           public static String ADDFRAME, ADDFRAME_ERROR;
           public static String PLAY_ANIMATION, PLAY_ANIMATION_ERROR, PLAY_ANIMATION_RUNNING;
           public static String STOP_ANIMATION, STOP_ANIMATION_ERROR, STOP_ANIMATION_NOT_RUNNING;
           public static String REMOVE_FRAME, REMOVE_FRAME_ERROR;
           public static String GO_TO_FRAME, GO_TO_FRAME_ERROR, GO_TO_FRAME_NOT_EXISTS;
    }

    public static void load() {

        JustAnimations.INSTANCE.reloadConfig();
        config = JustAnimations.INSTANCE.getConfig();

        PREFIX = config.getString("prefix");
        Messages.RELOAD = config.getString("messages.reload");
        Messages.RELOAD_ERROR = config.getString("messages.reload-error");
        Messages.RELOAD_SUCCESS = config.getString("messages.reload-success");

        Messages.CREATE = config.getString("messages.create");
        Messages.CREATE_ERROR = config.getString("messages.create-error");
        Messages.CREATE_SUCCESS = config.getString("messages.create-success");
        Messages.CREATE_EXISTS = config.getString("messages.create-exists");

        Messages.DELETE = config.getString("messages.delete");
        Messages.DELETE_ERROR = config.getString("messages.delete-error");
        Messages.DELETE_SUCCESS = config.getString("messages.delete-success");
        Messages.DELETE_NOT_EXISTS = config.getString("messages.delete-not-exists");

        Messages.ANIMATION_NOT_EXISTS = config.getString("messages.animation-not-exists");

        Messages.HELP = config.getString("messages.help");

        Messages.TOGGLE_REVERSE = config.getString("messages.toggle-reverse");

        Messages.WORLD_CHANGE = config.getString("messages.world-change");
        Messages.WORLD_NOT_EXISTS = config.getString("messages.world-not-exists");
        Messages.WORLD_IN_USE = config.getString("messages.world-in-use");

        Messages.SETTINGS = config.getString("messages.settings");

        Messages.GETFRAME = config.getString("messages.getframe");

        Messages.ADDFRAME = config.getString("messages.addframe");
        Messages.ADDFRAME_ERROR = config.getString("messages.addframe-error");

        Messages.PLAY_ANIMATION = config.getString("messages.play-animation");
        Messages.PLAY_ANIMATION_ERROR = config.getString("messages.play-animation-error");
        Messages.PLAY_ANIMATION_RUNNING = config.getString("messages.play-animation-running");

        Messages.STOP_ANIMATION = config.getString("messages.stop-animation");
        Messages.STOP_ANIMATION_ERROR = config.getString("messages.stop-animation-error");
        Messages.STOP_ANIMATION_NOT_RUNNING = config.getString("messages.stop-animation-not-running");

        Messages.REMOVE_FRAME = config.getString("messages.remove-frame");
        Messages.REMOVE_FRAME_ERROR = config.getString("messages.remove-frame-error");

        Messages.GO_TO_FRAME = config.getString("messages.go-to-frame");
        Messages.GO_TO_FRAME_ERROR = config.getString("messages.go-to-frame-error");
        Messages.GO_TO_FRAME_NOT_EXISTS = config.getString("messages.go-to-frame-not-exists");

        for (IAnimation animation : JustAnimations.INSTANCE.getAnimations().values()) {
            if(animation.getRunnable() == null) continue;
            animation.stop();
        }

        JustAnimations.INSTANCE.getAnimations().clear();
        if (new File(JustAnimations.INSTANCE.getAnimationDataFolder()).exists()) {
            for (File animation : new File(JustAnimations.INSTANCE.getAnimationDataFolder()).listFiles()) {
                BlockAnimation blockAnimation = new BlockAnimation();

                blockAnimation.setDataStore(new File(animation.getPath() + "/frames.yml").exists()
                        ? new SingleFileFrameStorage(animation.getName())
                        : new MultipleFileFrameStorage(animation.getName()));
                blockAnimation.setFrameCount(blockAnimation.getDataStore().getFrameCount());

                File settings = new File(animation.getPath() + "/settings.yml");
                FileConfiguration settingsYml = YamlConfiguration.loadConfiguration(settings);
                blockAnimation.setName(animation.getName().replace(".yml", ""));
                blockAnimation.setReverse(settingsYml.getBoolean("reverse"));
                blockAnimation.setWorld(Bukkit.getWorld(UUID.fromString(settingsYml.getString("world"))));

                AnimationFrame animationFrame = AnimationUtil.getFrame(blockAnimation, "0");
                if(animationFrame != null) blockAnimation.addFrame(0, animationFrame);

                JustAnimations.INSTANCE.getAnimations().put(animation.getName(), blockAnimation);

                if (blockAnimation.getFrames().size() > 0) blockAnimation.play();
            }
        }
    }
}