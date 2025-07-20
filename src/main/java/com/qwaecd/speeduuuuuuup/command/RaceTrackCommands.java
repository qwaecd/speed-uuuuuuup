package com.qwaecd.speeduuuuuuup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qwaecd.speeduuuuuuup.data.ModData;
import com.qwaecd.speeduuuuuuup.data.PlayerResult;
import com.qwaecd.speeduuuuuuup.data.RaceResultData;
import com.qwaecd.speeduuuuuuup.data.RaceTrackData;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrackManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.server.command.EnumArgument;

import java.util.List;

public class RaceTrackCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("racetrack")
                        .then(
                                Commands.literal("create")
                                    .requires(source -> source.hasPermission(4))
                                    .then(Commands.argument("name", StringArgumentType.string())
                                            .executes(context -> createRaceTrack(context, StringArgumentType.getString(context, "name")))
                                    )
                        )
                        .then(
                                Commands.literal("remove")
                                        .requires(source -> source.hasPermission(4))
                                        .then(Commands.argument("name", StringArgumentType.string())
                                                .executes(context -> removeRaceTrack(context, StringArgumentType.getString(context, "name"))))
                        )
                        .then(
                                Commands.literal("list")
                                        .requires(source -> source.hasPermission(0))
                                        .executes(RaceTrackCommands::listRaceTracks)
                        )
                        .then(
                                Commands.literal("info")
                                        .requires(source -> source.hasPermission(0))
                                        .then(
                                                Commands.argument("name", StringArgumentType.string()).executes(context->infoRaceTrack(context, StringArgumentType.getString(context, "name")))
                                                        .then(Commands.argument("type", EnumArgument.enumArgument(PointType.class)).executes(context -> infoPoint(context, StringArgumentType.getString(context, "name"), context.getArgument("type", PointType.class))))
                                        )
                        )
                        .then(
                                Commands.literal("rankinglist")
                                        .requires(source -> source.hasPermission(0))
                                        .then(Commands.argument("name", StringArgumentType.string())
                                                .executes(context -> getRankingList(context, StringArgumentType.getString(context, "name"))))
                        )

        );
    }

    private static int createRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        if(RaceTrackManager.createRaceTrack(name, new RaceTrack(name), context.getSource().getLevel())) {
            context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.create.success"),true);
            return 1;
        }
        context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.create.already_exists", name),true);
        return 0;
    }

    private static int removeRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        if(RaceTrackManager.removeRaceTrack(name, context.getSource().getLevel())) {
            context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.remove.success", name),true);
            return 1;
        }
        context.getSource().sendSuccess(()-> Component.translatable("speed_uuuuuuup.command.racetrack.remove.not_exists", name), false);
        return 0;
    }

    private static int listRaceTracks(CommandContext<CommandSourceStack> context) {
        StringBuilder sb = new StringBuilder();
        RaceTrackData data = ModData.getRaceTrackData(context.getSource().getLevel());
        for(String name : data.getRaceTracks().keySet()) {
            RaceTrack raceTrack = RaceTrackManager.getRaceTrack(name, context.getSource().getLevel());
            if (raceTrack != null) {
                context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.list.name", raceTrack.getName()), false);
            }
        }
        return 1;
    }

    private static int infoRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(name, context.getSource().getLevel());
        if (raceTrack != null) {
            context.getSource().sendSuccess(() -> Component.literal(raceTrack.toString()), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.info.not_exists", name), false);
        }
        return 1;
    }

    private static int infoPoint(CommandContext<CommandSourceStack> context, String raceTrackName, PointType type) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(raceTrackName, context.getSource().getLevel());
        if (raceTrack != null) {
            context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.info.racetrack", raceTrackName), false);
            if (type == PointType.START && raceTrack.getStartRegion() != null) {
                context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.info.start", raceTrack.getStartRegion().toString()), false);
            } else if (type == PointType.END && raceTrack.getEndRegion() != null) {
                context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.info.end", raceTrack.getEndRegion().toString()), false);
            }

            if (type == PointType.CHECKPOINT) {
                for (var checkpoint : raceTrack.getCheckpoints()) {
                    context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.info.checkpoint", checkpoint.toString()), false);
                }
            }
            return 1;
        }
        context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.ranking.not_exists", raceTrackName), false);
        return 0;
    }

    private static int getRankingList(CommandContext<CommandSourceStack> context, String raceTrackId) {
        ServerLevel level = context.getSource().getLevel();
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(raceTrackId, level);
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.ranking.not_exists", raceTrackId), false);
            return 0;
        }
        RaceResultData raceResultData = ModData.getRaceResultData(level);
        int index = 0;
        List<PlayerResult> list = raceResultData.getOrderedResults(raceTrackId);
        context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.ranking.title", raceTrackId), false);
        while (index < Math.min(10, list.size())) {
            PlayerResult playerResult = list.get(index);
            int Num = index + 1;
            context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.racetrack.ranking.entry", Num, playerResult.toString()),false);
            index++;
        }

        return 1;
    }
    enum PointType {
        START,
        END,
        CHECKPOINT
    }
}
