package com.uberhelixx.flatlights.tileentity;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }

    public static RegistryObject<TileEntityType<PlatingMachineTile>> PLATING_MACHINE_TILE = TILE_ENTITIES.register("plating_machine_tile",
            () -> TileEntityType.Builder.create(PlatingMachineTile::new, ModBlocks.PLATING_MACHINE.get()).build(null));

    public static RegistryObject<TileEntityType<SpectrumAnvilTile>> SPECTRUM_ANVIL_TILE = TILE_ENTITIES.register("spectrum_anvil_tile",
            () -> TileEntityType.Builder.create(SpectrumAnvilTile::new, ModBlocks.SPECTRUM_ANVIL.get()).build(null));

    public static RegistryObject<TileEntityType<LightStorageTile>> LIGHT_STORAGE_TILE = TILE_ENTITIES.register("light_storage_tile",
            () -> TileEntityType.Builder.create(LightStorageTile::new, ModBlocks.LIGHT_STORAGE.get()).build(null));
}
