package com.imjustdoom.justanimations.storage;

import com.imjustdoom.justanimations.JustAnimations;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlStorage {

    public static final String dataFolder = JustAnimations.INSTANCE.getDataFolder() + "/data/";

    public static void createAnimationData(String animation, World world) {
        File data = new File(dataFolder);
        if (!data.exists()) data.mkdir();

        data = new File(dataFolder + animation);
        if (!data.exists()) data.mkdir();

        try {
            data = new File(dataFolder + animation + "/settings.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(data);
            config.set("reverse", false);
            config.set("world", world.getUID().toString());
            config.save(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getFrame(String frame) {
        System.out.println(dataFolder + frame + ".yml");
        File file = new File(dataFolder, frame + ".yml");

        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getSettings(String animation) {
        File file = new File(dataFolder + animation, "/settings.yml");

        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFrame(String frame, FileConfiguration config, int delay) {
        try {
            File file = new File(dataFolder + frame, (JustAnimations.INSTANCE.getAnimations().get(frame).getFrames().size() - 1) + ".yml");
            config.set("delay", delay);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toggleReverse(String animation, boolean reverse) {
        try {
            FileConfiguration config = getSettings(animation);
            File file = new File(dataFolder + animation + "/settings.yml");
            config.set("reverse", reverse);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setWorld(String animation, World world) {
        try {
            FileConfiguration config = getSettings(animation);
            File file = new File(dataFolder + animation + "/settings.yml");
            config.set("world", world.getUID().toString());
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAnimation(String animation) {
        File file = new File(dataFolder + animation);
        if (file.exists()) {
            for (File frame : file.listFiles()) {
                frame.delete();
            }
            file.delete();
        }
    }
}