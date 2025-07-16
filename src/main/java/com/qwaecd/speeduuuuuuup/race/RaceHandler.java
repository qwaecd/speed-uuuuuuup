package com.qwaecd.speeduuuuuuup.race;


import com.qwaecd.speeduuuuuuup.race.event.RaceEvent;
import com.qwaecd.speeduuuuuuup.race.event.RaceEventManager;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.Region;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RaceHandler {

    public static void racing(@NonNull RaceTrack raceTrack, @NonNull RacePlayer racePlayer, @NonNull Region region) {
        RacePlayer.RaceStatus raceStatus = racePlayer.getRaceStatus();
        switch (raceStatus) {
            case WAITING -> isWaiting(raceTrack, racePlayer, region);
            case RACING -> isRacing(raceTrack, racePlayer, region);
            case COMPLETED -> isComplied(raceTrack, racePlayer, region);
            case DISQUALIFIED -> isDisqualified(raceTrack, racePlayer, region);
            default -> {
                return;
            }
        }
    }

    private static void isWaiting(RaceTrack raceTrack, RacePlayer racePlayer, Region region){
        Region.PointType pointType = region.getPointType();
        if (pointType != Region.PointType.START) {
            return;
        }
        if (raceTrack.isRacing){
            racePlayer.setRaceStatus(RacePlayer.RaceStatus.RACING);
            racePlayer.setStartTime(System.currentTimeMillis());

            // 触发比赛开始事件
            RaceEvent startEvent = new RaceEvent(
                RaceEvent.EventType.RACE_START,
                raceTrack,
                racePlayer,
                region
            );
            RaceEventManager.fireEvent(startEvent);
        }
    }

    private static void isRacing(RaceTrack raceTrack, RacePlayer racePlayer, Region region){
        Region.PointType pointType = region.getPointType();
        if (pointType == Region.PointType.CHECKPOINT) {
            int checkpointIndex = raceTrack.getCheckpoints().indexOf(region);
            if (checkpointIndex == -1) {
                return; // 未找到对应的检查点
            }
            if (checkpointIndex == racePlayer.getLastCheckpointIndex() + 1) {
                racePlayer.setLastCheckpointIndex(checkpointIndex);

                // 触发检查点通过事件
                RaceEvent checkpointEvent = new RaceEvent(
                    RaceEvent.EventType.CHECKPOINT_PASS,
                    raceTrack,
                    racePlayer,
                    region,
                    checkpointIndex,
                    racePlayer.totalLaps
                );
                RaceEventManager.fireEvent(checkpointEvent);
            }
        } else if (pointType == Region.PointType.END && passedAllCheckpoints(raceTrack, racePlayer)) {
            if (racePlayer.totalLaps > 1){
                racePlayer.totalLaps--;
                // 触发完成一圈事件
                RaceEvent lapEvent = new RaceEvent(
                    RaceEvent.EventType.LAP_COMPLETE,
                    raceTrack,
                    racePlayer,
                    region,
                    -1,
                    racePlayer.totalLaps
                );
                RaceEventManager.fireEvent(lapEvent);
            } else {
                racePlayer.setRaceStatus(RacePlayer.RaceStatus.COMPLETED);
                racePlayer.setFinishTime(System.currentTimeMillis());
                raceTrack.isRacing = false;

                // 触发比赛完成事件
                RaceEvent finishEvent = new RaceEvent(
                    RaceEvent.EventType.RACE_FINISH,
                    raceTrack,
                    racePlayer,
                    region
                );
                RaceEventManager.fireEvent(finishEvent);
            }
        }
    }

    private static void isComplied(RaceTrack raceTrack, RacePlayer racePlayer, Region region){
        // 已完成状态，可能需要处理一些清理工作
    }

    private static void isDisqualified(RaceTrack raceTrack, RacePlayer racePlayer, Region region){
        // 触发取消资格事件
        RaceEvent disqualifyEvent = new RaceEvent(
            RaceEvent.EventType.RACE_DISQUALIFY,
            raceTrack,
            racePlayer,
            region
        );
        RaceEventManager.fireEvent(disqualifyEvent);
    }

    private static boolean passedAllCheckpoints(RaceTrack raceTrack, RacePlayer racePlayer) {
        int checkPointsNumber = raceTrack.getCheckpoints().size();
        int lastCheckpointIndex = racePlayer.getLastCheckpointIndex();
        return checkPointsNumber == 0 || checkPointsNumber - 1 == lastCheckpointIndex;
    }
}
