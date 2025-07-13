package com.qwaecd.speeduuuuuuup.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.qwaecd.speeduuuuuuup.entity.RegionMarkerEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

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
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        AABB aabb = entity.getRegion().toAABB();
        LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.LINES), aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, 1.0F, 0.0F, 0.0F, 1.0F);
    }

}
