package com.qwaecd.speeduuuuuuup.data;

import javax.annotation.Nonnull;

public record PlayerResult(String playerName, long finishTime) implements Comparable<PlayerResult> {

    @Override
    public int compareTo(PlayerResult other) {
        return Long.compare(this.finishTime, other.finishTime);
    }

    @Nonnull
    @Override
    public String toString() {
        return playerName + ": " + formatTime(finishTime);
    }

    private static String formatTime(long time) {
        long minutes = time / 60000;
        long seconds = (time % 60000) / 1000;
        long milliseconds = time % 1000;
        return String.format("%d min:%02d s:%03d ms", minutes, seconds, milliseconds);
    }
}
