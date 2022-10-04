package com.uberhelixx.flatlights.item;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    //adding any additional items, just copy this and change the names e.g. 'POWERED_RAT' and 'powered_rat' to something else
    //item properties can all be added in that Item constructor e.g. 'ItemGroup' determines where in the Creative Menu the item can be found
    public static final RegistryObject<Item> GUN_RAT = ITEMS.register("gun_rat",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
}
