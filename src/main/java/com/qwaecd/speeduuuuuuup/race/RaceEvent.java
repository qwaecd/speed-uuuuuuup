package com.qwaecd.speeduuuuuuup.race;

public class RaceEvent {
    public enum EventType {
        RACE_START,      // 比赛开始
        CHECKPOINT_PASS, // 通过检查点
        LAP_COMPLETE,    // 完成一圈
        RACE_FINISH,     // 比赛完成
        RACE_DISQUALIFY  // 取消资格
    }

    private final EventType eventType;
    private final RaceTrack raceTrack;
    private final RacePlayer racePlayer;
    private final Region region;
    private final int checkpointIndex;
    private final int currentLap;
    private final long timestamp;

    public RaceEvent(EventType eventType, RaceTrack raceTrack, RacePlayer racePlayer, Region region) {
        this(eventType, raceTrack, racePlayer, region, -1, -1);
    }

    public RaceEvent(EventType eventType, RaceTrack raceTrack, RacePlayer racePlayer, Region region, int checkpointIndex, int currentLap) {
        this.eventType = eventType;
        this.raceTrack = raceTrack;
        this.racePlayer = racePlayer;
        this.region = region;
        this.checkpointIndex = checkpointIndex;
        this.currentLap = currentLap;
        this.timestamp = System.currentTimeMillis();
    }

    public EventType getEventType() {
        return eventType;
    }

    public RaceTrack getRaceTrack() {
        return raceTrack;
    }

    public RacePlayer getRacePlayer() {
        return racePlayer;
    }

    public Region getRegion() {
        return region;
    }

    public int getCheckpointIndex() {
        return checkpointIndex;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
