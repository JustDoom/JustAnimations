package com.imjustdoom.justanimations.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

@Getter
@Setter
public class BlockVector {

    private int x;
    private int y;
    private int z;
    private World world;
    private BlockData blockData;

    public BlockVector(int x, int y, int z, World world, BlockData blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.blockData = blockData;
    }
}
