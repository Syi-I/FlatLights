package com.uberhelixx.flatlights.painting;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPaintings {
    public static final DeferredRegister<PaintingType> PAINTING_TYPES =
            DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        PAINTING_TYPES.register(eventBus);
    }

    //size is 16 * number of blocks (e.g. this is 2 wide, 1 tall painting so 32 by 16)
    public static final RegistryObject<PaintingType> JAZZ = PAINTING_TYPES.register("jazz", () -> new PaintingType(32, 16));
    public static final RegistryObject<PaintingType> GREATE = PAINTING_TYPES.register("greate", () -> new PaintingType(32, 32));
}
