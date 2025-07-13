package com.qwaecd.speeduuuuuuup.data;

import com.qwaecd.speeduuuuuuup.race.RaceTrackManager;
import net.minecraft.server.level.ServerLevel;

public class ModData {
    public static RaceTrackData getRaceTrackData(ServerLevel serverLevel) {
        RaceTrackData data = serverLevel.getDataStorage().computeIfAbsent(
                RaceTrackData::load,
                RaceTrackData::new,
                "race_tracks"
        );
        if(RaceTrackManager.modifyCallback  == null){
            RaceTrackManager.modifyCallback = data::setDirty;
        }
        return data;
    }
}
