package com.imjustdoom.justanimations.storage;

import com.imjustdoom.justanimations.JustAnimations;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlStorage {

    public static final String dataFolder = JustAnimations.INSTANCE.getDataFolder() + "/data/";

    public static void createAnimationData(String animation) {
        File data = new File(dataFolder);
        if (!data.exists()) data.mkdir();

        data = new File(dataFolder + animation);
        if (!data.exists()) data.mkdir();
    }

    public static FileConfiguration getFrame(String frame) {
        File file = new File(dataFolder, frame + ".yml");

        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFrame(String frame, FileConfiguration config, int delay, World world) {
        try {
            File file = new File(dataFolder + frame, (JustAnimations.INSTANCE.getAnimations().get(frame).getFrames().size() - 1) + ".yml");
            config.set("world", world.getUID().toString());
            config.set("delay", delay);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}