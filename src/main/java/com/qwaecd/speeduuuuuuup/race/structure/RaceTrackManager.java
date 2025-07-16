package com.qwaecd.speeduuuuuuup.race.structure;


import com.qwaecd.speeduuuuuuup.data.ModData;
import com.qwaecd.speeduuuuuuup.data.RaceTrackData;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public class RaceTrackManager {
    private static String editingRaceTrack = null;
    public static Runnable modifyCallback;

    public static boolean createRaceTrack(String name, RaceTrack race, ServerLevel level) {
        RaceTrackData data = ModData.getRaceTrackData(level);

        if(data.containsRaceTrack(name)) return false;
        data.putRaceTrack(name, race);
        return true;
    }

    public static @Nullable RaceTrack getRaceTrack(String name, ServerLevel level) {
        return ModData.getRaceTrackData(level).getRaceTrack(name);
    }

    public static boolean removeRaceTrack(String name, ServerLevel level) {
        RaceTrackData data = ModData.getRaceTrackData(level);
        if(data.containsRaceTrack(name)){
            data.removeRaceTrack(name);
            data.setDirty();
            return true;
        }
        return false;
    }

    public static String getEditingRaceTrack() {
        return editingRaceTrack;
    }

    public static void setEditingRaceTrack(String editingRaceTrack) {
        RaceTrackManager.editingRaceTrack = editingRaceTrack;
    }
}
