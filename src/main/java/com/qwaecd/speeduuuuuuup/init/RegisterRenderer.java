package com.qwaecd.speeduuuuuuup.init;


import com.qwaecd.speeduuuuuuup.client.RegionMarkerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.qwaecd.speeduuuuuuup.SpeedUuuuuuup.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterRenderer {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(RegisterEntities.REGION_ENTITY.get(), RegionMarkerRenderer::new);
    }
}
