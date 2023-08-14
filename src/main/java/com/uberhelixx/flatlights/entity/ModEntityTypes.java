package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static final RegistryObject<EntityType<VoidProjectileEntity>> VOID_PROJECTILE = ENTITY_TYPES.register("void_projectile",
            () -> EntityType.Builder.create((EntityType.IFactory<VoidProjectileEntity>) VoidProjectileEntity::new,
                    EntityClassification.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MOD_ID, "void_projectile").toString()));

    public static final RegistryObject<EntityType<BombSwingEntity>> BOMB_SWING_PROJECTILE = ENTITY_TYPES.register("bomb_swing_projectile",
            () -> EntityType.Builder.create((EntityType.IFactory<BombSwingEntity>) BombSwingEntity::new,
                    EntityClassification.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MOD_ID, "bomb_swing_projectile").toString()));

    public static final RegistryObject<EntityType<VoidSphereEntity>> VOID_SPHERE = ENTITY_TYPES.register("void_sphere",
            () -> EntityType.Builder.create((EntityType.IFactory<VoidSphereEntity>) VoidSphereEntity::new,
                    EntityClassification.MISC).size(2F, 2F)
                    .build(new ResourceLocation(FlatLights.MOD_ID, "void_sphere").toString()));

    public static final RegistryObject<EntityType<ChairEntity>> CHAIR_ENTITY = ENTITY_TYPES.register("chair_entity",
            () -> EntityType.Builder.create((EntityType.IFactory<ChairEntity>) ChairEntity::new,
                            EntityClassification.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MOD_ID, "chair_entity").toString()));
}