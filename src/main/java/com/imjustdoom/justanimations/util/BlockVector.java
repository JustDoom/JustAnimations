package com.imjustdoom.justanimations.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockVector {

    private int x;
    private int y;
    private int z;

    public BlockVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
