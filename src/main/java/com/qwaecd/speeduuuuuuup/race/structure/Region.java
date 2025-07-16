package com.qwaecd.speeduuuuuuup.race.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public interface Region {
    RegionType getType();
    PointType getPointType();
    void setPointType(PointType pointType);
    AABB toAABB();

    CompoundTag toCompoundTag();
    BlockPos getStartPos();

    BlockPos getEndPos();


    enum PointType {
        START,
        END,
        CHECKPOINT
    }

    enum RegionType {
        CUBOID
    }
}
