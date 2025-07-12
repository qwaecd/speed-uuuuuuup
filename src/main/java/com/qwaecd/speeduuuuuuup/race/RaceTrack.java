package com.qwaecd.speeduuuuuuup.race;

import java.util.ArrayList;
import java.util.List;

public class RaceTrack {
    private String name;
    private String description;
    private Region startRegion;
    private Region endRegion;
    private List<Region> checkpoints;

    public RaceTrack(String name) {
        this.name = name;
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
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartRegion(Region startRegion) {
        this.startRegion = startRegion;
    }

    public void setEndRegion(Region endRegion) {
        this.endRegion = endRegion;
    }

    public Region getStartRegion() {
        return startRegion;
    }

    public Region getEndRegion() {
        return endRegion;
    }

    public void addCheckpoint(Region checkpoint) {
        this.checkpoints.add(checkpoint);
    }

    public Boolean addCheckpointAt(int index, Region checkpoint) {
        if (index < 0 || index > checkpoints.size()) {
            return false;
        }
        this.checkpoints.add(index, checkpoint);
        return true;
    }

    public Boolean removeCheckpoint(Region checkpoint) {
        return this.checkpoints.remove(checkpoint);
    }

    public Boolean removeCheckpointAt(int index) {
        if (index < 0 || index >= checkpoints.size()) {
            return false;
        }
        this.checkpoints.remove(index);
        return true;
    }

    @Override
    public String toString() {
        return
                "name: '" + name + "'\n" +
                "description: '" + description + "'\n" +
                "startRegion: " + startRegion + "\n"+
                "endRegion: " + endRegion + "\n" +
                "checkpoints: " + checkpoints.size();
    }
}
