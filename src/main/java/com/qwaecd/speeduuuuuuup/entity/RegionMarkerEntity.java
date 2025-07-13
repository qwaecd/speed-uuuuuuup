package com.qwaecd.speeduuuuuuup.entity;

import com.qwaecd.speeduuuuuuup.race.CuboidRegion;
import com.qwaecd.speeduuuuuuup.race.Region;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class RegionMarkerEntity extends Entity {
    protected String trackId;
    protected int regionIndex;
    protected Region.RegionType regionType;
    protected Region region;
    protected static final EntityDataAccessor<String> TRACK_ID = SynchedEntityData.defineId(RegionMarkerEntity.class, EntityDataSerializers.STRING);

    public RegionMarkerEntity(EntityType<?> entityType, Level level, Region region) {
        super(entityType, level);
        this.noPhysics = true;
        this.region = region;
        this.regionType = region.getType();
        this.setInvulnerable(true);
        this.refreshDimensions();
    }

    public static RegionMarkerEntity create(EntityType<RegionMarkerEntity> entityType, Level level) {
        Region defaultRegion = new CuboidRegion(new BlockPos(0, 0, 0), new BlockPos(1, 1, 1));
        return new RegionMarkerEntity(entityType, level, defaultRegion);
    }

    public void move() {
        AABB aabb = this.region.toAABB();
        double maxX = aabb.maxX;
        double minX = aabb.minX;
        double maxY = aabb.maxY;
        double minY = aabb.minY;
        double maxZ = aabb.maxZ;
        double minZ = aabb.minZ;
        this.setPos((maxX + minX) / 2, (maxY + minY) / 2, (maxZ + minZ) / 2);
        this.refreshDimensions();
    }
//    @Override
//    public EntityDimensions getDimensions(Pose pose) {
//        AABB aabb = this.region.toAABB();
//        float xSize = (float) Math.abs(aabb.maxX - aabb.minX);
//        float zSize = (float) Math.abs(aabb.maxZ - aabb.minZ);
//        float width = Math.max(xSize, zSize);
//        float height = (float) Math.abs(aabb.maxY - aabb.minY);
//        return EntityDimensions.scalable(width, height);
//    }

    @Override
    public void refreshDimensions() {
        if (this.region != null) {
            this.setBoundingBox(this.region.toAABB());
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TRACK_ID, "");
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
//        this.refreshDimensions();
        AABB bb = region.toAABB();
        level.getEntitiesOfClass(Player.class, bb).forEach(this::onPlayerInBox);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        CompoundTag region = compoundTag.getCompound("region");
        if(region.isEmpty()) {
            this.region = new CuboidRegion(new BlockPos(0, 0, 0), new BlockPos(1, 1, 1));
            return;
        }
        this.region = getRegionFromTag(compoundTag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.region == null) return;
        CompoundTag regionTag = this.region.toCompoundTag();
        compoundTag.put("region", regionTag);
        compoundTag.putString("type", this.regionType.name());
    }

    public void onPlayerInBox(Player player) {
        // Override this method to handle player entering the region
        System.out.println("Player " + player.getName().getString() + " entered region: " + trackId + " at index: " + regionIndex);
    }

    public Region getRegion() {
        return this.region;
    }

    private static Region getRegionFromTag(CompoundTag tag) {
        Region.RegionType type = Region.RegionType.valueOf(tag.getString("type"));
        switch (type) {
            case CUBOID:
                return CuboidRegion.fromCompoundTag(tag);
            default:
                throw new IllegalArgumentException("Unknown region type: " + type);
        }
    }
}
