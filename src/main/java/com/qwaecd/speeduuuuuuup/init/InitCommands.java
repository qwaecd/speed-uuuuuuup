package com.qwaecd.speeduuuuuuup.init;

import com.qwaecd.speeduuuuuuup.command.RacePointCommands;
import com.qwaecd.speeduuuuuuup.command.RaceTrackCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.qwaecd.speeduuuuuuup.SpeedUuuuuuup.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InitCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        RaceTrackCommands.register(event.getDispatcher());
        RacePointCommands.register(event.getDispatcher());
    }
}
