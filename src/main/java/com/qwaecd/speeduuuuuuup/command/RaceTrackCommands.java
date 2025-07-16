package com.qwaecd.speeduuuuuuup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qwaecd.speeduuuuuuup.data.ModData;
import com.qwaecd.speeduuuuuuup.data.RaceTrackData;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrackManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

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

        );
    }

    private static int createRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        if(RaceTrackManager.createRaceTrack(name, new RaceTrack(name), context.getSource().getLevel())) {
            context.getSource().sendSuccess(() -> Component.literal("Successfully"),true);
            return 1;
        }
        context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + name + " already exist."),true);
        return 0;
    }

    private static int removeRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        if(RaceTrackManager.removeRaceTrack(name, context.getSource().getLevel())) {
            context.getSource().sendSuccess(() -> Component.literal("Removed " + name),true);
            return 1;
        }
        context.getSource().sendSuccess(()-> Component.literal("RaceTrack " + name + " does not exist."), false);
        return 0;
    }

    private static int listRaceTracks(CommandContext<CommandSourceStack> context) {
        StringBuilder sb = new StringBuilder();
        RaceTrackData data = ModData.getRaceTrackData(context.getSource().getLevel());
        for(String name : data.getRaceTracks().keySet()) {
            RaceTrack raceTrack = RaceTrackManager.getRaceTrack(name, context.getSource().getLevel());
            if (raceTrack != null) {
                sb.append("Name: ").append(raceTrack.getName()).append("\n");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        context.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
        return 1;
    }

    private static int infoRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(name, context.getSource().getLevel());
        StringBuilder sb = new StringBuilder();
        if (raceTrack != null) {
            sb.append(raceTrack);
        } else {
            sb.append(name).append(" does not exist.");
        }
        context.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
        return 1;
    }

    private static int infoPoint(CommandContext<CommandSourceStack> context, String raceTrackName, PointType type) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(raceTrackName, context.getSource().getLevel());
        if (raceTrack != null) {
            context.getSource().sendSuccess(() -> Component.literal("RaceTrack: " + raceTrackName), false);
            if (type == PointType.START && raceTrack.getStartRegion() != null) {
                context.getSource().sendSuccess(() -> Component.literal("Start: " + raceTrack.getStartRegion()), false);
            } else if (type == PointType.END && raceTrack.getEndRegion() != null) {
                context.getSource().sendSuccess(() -> Component.literal("End: " + raceTrack.getEndRegion()), false);
            }

            if (type == PointType.CHECKPOINT) {
                for (var checkpoint : raceTrack.getCheckpoints()) {
                    context.getSource().sendSuccess(() -> Component.literal("Checkpoint: " + checkpoint), false);
                }
            }
            return 1;
        }
        context.getSource().sendSuccess(() -> Component.literal("RaceTrack " + raceTrackName + " does not exist."), false);
        return 0;
    }

    enum PointType {
        START,
        END,
        CHECKPOINT
    }
}
