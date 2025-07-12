package com.qwaecd.speeduuuuuuup.race;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public interface Region {
    RegionType getType();
    AABB toAABB(BlockPos start, BlockPos end);
    CompoundTag toCompoundTag();

    enum RegionType {
        CUBOID
    }
}
