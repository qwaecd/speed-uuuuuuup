package com.qwaecd.speeduuuuuuup.data;

public record PlayerResult(String playerName, long finishTime) implements Comparable<PlayerResult> {

    @Override
    public int compareTo(PlayerResult other) {
        return Long.compare(this.finishTime, other.finishTime);
    }

    @Override
    public String toString() {
        return playerName + ": " + formatTime(finishTime);
    }

    private static String formatTime(long time) {
        long seconds = time / 1000;
        long milliseconds = time % 1000;
        return String.format("%02d:%03d", seconds, milliseconds);
    }
}
