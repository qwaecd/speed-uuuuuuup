package com.qwaecd.speeduuuuuuup.entity;

import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrackManager;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerEntityManager {
    private static final Map<String, List<RegionMarkerEntity>> markerEntities = new HashMap<>();

    public static void addMarkerEntity(String raceTrackId, RegionMarkerEntity entity, ServerLevel level) {
        if (level == null || !level.isLoaded(entity.blockPosition())) {
            return;
        }
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(raceTrackId, level);
        if (raceTrack == null){
            return;
        }
        markerEntities.computeIfAbsent(raceTrackId, k -> new ArrayList<>()).add(entity);
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
            if (entity.level() instanceof ServerLevel serverLevel && serverLevel.isLoaded(entity.blockPosition())) {
                serverLevel.addFreshEntity(entity);
            }
        }
    }

    public static void removeFromLevel(String raceTrackId) {
        List<RegionMarkerEntity> entities = markerEntities.get(raceTrackId);
        if (entities == null || entities.isEmpty()) {
            return;
        }
        for (RegionMarkerEntity entity : entities) {
            if (entity.level() instanceof ServerLevel serverLevel && serverLevel.isLoaded(entity.blockPosition())) {
                entity.discard();
            }
        }
        removeAllMarkerEntities(raceTrackId);
    }
}
