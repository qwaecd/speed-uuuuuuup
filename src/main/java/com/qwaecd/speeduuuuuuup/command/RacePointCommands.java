package com.qwaecd.speeduuuuuuup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qwaecd.speeduuuuuuup.race.structure.CuboidRegion;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrack;
import com.qwaecd.speeduuuuuuup.race.structure.RaceTrackManager;
import com.qwaecd.speeduuuuuuup.race.structure.Region;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class RacePointCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("racepoint")
                        .requires(source -> source.hasPermission(4))
                        .then(
                                Commands.literal("edit")
                                        .then(Commands.argument("name", StringArgumentType.string()).executes(context->editRaceTrack(context, StringArgumentType.getString(context, "name"))))
                        )
                        .then(
                                Commands.literal("add")
                                        .then(
                                                Commands.literal("start")
                                                        .then(
                                                                Commands.argument("start pos", BlockPosArgument.blockPos())
                                                                        .then(Commands.argument("end pos", BlockPosArgument.blockPos())
                                                                        .executes(context -> addPoint(context, 0, BlockPosArgument.getBlockPos(context, "start pos"), BlockPosArgument.getBlockPos(context, "end pos"))))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("end")
                                                        .then(
                                                                Commands.argument("start pos", BlockPosArgument.blockPos())
                                                                        .then(Commands.argument("end pos", BlockPosArgument.blockPos())
                                                                        .executes(context -> addPoint(context, 1, BlockPosArgument.getBlockPos(context, "start pos"), BlockPosArgument.getBlockPos(context, "end pos"))))
                                                        )
                                        )
                                        .then(
                                                Commands.literal("checkpoint")
                                                        .then(
                                                                Commands.argument("start pos", BlockPosArgument.blockPos()).then(Commands.argument("end pos", BlockPosArgument.blockPos())
                                                                        .executes(context -> addPoint(context, 2, BlockPosArgument.getBlockPos(context, "start pos"), BlockPosArgument.getBlockPos(context, "end pos")))
                                                                        .then(
                                                                                Commands.argument("index", IntegerArgumentType.integer(0)).executes(context -> addPointAt(context, BlockPosArgument.getBlockPos(context, "start pos"), BlockPosArgument.getBlockPos(context, "end pos"), IntegerArgumentType.getInteger(context, "index")))
                                                                        )
                                                                )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("remove")
                                        .then(
                                                Commands.literal("checkpoint")
                                                        .then(
                                                                Commands.argument("index", IntegerArgumentType.integer())
                                                                        .executes(context -> removePoint(context, IntegerArgumentType.getInteger(context, "index")))
                                                        )
                                        )
                        )
        );
    }

    private static int editRaceTrack(CommandContext<CommandSourceStack> context, String name) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(name, context.getSource().getLevel());
        if (raceTrack != null) {
            RaceTrackManager.setEditingRaceTrack(name);
            context.getSource().sendSuccess(() -> Component.literal("Now editing " + name), false);
            return 1;
        }
        context.getSource().sendSuccess(()-> Component.literal("RaceTrack " + name + " does not exist."), false);
        return 0;
    }

    private static int addPoint(CommandContext<CommandSourceStack> context, int kind ,BlockPos startPos, BlockPos endPos) {
        //kind: 0 for start, 1 for end, 2 for checkpoint
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(RaceTrackManager.getEditingRaceTrack(), context.getSource().getLevel());
        if(raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("You are not editing any RaceTrack."), false);
            return 0;
        }

        switch (kind) {
            case 0: // start
                raceTrack.setStartRegion(new CuboidRegion(startPos, endPos, Region.PointType.START));
                context.getSource().sendSuccess(() -> Component.literal("Set start region for " + raceTrack.getName()), true);
                break;
            case 1: // end
                raceTrack.setEndRegion(new CuboidRegion(startPos, endPos, Region.PointType.END));
                context.getSource().sendSuccess(() -> Component.literal("Set end region for " + raceTrack.getName()), true);
                break;
            case 2: // checkpoint
                if (raceTrack.addCheckpointAt(raceTrack.getCheckpoints().size(), new CuboidRegion(startPos, endPos, Region.PointType.CHECKPOINT))) {
                    context.getSource().sendSuccess(() -> Component.literal("Added checkpoint to " + raceTrack.getName()), true);
                } else {
                    context.getSource().sendSuccess(() -> Component.literal("Failed to add checkpoint."), false);
                }
                return 1;
            default:
                context.getSource().sendSuccess(() -> Component.literal("Invalid kind of point."), false);
                return 0;
        }
        return 1;
    }

    private static int addPointAt(CommandContext<CommandSourceStack> context, BlockPos startPos, BlockPos endPos, int index) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(RaceTrackManager.getEditingRaceTrack(), context.getSource().getLevel());
        if(raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("You are not editing any RaceTrack."), false);
            return 0;
        }

        if (raceTrack.addCheckpointAt(index, new CuboidRegion(startPos, endPos, Region.PointType.CHECKPOINT))) {
            context.getSource().sendSuccess(() -> Component.literal("Added checkpoint to " + raceTrack.getName()), true);
            return 1;
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Failed to add checkpoint."), false);
        }
        return 0;
    }

    private static int removePoint(CommandContext<CommandSourceStack> context, int index) {
        RaceTrack raceTrack = RaceTrackManager.getRaceTrack(RaceTrackManager.getEditingRaceTrack(), context.getSource().getLevel());
        if (raceTrack == null) {
            context.getSource().sendSuccess(() -> Component.literal("You are not editing any RaceTrack."), false);
            return 0;
        }

        if (index >= raceTrack.getCheckpoints().size()) {
            context.getSource().sendSuccess(() -> Component.literal("Invalid checkpoint index."), false);
            return 0;
        }

        if(index < 0){
            raceTrack.removeCheckpointAt(raceTrack.getCheckpoints().size()-1);
            context.getSource().sendSuccess(() -> Component.literal("Removed last checkpoint from " + raceTrack.getName()), true);
            return 1;
        }

        raceTrack.getCheckpoints().remove(index);
        context.getSource().sendSuccess(() -> Component.literal("Removed checkpoint at index " + index + " from " + raceTrack.getName()), true);
        return 1;
    }
}
