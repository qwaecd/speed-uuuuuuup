package com.qwaecd.speeduuuuuuup.data;

import net.minecraft.server.level.ServerLevel;

public class ModData {
    public static RaceTrackData getRaceTrackData(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(
                RaceTrackData::load,
                RaceTrackData::new,
                "race_tracks"
        );
    }
}
