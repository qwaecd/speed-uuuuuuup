package com.qwaecd.speeduuuuuuup.race;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RaceTrackManager {
    private static final Map<String, RaceTrack> RACES = new HashMap<>();
    private static String editingRaceTrack = null;

    public static Boolean createRaceTrack(String name, RaceTrack race) {
        if(RACES.containsKey(name)) return false;
        RACES.put(name, race);
        return true;
    }

    public static @Nullable RaceTrack getRaceTrack(String name) {
        return RACES.get(name);
    }

    public static boolean removeRaceTrack(String name) {
        if(RACES.containsKey(name)){
            RACES.remove(name);
            return true;
        }
        return false;
    }

    public static Map<String, RaceTrack> getAllRaceTracks() {
        return RACES;
    }

    public static String getEditingRaceTrack() {
        return editingRaceTrack;
    }

    public static void setEditingRaceTrack(String editingRaceTrack) {
        RaceTrackManager.editingRaceTrack = editingRaceTrack;
    }
}
