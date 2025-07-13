package com.qwaecd.speeduuuuuuup.race;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RaceTrack {
    private String name;
    private String description;
    private Region startRegion;
    private Region endRegion;
    private List<Region> checkpoints;
    private ResourceKey<Level> dimension;
    private boolean isActive = false;

    public RaceTrack(String name) {
        this.name = name;
        this.description = "";
        this.dimension = Level.OVERWORLD;
        this.checkpoints = new ArrayList<>();
    }

    public RaceTrack(String name, ResourceKey<Level> level) {
        this.name = name;
        this.dimension = level;
        this.checkpoints = new ArrayList<>();
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

    public Boolean addCheckpointAt(int index, Region checkpoint) {
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

    public Boolean removeCheckpointAt(int index) {
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
                "checkpoints: " + checkpoints.size();
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
}
