package com.qwaecd.speeduuuuuuup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qwaecd.speeduuuuuuup.data.ModData;
import com.qwaecd.speeduuuuuuup.data.RaceResultData;
import com.qwaecd.speeduuuuuuup.data.RaceTrackData;
import com.qwaecd.speeduuuuuuup.entity.RegionMarkerEntity;
import com.qwaecd.speeduuuuuuup.init.RegisterEntities;
import com.qwaecd.speeduuuuuuup.race.RaceManager;
import com.qwaecd.speeduuuuuuup.race.RacePlayer;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.Region;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class RaceCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("race")
                        .then(
                                Commands.literal("init")
                                        .requires(source -> source.hasPermission(4))
                                        .then(Commands.argument("race_track_name", StringArgumentType.string())
                                                .executes(context -> initRace(context, StringArgumentType.getString(context, "race_track_name")))
                                        )
                        )
                        .then(
                                Commands.literal("join")
                                        .requires(source -> source.hasPermission(0))
                                        .then(Commands.argument("race_track_name", StringArgumentType.string())
                                                .executes(context -> joinRace(context, StringArgumentType.getString(context, "race_track_name"), context.getSource().getPlayer()))
                                        )
                        )
                        .then(
                                Commands.literal("leave")
                                        .requires(source -> source.hasPermission(0))
                                        .then(Commands.argument("race_track_name", StringArgumentType.string())
                                                .executes(context -> leaveRace(context, StringArgumentType.getString(context, "race_track_name"), context.getSource().getPlayer()))
                                        )
                        )
                        .then(
                                Commands.literal("run")
                                        .requires(source -> source.hasPermission(4))
                                        .then(Commands.argument("race_track_name", StringArgumentType.string())
                                                .executes(context -> runRace(context, StringArgumentType.getString(context, "race_track_name")))
                                        )
                        )
                        .then(
                                Commands.literal("stop")
                                        .requires(source -> source.hasPermission(4))
                                        .then(Commands.argument("race_track_name", StringArgumentType.string())
                                                .executes(context -> stopRace(context, StringArgumentType.getString(context, "race_track_name")))
                                        )
                        )
                        .then(
                                Commands.literal("save")
                                        .requires(source -> source.hasPermission(4))
                                        .then(Commands.argument("race_track_name", StringArgumentType.string())
                                                .executes(context -> saveResults(context, StringArgumentType.getString(context, "race_track_name")))
                                        )
                        )
        );
    }

    private static int initRace(CommandContext<CommandSourceStack> context, String raceTrackName) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrackData data = ModData.getRaceTrackData(level);
        RaceTrack raceTrack = data.getRaceTrack(raceTrackName);
        if (raceTrack == null) {
            context.getSource().sendFailure(Component.literal("failed to find"));
            return 0;
        }
        if (raceTrack.isActive()) {
            context.getSource().sendSuccess(()->Component.literal("Race is already active"), false);
            return 0;
        }
        EntityType<RegionMarkerEntity> entityType = RegisterEntities.REGION_ENTITY.get();
        Region startRegion = raceTrack.getStartRegion();
        Region endRegion = raceTrack.getEndRegion();

        if (startRegion == null || endRegion == null) {
            context.getSource().sendSuccess(()->Component.literal("Start or End region is not set"), false);
            return 0;
        }
        RegionMarkerEntity startMarker = new RegionMarkerEntity(entityType, level, startRegion, raceTrack);
        level.addFreshEntity(startMarker);
        startMarker.move();
        RegionMarkerEntity endMarker = new RegionMarkerEntity(entityType, level, endRegion, raceTrack);
        level.addFreshEntity(endMarker);
        endMarker.move();


        for (Region checkpoint : raceTrack.getCheckpoints()) {
            if (checkpoint == null) continue;
            RegionMarkerEntity entity = new RegionMarkerEntity(entityType, level, checkpoint, raceTrack);
            level.addFreshEntity(entity);
            entity.move();
        }
        context.getSource().sendSuccess(()->Component.literal("Successfully"), true);
//        raceTrack.setActive(true);
        return 1;
    }

    private static int joinRace(CommandContext<CommandSourceStack> context, String raceId, Player player) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrackData data = ModData.getRaceTrackData(level);
        RaceTrack raceTrack = data.getRaceTrack(raceId);
        if (player==null){
            context.getSource().sendFailure(Component.literal("You must be a player"));
            return 0;
        }
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " does not exist."), false);
            return 0;
        }
        RacePlayer racePlayer = new RacePlayer(player.getName().getString(), player.getUUID(), raceTrack);
        if (RaceManager.joinRace(raceTrack, racePlayer)){
            context.getSource().sendSuccess(() -> Component.literal("Player " + player.getName().getString() + " joined."), false);
            return 1;
        }
        context.getSource().sendSuccess(() -> Component.literal("Failed to join"), false);
        return 0;
    }

    private static int leaveRace(CommandContext<CommandSourceStack> context, String raceId, Player player) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrackData data = ModData.getRaceTrackData(level);
        RaceTrack raceTrack = data.getRaceTrack(raceId);
        if (player==null){
            context.getSource().sendFailure(Component.literal("You must be a player"));
            return 0;
        }
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " does not exist."), false);
            return 0;
        }
        RacePlayer racePlayer = new RacePlayer(player.getName().getString(), player.getUUID(), raceTrack);
        RaceManager.leaveRace(raceTrack, racePlayer);
        context.getSource().sendSuccess(() -> Component.literal("Player " + player.getName().getString() + " leaved."), false);
        return 1;
    }

    private static int runRace(CommandContext<CommandSourceStack> context, String raceId) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrackData data = ModData.getRaceTrackData(level);
        RaceTrack raceTrack = data.getRaceTrack(raceId);
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " does not exist."), false);
            return 0;
        }
        raceTrack.isRacing = true;
        context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " is now racing."), false);
        return 1;
    }

    private static int stopRace(CommandContext<CommandSourceStack> context, String raceId) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrackData data = ModData.getRaceTrackData(level);
        RaceTrack raceTrack = data.getRaceTrack(raceId);
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " does not exist."), false);
            return 0;
        }
        raceTrack.isRacing = false;
        for (RacePlayer racePlayer : RaceManager.getInstance().get(raceId)) {
            if (racePlayer != null)
                racePlayer.onFinish();
        }
        context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " is now stopped."), false);
        return 1;
    }

    private static int saveResults(CommandContext<CommandSourceStack> context, String raceId) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrackData data = ModData.getRaceTrackData(level);
        RaceTrack raceTrack = data.getRaceTrack(raceId);
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceId + " does not exist."), false);
            return 0;
        }
        List<RaceManager.PlayerResultCache> resultsCaches = RaceManager.getPlayerResultsCache(raceId);
        if (resultsCaches != null){
            RaceResultData raceResultData = ModData.getRaceResultData(level);
            for (var playerResultCache : resultsCaches) {
                if (playerResultCache == null) continue;
                raceResultData.putWithCompare(raceId, playerResultCache.playerUUID(), playerResultCache.playerResult());
            }
            RaceManager.clearPlayerResultsCache(raceId);
        }

        context.getSource().sendSuccess(() -> Component.literal("Race result saved."), false);
        return 1;
    }
}
