package com.qwaecd.speeduuuuuuup;

import com.mojang.logging.LogUtils;
import com.qwaecd.speeduuuuuuup.command.RaceTrackCommands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SpeedUuuuuuup.MODID)
public class SpeedUuuuuuup
{
    public static final String MODID = "speed_uuuuuuup";
    private static final Logger LOGGER = LogUtils.getLogger();


    public SpeedUuuuuuup(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

//        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
}
