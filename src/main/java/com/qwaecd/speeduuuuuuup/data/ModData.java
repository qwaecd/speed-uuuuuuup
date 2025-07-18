package com.qwaecd.speeduuuuuuup.data;

import com.qwaecd.speeduuuuuuup.race.structure.RaceTrackManager;
import net.minecraft.server.level.ServerLevel;

import static com.qwaecd.speeduuuuuuup.SpeedUuuuuuup.MODID;

public class ModData {
    public static RaceTrackData getRaceTrackData(ServerLevel serverLevel) {
        RaceTrackData data = serverLevel.getDataStorage().computeIfAbsent(
                RaceTrackData::load,
                RaceTrackData::new,
                MODID + "_race_tracks"
        );
        if(RaceTrackManager.modifyCallback  == null){
            RaceTrackManager.modifyCallback = data::setDirty;
        }
        return data;
    }

    public static RaceResultData getRaceResultData(ServerLevel serverLevel) {
        RaceResultData data = serverLevel.getDataStorage().computeIfAbsent(
                RaceResultData::load,
                RaceResultData::new,
                MODID + "_race_result_data"
        );
        return data;
    }
}
