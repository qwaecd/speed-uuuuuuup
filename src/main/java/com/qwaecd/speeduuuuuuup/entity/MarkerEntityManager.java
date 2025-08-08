package com.qwaecd.speeduuuuuuup.entity;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrackManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MarkerEntityManager {
    private static final Logger LOGGER = LogUtils.getLogger();;
    private static final Map<String, List<RegionMarkerEntity>> markerEntities = new HashMap<>();

    public static void addMarkerEntity(String raceTrackId, RegionMarkerEntity entity, ServerLevel level) {
        if (level == null) {
            return;
        }
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(raceTrackId, level);
        if (raceTrack == null){
            return;
        }
        markerEntities.computeIfAbsent(raceTrackId, k -> new ArrayList<>()).add(entity);
    }

    public static void removeMarkerEntity(String raceTrackId, RegionMarkerEntity entity) {
        List<RegionMarkerEntity> entities = markerEntities.get(raceTrackId);
        if (entities != null) {
            entities.remove(entity);
        }
    }

    public static void removeAllMarkerEntities(String raceTrackId) {
        markerEntities.remove(raceTrackId);
    }

    public static void addToLevel(String raceTrackId) {
        List<RegionMarkerEntity> entities = markerEntities.get(raceTrackId);
        if (entities == null || entities.isEmpty()) {
            return;
        }
        for (RegionMarkerEntity entity : entities) {
            if (entity.level() instanceof ServerLevel serverLevel) {
                fuckChunkToAddEntity(entity, serverLevel);
            }
        }
    }

    public static boolean removeFromLevel(String raceTrackId) {
        List<RegionMarkerEntity> entities = markerEntities.get(raceTrackId);
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        List<RegionMarkerEntity> removed = new ArrayList<>();
        for (RegionMarkerEntity entity : entities) {
            if (entity.level() instanceof ServerLevel serverLevel) {
                boolean success = fuckChunkToRemoveEntity(entity, serverLevel);
                if (success){
                    removed.add(entity);
                }
            }
        }
        for (RegionMarkerEntity entity : removed) {
            removeMarkerEntity(raceTrackId, entity);
        }
        if (entities.isEmpty()) {
            removeAllMarkerEntities(raceTrackId);
            return true;
        }
        return false;
    }
    
    private static void fuckChunkToAddEntity(RegionMarkerEntity entity, ServerLevel level) {
        fuckChunkToDo(entity, level,()->{
            level.addFreshEntity(entity);
        });
        LOGGER.debug("added entity to level");
    }

    private static boolean fuckChunkToRemoveEntity(RegionMarkerEntity entity, ServerLevel level) {
        LevelEntityGetter<Entity> getter = level.getEntities();
        Entity that = getter.get(entity.getUUID());
        if (that == null) {
            LOGGER.warn("Try to remove entity but it is null, maybe already removed, or try again?");
            return false;
        }
        fuckChunkToDo((RegionMarkerEntity) that, level, () -> that.remove(Entity.RemovalReason.DISCARDED));
        LOGGER.debug("removed entity from level");
        return true;
    }

    private static void fuckChunkToDo(RegionMarkerEntity entity, ServerLevel level, Runnable runnable) {
        BlockPos pos = entity.blockPosition();
        ChunkPos chunkPos = new ChunkPos(pos);
        ServerChunkCache chunkSource = level.getChunkSource();

        level.setChunkForced(chunkPos.x, chunkPos.z, true);
        chunkSource.updateChunkForced(chunkPos, true);

        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future =
                chunkSource.getChunkFuture(chunkPos.x, chunkPos.z, ChunkStatus.FULL, true);

        future.thenAcceptAsync(either -> {
            either.ifLeft(chunk -> {
                if (chunk instanceof LevelChunk) {

                    runnable.run();

                }
                chunkSource.updateChunkForced(chunkPos, false);
            });
        }, level.getServer());
    }
}
