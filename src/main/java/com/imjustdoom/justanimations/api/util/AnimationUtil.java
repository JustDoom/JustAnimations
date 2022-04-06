package com.imjustdoom.justanimations.api.util;

import com.imjustdoom.justanimations.animation.IAnimation;
import com.imjustdoom.justanimations.util.BlockVector;
import org.bukkit.Location;

public class AnimationUtil {

    public static Location blockVectorToLocation(IAnimation animation, BlockVector vector) {
        return new Location(animation.getWorld(), vector.getX(), vector.getY(), vector.getZ());
    }
}