package com.qwaecd.speeduuuuuuup.race;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public class CuboidRegion implements Region{
    private BlockPos start;
    private BlockPos end;
    private AABB aabb;

    public CuboidRegion(BlockPos start, BlockPos end) {
        this.start = start;
        this.end = end;
        this.aabb = new AABB(start, end);
    }

    @Override
    public RegionType getType() {
        return RegionType.CUBOID;
    }

    @Override
    public AABB toAABB() {
        return aabb;
    }

    public BlockPos getEnd() {
        return end;
    }

    public BlockPos getStart() {
        return start;
    }

    @Override
    public CompoundTag toCompoundTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("startX", start.getX());
        tag.putInt("startY", start.getY());
        tag.putInt("startZ", start.getZ());
        tag.putInt("endX", end.getX());
        tag.putInt("endY", end.getY());
        tag.putInt("endZ", end.getZ());
        tag.putString("type", getType().name());
        return tag;
    }

    public static Region fromCompoundTag(CompoundTag tag) {
        BlockPos start = new BlockPos(tag.getInt("startX"), tag.getInt("startY"), tag.getInt("startZ"));
        BlockPos end = new BlockPos(tag.getInt("endX"), tag.getInt("endY"), tag.getInt("endZ"));
        return new CuboidRegion(start, end);
    }

    @Override
    public String toString() {
        return "{start: " +
                start.getX() + ", " +
                start.getY() + ", " +
                start.getZ() + " | " +
                "end: " +
                end.getX() + ", " +
                end.getY() + ", " +
                end.getZ()+"}";
    }
}
