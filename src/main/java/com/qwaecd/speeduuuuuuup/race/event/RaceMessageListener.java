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
                player.sendSystemMessage(Component.literal("§a比赛开始！加油！"));
            }
            case CHECKPOINT_PASS -> {
                int checkpointIndex = event.getCheckpointIndex();
                player.sendSystemMessage(Component.literal(
                    "§e通过检查点 " + (checkpointIndex + 1) + "/" + event.getRaceTrack().getCheckpoints().size()
                ));
            }
            case LAP_COMPLETE -> {
                player.sendSystemMessage(Component.literal(
                    "§b还剩 " + event.getRacePlayer().totalLaps + " 圈"
                ));
            }
            case RACE_FINISH -> {
                long raceTime = event.getTimestamp() - event.getRacePlayer().getStartTime();
                player.sendSystemMessage(Component.literal(
                    "§6恭喜！您完成了比赛！用时：" + formatTime(raceTime)
                ));
            }
            case RACE_DISQUALIFY -> {
                player.sendSystemMessage(Component.literal("§c您已被取消比赛资格！"));
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
