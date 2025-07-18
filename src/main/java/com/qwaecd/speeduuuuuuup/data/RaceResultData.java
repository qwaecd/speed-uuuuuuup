package com.qwaecd.speeduuuuuuup.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RaceResultData extends SavedData {
    private static final Map<String, Map<UUID, PlayerResult>> raceResults = new HashMap<>();

    public void putWithCompare(String raceId, UUID playerUUID, PlayerResult playerResult){
        if (raceId == null || raceId.isEmpty() || playerUUID == null) {
            return;
        }
        Map<UUID, PlayerResult> results = raceResults.getOrDefault(raceId, new HashMap<>());
        if (results.containsKey(playerUUID)) {
            PlayerResult existingResult = results.get(playerUUID);
            if (existingResult.compareTo(playerResult) > 0) {
                results.put(playerUUID, playerResult);
            }
        } else {
            results.put(playerUUID, playerResult);
        }
        raceResults.put(raceId, results);
        setDirty();
    }

    public @Nullable PlayerResult getResult(String raceId, UUID playerUUID) {
        if (raceId == null || raceId.isEmpty() || playerUUID == null) {
            return null;
        }
        Map<UUID, PlayerResult> results = raceResults.get(raceId);
        if (results != null) {
            return results.get(playerUUID);
        }
        return null;
    }

    public List<PlayerResult> getOrderedResults(String raceId) {
        if (raceId == null || raceId.isEmpty()) {
            return List.of();
        }
        Map<UUID, PlayerResult> results = raceResults.get(raceId);
        if (results != null) {
            return results.values().stream()
                    .sorted(PlayerResult::compareTo)
                    .toList();
        }
        return List.of();
    }
    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        CompoundTag rootTag = new CompoundTag();
        for (var entry : raceResults.entrySet()){
            String raceId = entry.getKey();
            if (raceId == null || raceId.isEmpty()) {
                continue;
            }
            CompoundTag raceTag = getCompoundTag(entry);

            rootTag.put(raceId, raceTag);
        }

        compoundTag.put("result", rootTag);
        return compoundTag;
    }

    private static CompoundTag getCompoundTag(Map.Entry<String, Map<UUID, PlayerResult>> entry) {
        CompoundTag raceTag = new CompoundTag();
        Map<UUID, PlayerResult> results = entry.getValue();
        for (var resultEntry : results.entrySet()) {
            UUID playerId = resultEntry.getKey();
            PlayerResult playerResult = resultEntry.getValue();
            CompoundTag playerTag = new CompoundTag();
            playerTag.putString("player_name", playerResult.playerName());
            playerTag.putLong("finish_time", playerResult.finishTime());
            raceTag.put(playerId.toString(), playerTag);
        }
        return raceTag;
    }

    public static RaceResultData load(CompoundTag compoundTag) {
        RaceResultData raceResultData = new RaceResultData();
        CompoundTag tag = compoundTag.getCompound("result");
        for (String raceId : tag.getAllKeys()) {
            CompoundTag raceTag = tag.getCompound(raceId);
            Map<UUID, PlayerResult> results = new HashMap<>();

            for (String playerId : raceTag.getAllKeys()) {
                CompoundTag playerTag = raceTag.getCompound(playerId);
                String playerName = playerTag.getString("player_name");
                long finishTime = playerTag.getLong("finish_time");
                PlayerResult playerResult = new PlayerResult(playerName, finishTime);
                results.put(UUID.fromString(playerId), playerResult);
            }

            raceResults.put(raceId, results);
        }
        return raceResultData;
    }
}
