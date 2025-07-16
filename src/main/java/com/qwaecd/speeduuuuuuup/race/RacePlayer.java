package com.qwaecd.speeduuuuuuup.race;

import java.util.UUID;
import java.util.Objects;

public class RacePlayer {
    private final String name;
    private final UUID uuid;
    private final RaceTrack raceTrack;

    public RacePlayer(String name, UUID uuid, RaceTrack raceTrack) {
        this.name = name;
        this.uuid = uuid;
        this.raceTrack = raceTrack;
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
}
