package com.qwaecd.speeduuuuuuup.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.qwaecd.speeduuuuuuup.entity.RegionMarkerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public abstract class MarkerEntityRenderer<T extends RegionMarkerEntity> extends EntityRenderer<T> {

    protected MarkerEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight){

    }

}
