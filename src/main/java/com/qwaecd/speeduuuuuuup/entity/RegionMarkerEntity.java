package com.qwaecd.speeduuuuuuup.entity;

import com.qwaecd.speeduuuuuuup.race.Region;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class RegionMarkerEntity extends Entity {
    protected String trackId;
    protected int regionIndex;
    protected Region.RegionType type;

    public RegionMarkerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
        this.setInvulnerable(true);
    }

    @Override
    protected void defineSynchedData() {
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
        AABB bb = getBoundingBox();
        level.getEntitiesOfClass(Player.class, bb).forEach(this::onPlayerInBox);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public void onPlayerInBox(Player player) {
        // Override this method to handle player entering the region
        System.out.println("Player " + player.getName().getString() + " entered region: " + trackId + " at index: " + regionIndex);
    }
}
