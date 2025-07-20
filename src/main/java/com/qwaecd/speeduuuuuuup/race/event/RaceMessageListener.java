package com.qwaecd.speeduuuuuuup.race.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class RaceMessageListener implements RaceEventListener {

    @Override
    public void onRaceEvent(RaceEvent event) {
        ServerPlayer player = getPlayerByUUID(event.getRacePlayer().getUUID());
        if (player == null) {
            return;
        }

        switch (event.getEventType()) {
            case RACE_START -> {
                player.sendSystemMessage(Component.translatable("speed_uuuuuuup.race.message.start"));
            }
            case CHECKPOINT_PASS -> {
                int checkpointIndex = event.getCheckpointIndex();
                player.sendSystemMessage(Component.translatable(
                    "speed_uuuuuuup.race.message.checkpoint_pass",
                    checkpointIndex + 1,
                    event.getRaceTrack().getCheckpoints().size()
                ));
            }
            case LAP_COMPLETE -> {
                int needLaps = event.getRaceTrack().getTotalLaps() - event.getRacePlayer().laps;
                player.sendSystemMessage(Component.translatable(
                    "speed_uuuuuuup.race.message.lap_complete",
                        needLaps
                ));
            }
            case RACE_FINISH -> {
                long raceTime = event.getTimestamp() - event.getRacePlayer().getStartTime();
                player.sendSystemMessage(Component.translatable(
                    "speed_uuuuuuup.race.message.finish",
                    formatTime(raceTime)
                ));
            }
            case RACE_DISQUALIFY -> {
                player.sendSystemMessage(Component.translatable("speed_uuuuuuup.race.message.disqualify"));
            }
        }
    }

    private ServerPlayer getPlayerByUUID(UUID uuid) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return null;
        }
        return server.getPlayerList().getPlayer(uuid);
    }

    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long ms = milliseconds % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, ms);
    }
}
