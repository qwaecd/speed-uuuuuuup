package com.qwaecd.speeduuuuuuup;

import com.mojang.logging.LogUtils;
import com.qwaecd.speeduuuuuuup.init.RaceSystemInitializer;
import com.qwaecd.speeduuuuuuup.init.RegisterEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
        RegisterEntities.ENTITIES.register(modEventBus);
        RaceSystemInitializer.initialize();
//        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
}
