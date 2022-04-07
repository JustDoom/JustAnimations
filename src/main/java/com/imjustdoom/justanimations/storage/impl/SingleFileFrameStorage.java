package com.imjustdoom.justanimations.storage.impl;

import com.imjustdoom.justanimations.JustAnimations;
import com.imjustdoom.justanimations.api.util.AnimationUtil;
import com.imjustdoom.justanimations.storage.DataStore;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class SingleFileFrameStorage implements DataStore {

    public final String dataFolder;
    private final String name;

    public SingleFileFrameStorage(String animation) {
        this.dataFolder = JustAnimations.INSTANCE.getDataFolder() + "/data/" + animation + "/";
        this.name = animation;
    }

    public void createAnimationData(String animation, World world) {
        File data = new File(JustAnimations.INSTANCE.getAnimationDataFolder());
        if (!data.exists()) data.mkdir();

        data = new File(dataFolder);
        if (!data.exists()) data.mkdir();

        try {
            data = new File(dataFolder + "/settings.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(data);
            config.set("reverse", false);
            config.set("world", world.getUID().toString());
            config.set("store-type", "single-file");
            config.save(data);

            File framesFile = new File(dataFolder, "frames.yml");
            YamlConfiguration frames = YamlConfiguration.loadConfiguration(framesFile);
            frames.createSection("frames");
            frames.save(framesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getFrame(String frame) {
        File file = new File(dataFolder, "frames.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        return config.getConfigurationSection("frames." + frame);
    }

    public void saveFrame(String animation, ConfigurationSection section, int delay) {
        try {
            File file = new File(dataFolder, "frames.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection cfgSec = config.getConfigurationSection("frames");
            int frame = cfgSec == null ? 0 : cfgSec.getKeys(false).size();
            cfgSec.createSection(String.valueOf(frame));
            config.set("frames." + frame + ".delay", delay);
            config.set("frames." + frame + ".blocks", section);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFrameCount() {
        File file = new File(dataFolder, "frames.yml");
        ConfigurationSection cfgSec = YamlConfiguration.loadConfiguration(file).getConfigurationSection("frames");
        System.out.println(cfgSec.getKeys(false).size());
        return cfgSec == null ? 0 : cfgSec.getKeys(false).size();
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

    public DataStore convertFrames() {
        File configFile = new File(dataFolder, "frames.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        JustAnimations.INSTANCE.getAnimations().get(name).getFrames().clear();

        config.getConfigurationSection("frames").getKeys(false).forEach(key -> {
            try {
                File frameFile = new File(dataFolder, key + ".yml");
                YamlConfiguration frame = YamlConfiguration.loadConfiguration(frameFile);
                frame.createSection("blocks");
                frame.set("blocks", config.getConfigurationSection("frames." + key + ".blocks"));
                frame.set("delay", config.getInt("frames." + key + ".delay"));
                frame.save(frameFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        configFile.delete();

        JustAnimations.INSTANCE.getAnimations().get(name).getFrames().put(
                0, AnimationUtil.getFrame(JustAnimations.INSTANCE.getAnimations().get(name), String.valueOf(0)));

        return new MultipleFileFrameStorage(this.name);
    }
}