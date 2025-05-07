package com.uberhelixx.flatlights.common.blockentity;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FlatLights.MODID);
    
    public static final RegistryObject<BlockEntityType<PlatingMachineBE>> PLATING_MACHINE_BE =
            BLOCK_ENTITIES.register("plating_machine_be", () ->
                    BlockEntityType.Builder.of(PlatingMachineBE::new,
                            ModBlocks.PLATING_MACHINE.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<LightStorageBE>> LIGHT_STORAGE_BE =
            BLOCK_ENTITIES.register("light_storage_be", () ->
                    BlockEntityType.Builder.of(LightStorageBE::new,
                            ModBlocks.LIGHT_STORAGE.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<SpectralizerBE>> SPECTRALIZER_BE =
            BLOCK_ENTITIES.register("spectralizer_be", () ->
                    BlockEntityType.Builder.of(SpectralizerBE::new,
                            ModBlocks.SPECTRALIZER.get()).build(null));
    
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
