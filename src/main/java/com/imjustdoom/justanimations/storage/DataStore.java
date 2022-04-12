package com.imjustdoom.justanimations.storage;

import com.imjustdoom.justanimations.animation.IAnimation;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public interface DataStore {

    String getDataFolder();

    void createAnimationData(String animation, World world, String frameLoad);

    ConfigurationSection getFrame(String frame);

    void saveSetting(String path, Object value);

    File getSettings();

    Object getSetting(String path);

    void saveFrame(String animation, ConfigurationSection section, int delay, String frame);

    void deleteAnimation(String animation);

    int getFrameCount();

    DataStore convertFrames(IAnimation animation);

    void setFrameSetting(String frame, String path, Object value);

    void setName(String name);

    void removeFrame(String frame);
}