package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, FlatLights.MODID);

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static final RegistryObject<EntityType<VoidProjectileEntity>> VOID_PROJECTILE = ENTITY_TYPES.register("void_projectile",
            () -> EntityType.Builder.create((EntityType.IFactory<VoidProjectileEntity>) VoidProjectileEntity::new,
                            MobCategory.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "void_projectile").toString()));

    public static final RegistryObject<EntityType<BombSwingEntity>> BOMB_SWING_PROJECTILE = ENTITY_TYPES.register("bomb_swing_projectile",
            () -> EntityType.Builder.create((EntityType.IFactory<BombSwingEntity>) BombSwingEntity::new,
                            MobCategory.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "bomb_swing_projectile").toString()));

    public static final RegistryObject<EntityType<VoidSphereEntity>> VOID_SPHERE = ENTITY_TYPES.register("void_sphere",
            () -> EntityType.Builder.create((EntityType.IFactory<VoidSphereEntity>) VoidSphereEntity::new,
                            MobCategory.MISC).size(2F, 2F)
                    .build(new ResourceLocation(FlatLights.MODID, "void_sphere").toString()));

    public static final RegistryObject<EntityType<ChairEntity>> CHAIR_ENTITY = ENTITY_TYPES.register("chair_entity",
            () -> EntityType.Builder.create((EntityType.IFactory<ChairEntity>) ChairEntity::new,
                            MobCategory.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "chair_entity").toString()));

    public static final RegistryObject<EntityType<PortableBlackHoleEntity>> PORTABLE_BLACK_HOLE_ENTITY = ENTITY_TYPES.register("portable_black_hole_entity",
            () -> EntityType.Builder.create((EntityType.IFactory<PortableBlackHoleEntity>) PortableBlackHoleEntity::new,
                            MobCategory.MISC).size(1F, 1F)
                    .build(new ResourceLocation(FlatLights.MODID, "portable_black_hole_entity").toString()));

    public static final RegistryObject<EntityType<PortableBlackHoleProjectileEntity>> PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY = ENTITY_TYPES.register("portable_black_hole_projectile_entity",
            () -> EntityType.Builder.create((EntityType.IFactory<PortableBlackHoleProjectileEntity>) PortableBlackHoleProjectileEntity::new,
                            MobCategory.MISC).size(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "portable_black_hole_projectile_entity").toString()));

    //defines gravity lift hitbox height
    public static final float GRAV_LIFT_HEIGHT = 8F;
    public static final RegistryObject<EntityType<GravityLiftEntity>> GRAVITY_LIFT_ENTITY = ENTITY_TYPES.register("gravity_lift_entity",
            () -> EntityType.Builder.create((EntityType.IFactory<GravityLiftEntity>) GravityLiftEntity::new,
                            MobCategory.MISC).size(0.25F, GRAV_LIFT_HEIGHT)
                    .build(new ResourceLocation(FlatLights.MODID, "gravity_lift_entity").toString()));

    public static final RegistryObject<EntityType<GravityLiftProjectileEntity>> GRAVITY_LIFT_PROJECTILE_ENTITY = ENTITY_TYPES.register("gravity_lift_projectile_entity",
            () -> EntityType.Builder.create((EntityType.IFactory<GravityLiftProjectileEntity>) GravityLiftProjectileEntity::new,
                            MobCategory.MISC).size(0.1F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "gravity_lift_projectile_entity").toString()));
}
