package com.imjustdoom.justanimations.storage;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public interface DataStore {

    String getDataFolder();

    void createAnimationData(String animation, World world);

    FileConfiguration getFrame(String frame);

    void saveSetting(String path, Object value);

    File getSettings();

    Object getSetting(String path);

    void saveFrame(String frame, ConfigurationSection section, int delay);

    void deleteAnimation(String animation);
}
