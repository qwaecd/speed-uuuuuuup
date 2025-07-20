package com.qwaecd.speeduuuuuuup.init;

import com.mojang.brigadier.CommandDispatcher;
import com.qwaecd.speeduuuuuuup.command.RaceCommands;
import com.qwaecd.speeduuuuuuup.command.RacePointCommands;
import com.qwaecd.speeduuuuuuup.command.RaceTrackCommands;
import com.qwaecd.speeduuuuuuup.command.SpeedUuuuuuupCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.qwaecd.speeduuuuuuup.SpeedUuuuuuup.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InitCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        RaceTrackCommands.register(dispatcher);
        RacePointCommands.register(dispatcher);
        RaceCommands.register(dispatcher);
        SpeedUuuuuuupCommands.register(dispatcher);
    }
}
