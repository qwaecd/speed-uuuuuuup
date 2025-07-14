package com.qwaecd.speeduuuuuuup.init;


import com.qwaecd.speeduuuuuuup.entity.RegionMarkerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.qwaecd.speeduuuuuuup.SpeedUuuuuuup.MODID;

public class RegisterEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<RegionMarkerEntity>> REGION_ENTITY = register(RegionMarkerEntity::new, "region_marker");

    public static <T extends Entity> RegistryObject<EntityType<T>> register(EntityType.EntityFactory<T> entity, String name) {
        return ENTITIES.register(
                name,
                () -> EntityType.Builder
                        .of(entity, MobCategory.MISC)
                        .sized(0.5F, 0.5F)
                        .noSave()
                        .build(name)
        );
    }

}
