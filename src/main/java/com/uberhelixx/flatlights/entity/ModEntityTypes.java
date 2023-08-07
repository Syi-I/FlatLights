package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
            () -> EntityType.Builder.create((EntityType.IFactory<VoidProjectileEntity>) VoidProjectileEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).build("void_projectile"));
}
