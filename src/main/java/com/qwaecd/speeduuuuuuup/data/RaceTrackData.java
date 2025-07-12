package com.qwaecd.speeduuuuuuup.data;

import com.qwaecd.speeduuuuuuup.race.CuboidRegion;
import com.qwaecd.speeduuuuuuup.race.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.Region;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceTrackData extends SavedData {
    private final Map<String, RaceTrack> raceTracks = new HashMap<>();


    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        CompoundTag rootTag = new CompoundTag();
        for(Map.Entry<String, RaceTrack> entry : raceTracks.entrySet()) {
            String id = entry.getKey();
            if(id == null || id.isEmpty()) {
                continue;
            }
            CompoundTag raceTag = new CompoundTag();
            raceTag.putString("description", entry.getValue().getDescription());
            raceTag.put("start_region", entry.getValue().getStartRegion().toCompoundTag());
            raceTag.put("end_region", entry.getValue().getEndRegion().toCompoundTag());
            List<Region> checkpoints = entry.getValue().getCheckpoints();
            CompoundTag checkpointTag = new CompoundTag();
            for(Region checkpoint : checkpoints) {
                checkpointTag.put("checkpoint_" + checkpoints.indexOf(checkpoint), checkpoint.toCompoundTag());
            }
            raceTag.put("checkpoints", checkpointTag);
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

            Region startRegion = getRegionFromTag(raceTag.getCompound("start_region"));
            raceTrack.setStartRegion(startRegion);
            Region endRegion = getRegionFromTag(raceTag.getCompound("end_region"));
            raceTrack.setEndRegion(endRegion);

            CompoundTag checkpointsTag = raceTag.getCompound("checkpoints");
            for(String checkpointKey : checkpointsTag.getAllKeys()) {
                CompoundTag checkpointTag = checkpointsTag.getCompound(checkpointKey);
                Region checkpointRegion = getRegionFromTag(checkpointTag);
                raceTrack.addCheckpoint(checkpointRegion);
            }
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
}
