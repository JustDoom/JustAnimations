package com.imjustdoom.justanimations.storage.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.storage.DataStore;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class MultipleFileFrameStorage implements DataStore {

    public final String dataFolder;
    private final String name;

    public MultipleFileFrameStorage(String animation) {
        this.dataFolder = JustAnimations.INSTANCE.getDataFolder() + "/data/" + animation + "/";
        this.name = animation;
    }

    public void createAnimationData(String animation, World world, String frameLoad) {
        File data = new File(JustAnimations.INSTANCE.getAnimationDataFolder());
        if (!data.exists()) data.mkdir();

        data = new File(dataFolder);
        if (!data.exists()) data.mkdir();

        try {
            data = new File(dataFolder + "/settings.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(data);
            config.set("reverse", false);
            config.set("world", world.getUID().toString());
            config.set("frame-load", frameLoad);
            config.set("random-frame", false);
            config.save(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFrame(String frame) {
        File file = new File(dataFolder, frame + ".yml");

        if(!file.exists()) return null;

        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveFrame(String animation, ConfigurationSection section, int delay) {
        try {
            File file = new File(dataFolder, (JustAnimations.INSTANCE.getAnimations().get(animation).getDataStore().getFrameCount()) + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("blocks", section);
            config.set("delay", delay);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFrameCount() {
        File file = new File(dataFolder);
        int count = file.listFiles().length - 1;
        return new File(file, "frames.yml").exists() ? count - 1 : count;
    }

    public File getSettings() {
        return new File(dataFolder, "/settings.yml");
    }

    public void saveSetting(String path, Object value) {
        try {
            File file = getSettings();
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set(path, value);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFrameSetting(String frame, String path, Object value) {
        File file = new File(dataFolder, frame + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getSetting(String path) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(getSettings());
        return config.get(path);
    }

    public void deleteAnimation(String animation) {
        File file = new File(dataFolder);
        if (file.exists()) {
            for (File frame : file.listFiles()) {
                frame.delete();
            }
            file.delete();
        }
    }

    public DataStore convertFrames(IAnimation animation) {

        DataStore store = new SingleFileFrameStorage(name);
        File framesFile = new File(dataFolder, "frames.yml");

        animation.getFrames().clear();

        try {
            YamlConfiguration frames = YamlConfiguration.loadConfiguration(framesFile);
            frames.createSection("frames");
            frames.save(framesFile);

            int count = getFrameCount() - 1;
            for(int i = 0; i <= count; i++) {
                FileConfiguration frame = getFrame(String.valueOf(i));
                store.saveFrame(name, frame.getConfigurationSection("blocks"), frame.getInt("delay"));
                new File(dataFolder, i + ".yml").delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return store;
    }
}