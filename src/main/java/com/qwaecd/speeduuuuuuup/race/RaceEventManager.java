package com.qwaecd.speeduuuuuuup.race;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RaceEventManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<RaceEventListener> listeners = new CopyOnWriteArrayList<>();

    public static void addListener(RaceEventListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(RaceEventListener listener) {
        listeners.remove(listener);
    }

    public static void fireEvent(RaceEvent event) {
        for (RaceEventListener listener : listeners) {
            try {
                listener.onRaceEvent(event);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public static void clearAllListeners() {
        listeners.clear();
    }
}
