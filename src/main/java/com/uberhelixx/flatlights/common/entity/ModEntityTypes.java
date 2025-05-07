package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FlatLights.MODID);
    
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
    
    public static final RegistryObject<EntityType<BombEntity>> BOMB_PROJECTILE = ENTITY_TYPES.register("bomb_projectile",
            () -> EntityType.Builder.of((EntityType.EntityFactory<BombEntity>) BombEntity::new,
                            MobCategory.MISC).sized(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "bomb_projectile").toString()));
    
    public static final RegistryObject<EntityType<PortableBlackHoleProjectileEntity>> PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY = ENTITY_TYPES.register("portable_black_hole_projectile_entity",
            () -> EntityType.Builder.of((EntityType.EntityFactory<PortableBlackHoleProjectileEntity>) PortableBlackHoleProjectileEntity::new,
                            MobCategory.MISC).sized(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "portable_black_hole_projectile_entity").toString()));
    
    public static final RegistryObject<EntityType<PortableBlackHoleEntity>> PORTABLE_BLACK_HOLE_ENTITY = ENTITY_TYPES.register("portable_black_hole_entity",
            () -> EntityType.Builder.of((EntityType.EntityFactory<PortableBlackHoleEntity>) PortableBlackHoleEntity::new,
                            MobCategory.MISC).sized(1F, 1F)
                    .build(new ResourceLocation(FlatLights.MODID, "portable_black_hole_entity").toString()));
    
    public static final RegistryObject<EntityType<GravityLiftProjectileEntity>> GRAVITY_LIFT_PROJECTILE_ENTITY = ENTITY_TYPES.register("gravity_lift_projectile_entity",
            () -> EntityType.Builder.of((EntityType.EntityFactory<GravityLiftProjectileEntity>) GravityLiftProjectileEntity::new,
                            MobCategory.MISC).sized(0.1F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "gravity_lift_projectile_entity").toString()));
    
    //defines gravity lift hitbox height
    public static final float GRAV_LIFT_HEIGHT = 8F;
    public static final RegistryObject<EntityType<GravityLiftEntity>> GRAVITY_LIFT_ENTITY = ENTITY_TYPES.register("gravity_lift_entity",
            () -> EntityType.Builder.of((EntityType.EntityFactory<GravityLiftEntity>) GravityLiftEntity::new,
                            MobCategory.MISC).sized(0.25F, GRAV_LIFT_HEIGHT)
                    .build(new ResourceLocation(FlatLights.MODID, "portable_black_hole_entity").toString()));
    
    public static final RegistryObject<EntityType<ChairEntity>> CHAIR_ENTITY = ENTITY_TYPES.register("chair_entity",
            () -> EntityType.Builder.of((EntityType.EntityFactory<ChairEntity>) ChairEntity::new,
                            MobCategory.MISC).sized(0.5F, 0.5F)
                    .build(new ResourceLocation(FlatLights.MODID, "chair_entity").toString()));
    
    public static final RegistryObject<EntityType<Mk2ProjectileEntity>> MK2_PROJECTILE_ENTITY = ENTITY_TYPES.register("mk2_projectile_entity",
            () -> EntityType.Builder.of((EntityType.EntityFactory<Mk2ProjectileEntity>) Mk2ProjectileEntity::new,
                            MobCategory.MISC).sized(1.0F, 1.0F)
                    .build(new ResourceLocation(FlatLights.MODID, "mk2_projectile_entity").toString()));
}
