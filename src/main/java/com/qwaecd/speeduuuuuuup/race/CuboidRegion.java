package com.qwaecd.speeduuuuuuup.race;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public class CuboidRegion implements Region{
    private BlockPos start;
    private BlockPos end;
    private AABB aabb;
    private Region.PointType pointType;

    private CuboidRegion(BlockPos start, BlockPos end) {
        this.start = new BlockPos(
            Math.min(start.getX(), end.getX()),
            Math.min(start.getY(), end.getY()),
            Math.min(start.getZ(), end.getZ())
        );
        this.end = new BlockPos(
            Math.max(start.getX(), end.getX()) + 1,
            Math.max(start.getY(), end.getY()) + 1,
            Math.max(start.getZ(), end.getZ()) + 1
        );
        this.aabb = new AABB(this.start, this.end);
        this.pointType = Region.PointType.CHECKPOINT;
    }

    public CuboidRegion(BlockPos start, BlockPos end, Region.PointType pointType) {
        this(start, end);
        this.pointType = pointType;
    }

    @Override
    public RegionType getType() {
        return RegionType.CUBOID;
    }

    @Override
    public PointType getPointType() {
        return this.pointType;
    }

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }

    @Override
    public AABB toAABB() {
        return aabb;
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
        BlockPos end = new BlockPos(tag.getInt("endX") - 1, tag.getInt("endY") - 1, tag.getInt("endZ") - 1);
        return new CuboidRegion(start, end);
    }
    @Override
    public BlockPos getStartPos() {
        return start;
    }

    @Override
    public BlockPos getEndPos() {
        return end;
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
