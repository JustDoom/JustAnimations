package com.imjustdoom.justanimations.api.util;

import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.animation.frame.AnimationFrame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class AnimationUtil {

    public static Location blockVectorToLocation(IAnimation animation, BlockVector vector) {
        return new Location(animation.getWorld(), vector.getX(), vector.getY(), vector.getZ());
    }

    public static AnimationFrame getFrame(IAnimation animation, String frame) {
        ConfigurationSection newFrame = animation.getDataStore().getFrame(frame);
        if(newFrame == null) return null;
        Map<BlockVector, BlockData> blockVectors = new HashMap<>();

        if (newFrame.contains("blocks")) {
            for (String key : newFrame.getConfigurationSection("blocks").getKeys(false)) {
                for (String key1 : newFrame.getConfigurationSection("blocks." + key).getKeys(false)) {
                    for (String key2 : newFrame.getConfigurationSection("blocks." + key + "." + key1).getKeys(false)) {
                        BlockData blockData;
                        try {
                            blockData = Bukkit.getServer().createBlockData(newFrame.getString("blocks." + key + "." + key1 + "." + key2));
                        } catch (Exception e) {
                            blockData = Material.AIR.createBlockData();
                        }
                        blockVectors.put(new BlockVector(Integer.valueOf(key), Integer.valueOf(key1), Integer.valueOf(key2)), blockData);
                    }
                }
            }
        }

        return new AnimationFrame(blockVectors, newFrame.getInt("delay"));
    }
}