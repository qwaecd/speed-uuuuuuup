package com.qwaecd.speeduuuuuuup.race;

import com.qwaecd.speeduuuuuuup.data.PlayerResult;
import com.qwaecd.speeduuuuuuup.data.RaceTrackData;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RaceManager {
    // RaceName -> RacePlayers
    private static Map<String, Set<RacePlayer>> INSTANCE = null;

    private static final Map<String, List<PlayerResultCache>> playerResultsDataCache = new HashMap<>();

    // playerUUID -> RaceTrack name 的快速查找索引
    private static final Map<UUID, String> playerToRaceTrackIndex = new ConcurrentHashMap<>();

    // 缓存有效的 RaceTrack 名称，避免重复检查
    private static final Set<String> validRaceTrackCache = ConcurrentHashMap.newKeySet();

    // 缓存失效时间戳
    private static volatile long cacheLastUpdateTime = 0;
    private static final long CACHE_TIMEOUT = 5000; // 5秒缓存

    public static Map<String, Set<RacePlayer>> getInstance() {
        if (INSTANCE == null) {
            synchronized (RaceManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HashMap<>();
                }
            }
        }
        return INSTANCE;
    }

    public static void addPlayerResultCache(String raceTrackId, UUID playerUUID, PlayerResult playerResult) {
        if (raceTrackId == null || raceTrackId.isEmpty() || playerResult == null) {
            return;
        }
        List<PlayerResultCache> cacheList = playerResultsDataCache.getOrDefault(raceTrackId, new ArrayList<>());
        cacheList.add(new PlayerResultCache(playerUUID, playerResult));
    }

    public static void addPlayerResultCache(String raceTrackId, PlayerResultCache playerResultCache){
        if (raceTrackId == null || raceTrackId.isEmpty() || playerResultCache == null) {
            return;
        }
        List<PlayerResultCache> cacheList = playerResultsDataCache.computeIfAbsent(raceTrackId, k -> new ArrayList<>());
        cacheList.add(playerResultCache);
    }

    public static List<PlayerResultCache> getPlayerResultsCache(String raceTrackId) {
        if (raceTrackId == null || raceTrackId.isEmpty()) {
            return Collections.emptyList();
        }
        return playerResultsDataCache.get(raceTrackId);
    }

    public static void clearPlayerResultsCache(String raceTrackId) {
        playerResultsDataCache.remove(raceTrackId);
    }

    public static boolean joinRace(RaceTrack raceTrack, RacePlayer player){
        String raceTrackId = raceTrack.getName();
        if (!isValidRaceTrack(raceTrackId)){
            return false;
        }

        Map<String, Set<RacePlayer>> instance = getInstance();
        if (!instance.containsKey(raceTrackId)) {
            instance.put(raceTrackId, new HashSet<>());
        }

        instance.get(raceTrackId).add(player);
        // 更新索引
        playerToRaceTrackIndex.put(player.getUUID(), raceTrackId);
        return true;
    }

    public static void leaveRace(RaceTrack raceTrack, RacePlayer player) {
        String raceTrackId = raceTrack.getName();
        if (!isValidRaceTrack(raceTrackId)){
            return;
        }

        Map<String, Set<RacePlayer>> instance = getInstance();
        if (instance.containsKey(raceTrackId)) {
            instance.get(raceTrackId).remove(player);
            // 更新索引
            playerToRaceTrackIndex.remove(player.getUUID());
        }
    }

    public static boolean inRace(RaceTrack raceTrack, RacePlayer player) {
        String raceTrackId = raceTrack.getName();
        if (!isValidRaceTrack(raceTrackId)) {
            return false;
        }
        Map<String, Set<RacePlayer>> instance = getInstance();
        if (!instance.containsKey(raceTrackId)) {
            return false;
        }
        Set<RacePlayer> players = instance.get(raceTrackId);
        return players.contains(player);
    }

    public static boolean inRace(RaceTrack raceTrack, UUID playerUUID) {
        if (raceTrack == null || playerUUID == null) {
            return false;
        }

        String raceTrackId = raceTrack.getName();

        // 使用索引快速查找
        String playerCurrentRaceTrack = playerToRaceTrackIndex.get(playerUUID);
        if (playerCurrentRaceTrack == null) {
            return false;
        }

        // 检查玩家是否在指定的赛道中
        if (!raceTrackId.equals(playerCurrentRaceTrack)) {
            return false;
        }

        // 验证赛道是否有效
        if (!isValidRaceTrack(raceTrackId)) {
            // 赛道无效，清理索引
            playerToRaceTrackIndex.remove(playerUUID);
            return false;
        }

        return true;
    }

    public static @Nullable RacePlayer getRacePlayer(RaceTrack raceTrack, UUID playerUUID) {
        if (raceTrack == null || playerUUID == null) {
            return null;
        }

        String raceTrackId = raceTrack.getName();

        // 首先检查玩家是否在这个赛道中
        String playerCurrentRaceTrack = playerToRaceTrackIndex.get(playerUUID);
        if (!raceTrackId.equals(playerCurrentRaceTrack)) {
            return null;
        }

        // 验证赛道是否有效
        if (!isValidRaceTrack(raceTrackId)) {
            // 赛道无效，清理索引
            playerToRaceTrackIndex.remove(playerUUID);
            return null;
        }

        // 获取赛道中的所有玩家
        Map<String, Set<RacePlayer>> instance = getInstance();
        Set<RacePlayer> players = instance.get(raceTrackId);

        if (players == null || players.isEmpty()) {
            // 数据不一致，清理索引
            playerToRaceTrackIndex.remove(playerUUID);
            return null;
        }

        // 遍历找到匹配的 RacePlayer
        for (RacePlayer player : players) {
            if (player.getUUID().equals(playerUUID)) {
                return player;
            }
        }

        // 如果没找到，说明数据不一致，清理索引
        playerToRaceTrackIndex.remove(playerUUID);
        return null;
    }

    private static boolean isValidRaceTrack(String raceTrackId) {
        long currentTime = System.currentTimeMillis();

        // 检查缓存是否过期
        if (currentTime - cacheLastUpdateTime > CACHE_TIMEOUT) {
            synchronized (RaceManager.class) {
                if (currentTime - cacheLastUpdateTime > CACHE_TIMEOUT) {
                    validRaceTrackCache.clear();
                    cacheLastUpdateTime = currentTime;
                }
            }
        }

        // 检查缓存
        if (validRaceTrackCache.contains(raceTrackId)) {
            return true;
        }

        // 检查并更新缓存
        boolean isValid = RaceTrackData.containsRaceTrack(raceTrackId);
        if (isValid) {
            validRaceTrackCache.add(raceTrackId);
        }

        return isValid;
    }

    // 清理索引的辅助方法
    public static void clearPlayerIndex(UUID playerUUID) {
        playerToRaceTrackIndex.remove(playerUUID);
    }

    // 清理所有缓存的方法
    public static void clearCache() {
        synchronized (RaceManager.class) {
            validRaceTrackCache.clear();
            playerToRaceTrackIndex.clear();
            cacheLastUpdateTime = 0;
        }
    }

    public record PlayerResultCache(UUID playerUUID, PlayerResult playerResult) {
    }
}