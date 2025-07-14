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

        AABB aabb = entity.getSynchedAABB();

        if (aabb != null) {
            LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.LINES),
                aabb.minX - entity.getX(), aabb.minY - entity.getY(), aabb.minZ - entity.getZ(),
                aabb.maxX - entity.getX(), aabb.maxY - entity.getY(), aabb.maxZ - entity.getZ(),
                1.0F, 0.0F, 0.0F, 1.0F);
        }
    }

}
