package com.imjustdoom.justanimations;

import com.imjustdoom.justanimations.util.BlockVector;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnimationFrame {

    private List<BlockVector> blockVectors;
    private int delay;

    public AnimationFrame(List<BlockVector> blockVectors) {
        this.blockVectors = blockVectors;
    }

    public AnimationFrame(List<BlockVector> blockVectors, int delay) {
        this.blockVectors = blockVectors;
        this.delay = delay;
    }
}
