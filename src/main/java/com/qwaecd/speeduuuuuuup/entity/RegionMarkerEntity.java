package com.qwaecd.speeduuuuuuup.entity;

import com.qwaecd.speeduuuuuuup.race.*;
import com.qwaecd.speeduuuuuuup.race.structure.CuboidRegion;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.Region;
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
    protected RaceTrack raceTrack;
    protected Region.RegionType regionType;
    protected Region region;
    protected AABB synchedAABB;
    private static final EntityDataAccessor<BlockPos> START_POS = SynchedEntityData.defineId(RegionMarkerEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockPos> END_POS = SynchedEntityData.defineId(RegionMarkerEntity.class, EntityDataSerializers.BLOCK_POS);

    public RegionMarkerEntity(EntityType<?> entityType, Level level){
        super(entityType, level);
        this.noPhysics = true;
        this.setInvulnerable(true);
        this.setSyncedAABB(new BlockPos(0, 0, 0), new BlockPos(1, 1, 1));
    }

    public RegionMarkerEntity(EntityType<?> entityType, Level level, Region region, RaceTrack raceTrack) {
        super(entityType, level);
        this.noPhysics = true;
        this.region = region;
        this.regionType = region.getType();
        this.raceTrack = raceTrack;
        this.setInvulnerable(true);
        this.setSyncedAABB(region.getStartPos(), region.getEndPos());
    }

    public void move() {
        AABB aabb = this.region.toAABB();
        double maxX = aabb.maxX;
        double minX = aabb.minX;
        double maxY = aabb.maxY;
        double minY = aabb.minY;
        double maxZ = aabb.maxZ;
        double minZ = aabb.minZ;
        this.setPos((maxX + minX) / 2, (maxY + minY) / 2 - 0.5D, (maxZ + minZ) / 2);
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(START_POS, BlockPos.ZERO);
        this.entityData.define(END_POS, BlockPos.ZERO);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (level.isClientSide || !this.raceTrack.isRacing) {
            return;
        }
        AABB bb = region.toAABB();
        level.getEntitiesOfClass(Player.class, bb).forEach(this::onPlayerInBox);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        CompoundTag region = compoundTag.getCompound("region");
        String pointTypeName = compoundTag.getString("point_type");
        Region.PointType pointType = Region.PointType.valueOf(pointTypeName);
        if(region.isEmpty()) {
            this.region = new CuboidRegion(new BlockPos(0, 0, 0), new BlockPos(1, 1, 1), pointType);
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
        compoundTag.putString("point_type", this.region.getPointType().name());
    }

    public void onPlayerInBox(Player player) {
        if (player == null || player.isSpectator()/* || player.isCreative()*/) {
            return;
        }
        if (this.raceTrack == null) {
            return;
        }
        if (!RaceManager.inRace(this.raceTrack, player.getUUID())){
            return;
        }
        RacePlayer racePlayer = RaceManager.getRacePlayer(this.raceTrack, player.getUUID());
        if (racePlayer == null) {
            return;
        }
        RaceHandler.racing(this.raceTrack, racePlayer, this.region);
//        System.out.println("Player " + player.getName().getString() + " entered region.");
    }

    public void setSyncedAABB(BlockPos start, BlockPos end) {
        this.entityData.set(START_POS, start);
        this.entityData.set(END_POS, end);
    }

    public AABB getSynchedAABB() {
        this.synchedAABB = new AABB(this.entityData.get(START_POS), this.entityData.get(END_POS));
        return this.synchedAABB;
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
