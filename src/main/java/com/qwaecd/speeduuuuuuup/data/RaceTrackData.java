package com.qwaecd.speeduuuuuuup.data;

import com.qwaecd.speeduuuuuuup.race.CuboidRegion;
import com.qwaecd.speeduuuuuuup.race.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.Region;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RaceTrackData extends SavedData {
    private final Map<String, RaceTrack> raceTracks = new HashMap<>();

    public Map<String, RaceTrack> getRaceTracks() {
        return raceTracks;
    }

    public void putRaceTrack(String id, RaceTrack raceTrack){
        raceTracks.put(id,raceTrack);
        setDirty();
    }

    public RaceTrack getRaceTrack(String id) {
        return raceTracks.get(id);
    }

    public boolean removeRaceTrack(String id) {
        if(raceTracks.containsKey(id)) {
            raceTracks.remove(id);
            setDirty();
            return true;
        }
        return false;
    }

    public boolean containsRaceTrack(String id) {
        return raceTracks.containsKey(id);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        CompoundTag rootTag = new CompoundTag();
        for(Map.Entry<String, RaceTrack> entry : raceTracks.entrySet()) {
            String id = entry.getKey();
            if(id == null || id.isEmpty()) {
                continue;
            }
            CompoundTag raceTag = new CompoundTag();
            if(entry.getValue().getDescription() != null)
                raceTag.putString("description", entry.getValue().getDescription());
            if(entry.getValue().getStartRegion() != null)
                raceTag.put("start_region", entry.getValue().getStartRegion().toCompoundTag());
            if(entry.getValue().getEndRegion() != null)
                raceTag.put("end_region", entry.getValue().getEndRegion().toCompoundTag());

            List<Region> checkpoints = entry.getValue().getCheckpoints();
            CompoundTag checkpointTag = new CompoundTag();
            for(Region checkpoint : checkpoints) {
                checkpointTag.put("checkpoint_" + checkpoints.indexOf(checkpoint), checkpoint.toCompoundTag());
            }
            raceTag.put("checkpoints", checkpointTag);
            raceTag.putInt("dim", getDimensionNum(entry.getValue().getDimension()));
            rootTag.put(id,raceTag);
        }
        compoundTag.put("race_tracks", rootTag);
        return compoundTag;
    }

    public static RaceTrackData load(CompoundTag compoundTag){
        RaceTrackData raceTrackData = new RaceTrackData();
        CompoundTag rootTag = compoundTag.getCompound("race_tracks");
        for(String raceId : rootTag.getAllKeys()){
            if(raceId == null) continue;
            CompoundTag raceTag = rootTag.getCompound(raceId);
            RaceTrack raceTrack = new RaceTrack(raceId);

            raceTrack.setDescription(raceTag.getString("description"));
            if(!raceTag.getCompound("start_region").isEmpty()){
                Region startRegion = getRegionFromTag(raceTag.getCompound("start_region"));
                raceTrack.setStartRegion(startRegion);
            }
            if(!raceTag.getCompound("end_region").isEmpty()){
                Region endRegion = getRegionFromTag(raceTag.getCompound("end_region"));
                raceTrack.setEndRegion(endRegion);
            }
            raceTrack.setDimension(getDimensionFromNum(raceTag.getInt("dim")));

            CompoundTag checkpointsTag = raceTag.getCompound("checkpoints");
            TreeMap<Integer, Region> sortedCheckpoints = new TreeMap<>();
            for(String checkpointKey : checkpointsTag.getAllKeys()) {
                if (checkpointKey.startsWith("checkpoint_")) {
                    try {
                        int index = Integer.parseInt(checkpointKey.substring("checkpoint_".length()));
                        CompoundTag checkpointTag = checkpointsTag.getCompound(checkpointKey);
                        Region checkpointRegion = getRegionFromTag(checkpointTag);
                        sortedCheckpoints.put(index, checkpointRegion);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid checkpoint key: " + checkpointKey, e);
                    }
                }
            }
            for (Region checkpoint : sortedCheckpoints.values()) {
                raceTrack.addCheckpoint(checkpoint);
            }
            raceTrackData.raceTracks.put(raceId, raceTrack);
        }
        return raceTrackData;
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

    private static int getDimensionNum(ResourceKey<Level> level) {
        if (level == Level.OVERWORLD) {
            return 0; // Overworld
        } else if (level == Level.NETHER) {
            return -1; // Nether
        } else if (level == Level.END) {
            return 1; // End
        }
        return 0;
    }

    private static ResourceKey<Level> getDimensionFromNum(int num) {
        switch (num) {
            case -1:
                return Level.NETHER;
            case 1:
                return Level.END;
            case 0:
            default:
                return Level.OVERWORLD;
        }
    }
}
