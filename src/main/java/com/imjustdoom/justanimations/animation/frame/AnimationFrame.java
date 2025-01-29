package com.imjustdoom.justanimations.animation.frame;

import com.imjustdoom.justanimations.api.util.BlockVector;
import org.bukkit.block.data.BlockData;

import java.util.Map;

public class AnimationFrame {

    private Map<BlockVector, BlockData> blockVectors;
    private int delay;

    public AnimationFrame(Map<BlockVector, BlockData> blockVectors) {
        this.blockVectors = blockVectors;
    }

    public AnimationFrame(Map<BlockVector, BlockData> blockVectors, int delay) {
        this.blockVectors = blockVectors;
        this.delay = delay;
    }

    public Map<BlockVector, BlockData> getBlockVectors() {
        return this.blockVectors;
    }

    public int getDelay() {
        return this.delay;
    }
}