package com.qwaecd.speeduuuuuuup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qwaecd.speeduuuuuuup.data.ModData;
import com.qwaecd.speeduuuuuuup.data.RaceTrackData;
import com.qwaecd.speeduuuuuuup.entity.RegionMarkerEntity;
import com.qwaecd.speeduuuuuuup.init.RegisterEntities;
import com.qwaecd.speeduuuuuuup.race.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.Region;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;

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
}
