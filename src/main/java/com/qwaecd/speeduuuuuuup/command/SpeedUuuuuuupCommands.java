package com.qwaecd.speeduuuuuuup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.qwaecd.speeduuuuuuup.client.ModDebugger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class SpeedUuuuuuupCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("speeduuuuuuup")
                        .then(
                                Commands.literal("debug")
                                        .requires(source -> source.hasPermission(0))
                                        .then(
                                                Commands.literal("show_all")
                                                        .then(
                                                                Commands.argument("boolean", BoolArgumentType.bool())
                                                                        .executes(context -> debugModuleSetShowAll(context, BoolArgumentType.getBool(context, "boolean")))
                                                        )
                                                        .executes(SpeedUuuuuuupCommands::debugModuleShowAll)
                                        )
                        )
        );
    }

    private static int debugModuleShowAll(CommandContext<CommandSourceStack> context) {
        boolean showAllEntities = ModDebugger.showAllEntities;
        context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.modid.debugModuleShowAll", showAllEntities), false);
        return 1;
    }

    private static int debugModuleSetShowAll(CommandContext<CommandSourceStack> context, boolean ifShowAll) {
        ModDebugger.showAllEntities = ifShowAll;
        context.getSource().sendSuccess(() -> Component.translatable("speed_uuuuuuup.command.modid.debugModuleShowAll", ifShowAll), false);
        return 1;
    }
}
