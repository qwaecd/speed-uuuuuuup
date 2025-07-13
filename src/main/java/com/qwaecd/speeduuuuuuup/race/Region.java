package com.qwaecd.speeduuuuuuup.race;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public interface Region {
    RegionType getType();
    AABB toAABB();
    CompoundTag toCompoundTag();

    enum RegionType {
        CUBOID
    }
}
