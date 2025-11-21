package com.qwaecd.speeduuuuuuup.race.runtime;

import com.qwaecd.speeduuuuuuup.data.PlayerResult;

import java.util.ArrayList;
import java.util.List;

public class RaceRuntime {
    private final List<PlayerResult> resultCaches = new ArrayList<>();

    public void addResult(PlayerResult result) {
        this.resultCaches.add(result);
        this.resultCaches.sort(PlayerResult::compareTo);
    }

    public String getContent() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.resultCaches.size(); i++) {
            builder.append("#").append(i+1).append(" ");
            builder.append(this.resultCaches.get(i).toString()).append("\n");
        }
        return builder.toString();
    }

    public void reset() {
        this.resultCaches.clear();
    }
}
