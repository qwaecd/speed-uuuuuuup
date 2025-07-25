package com.qwaecd.speeduuuuuuup.race.structure;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RaceTrack {
    private String name;
    private String description;
    private Region startRegion;
    private Region endRegion;
    private List<Region> checkpoints;
    private ServerLevel level;
    private ResourceKey<Level> dimension;
    private int totalLaps;
    private boolean isActive = false;
    public boolean isRacing = false;

//    public RaceTrack(String name) {
//        this.name = name;
//        this.description = "";
//        this.dimension = Level.OVERWORLD;
//        this.checkpoints = new ArrayList<>();
//        this.totalLaps = 1;
//    }

    public RaceTrack(String name, ServerLevel level) {
        this.name = name;
        this.level = level;
        this.checkpoints = new ArrayList<>();
        this.totalLaps = 1;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Region> getCheckpoints() {
        return checkpoints;
    }

    public void setName(String name) {
        this.name = name;
        callBack();
    }

    public void setDescription(String description) {
        this.description = description;
        callBack();
    }

    public void setStartRegion(Region startRegion) {
        this.startRegion = startRegion;
        callBack();
    }

    public void setEndRegion(Region endRegion) {
        this.endRegion = endRegion;
        callBack();
    }

    public Region getStartRegion() {
        return startRegion;
    }

    public Region getEndRegion() {
        return endRegion;
    }

    public void addCheckpoint(Region checkpoint) {
        this.checkpoints.add(checkpoint);
        callBack();
    }

    public boolean addCheckpointAt(int index, Region checkpoint) {
        if (index < 0 || index > checkpoints.size()) {
            return false;
        }
        this.checkpoints.add(index, checkpoint);
        callBack();
        return true;
    }

    public boolean removeCheckpoint(Region checkpoint) {
        if(this.checkpoints.remove(checkpoint)){
            callBack();
            return true;
        }
        return false;
    }

    public boolean removeCheckpointAt(int index) {
        if (index < 0 || index >= checkpoints.size()) {
            return false;
        }
        this.checkpoints.remove(index);
        callBack();
        return true;
    }

    @Override
    public String toString() {
        return
                "name: " + name + "\n" +
                "description: " + description + "\n" +
                "startRegion: " + startRegion + "\n"+
                "endRegion: " + endRegion + "\n" +
                "checkpoints: " + checkpoints.size() + "\n" +
                "Laps: " + totalLaps;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        callBack();
    }

    private static void callBack(){
        if(RaceTrackManager.modifyCallback != null) {
            RaceTrackManager.modifyCallback.run();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RaceTrack raceTrack)) return false;
        return Objects.equals(name, raceTrack.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int getTotalLaps() {
        return totalLaps;
    }

    public void setTotalLaps(int totalLaps) {
        this.totalLaps = totalLaps;
    }

    public ServerLevel getLevel() {
        return level;
    }
}
