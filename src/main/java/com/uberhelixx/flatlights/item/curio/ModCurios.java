package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItemGroup;
import com.uberhelixx.flatlights.item.curio.dragonsfinal.DragonsFinalCube;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCurios {
    public static final DeferredRegister<Item> CURIOS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        CURIOS.register(eventBus);
    }

    public static final RegistryObject<Item> DRAGONS_FINAL_CUBE = CURIOS.register("dragons_final_cube",
            () -> new DragonsFinalCube(new Item.Properties().group(ModItemGroup.FLATLIGHTS).maxStackSize(1)));
}
