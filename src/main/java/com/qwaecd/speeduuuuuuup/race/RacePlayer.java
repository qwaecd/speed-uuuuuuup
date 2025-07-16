package com.qwaecd.speeduuuuuuup.race;

import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;

import java.util.UUID;
import java.util.Objects;

public class RacePlayer {
    private final String name;
    private final UUID uuid;
    private final RaceTrack raceTrack;
    private RaceStatus raceStatus;
    private long startTime;
    private long finishTime;
    private int lastCheckpointIndex;
    public int totalLaps;

    public RacePlayer(String name, UUID uuid, RaceTrack raceTrack) {
        this.name = name;
        this.uuid = uuid;
        this.raceTrack = raceTrack;
        this.raceStatus = RaceStatus.WAITING;
        this.startTime = 0;
        this.finishTime = 0;

        this.lastCheckpointIndex = -1;
        this.totalLaps = 1;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public RaceTrack getRaceTrack() {
        return raceTrack;
    }

    public RaceStatus getRaceStatus() {
        return this.raceStatus;
    }

    public void setRaceStatus(RaceStatus raceStatus){
        this.raceStatus = raceStatus;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public int getLastCheckpointIndex() {
        return lastCheckpointIndex;
    }

    public void setLastCheckpointIndex(int lastCheckpointIndex) {
        this.lastCheckpointIndex = lastCheckpointIndex;
    }

    public void onFinish() {
        this.raceStatus = RaceStatus.WAITING;
        this.startTime = 0;
        this.finishTime = 0;
        this.lastCheckpointIndex = -1;
        this.totalLaps = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RacePlayer that)) return false;
        return uuid.equals(that.uuid) &&
                Objects.equals(raceTrack, that.raceTrack);
    }
    @Override
    public int hashCode() {
        return Objects.hash(uuid, raceTrack);
    }

    public enum RaceStatus {
        WAITING,
        RACING,
        COMPLETED,
        DISQUALIFIED;
    }
}
