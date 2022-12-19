package com.uberhelixx.flatlights.item;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.tools.PrismaticBlade;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
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
    public static final RegistryObject<Item> PRISMATIC_BLADE = ITEMS.register("prismatic_blade",
            () -> new PrismaticBlade(ModItemTier.PRISMATIC, 5, 1f, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_INGOT = ITEMS.register("prismatic_ingot",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    public static final RegistryObject<Item> PRISMATIC_BOOTS = ITEMS.register("prismatic_boots",
            () -> new ArmorItem(ModArmorMaterial.PRISMATIC, EquipmentSlotType.FEET, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_LEGGINGS = ITEMS.register("prismatic_leggings",
            () -> new ArmorItem(ModArmorMaterial.PRISMATIC, EquipmentSlotType.LEGS, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_CHESTPLATE = ITEMS.register("prismatic_chestplate",
            () -> new ArmorItem(ModArmorMaterial.PRISMATIC, EquipmentSlotType.CHEST, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_HELMET = ITEMS.register("prismatic_helmet",
            () -> new ArmorItem(ModArmorMaterial.PRISMATIC, EquipmentSlotType.HEAD, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    public static final RegistryObject<Item> PRISMA_NUCLEUS = ITEMS.register("prisma_nucleus",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    /*public static final RegistryObject<Item> BIG_BREAD = ITEMS.register("bread_but_high_quality",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));*/
    public static final RegistryObject<Item> BIG_BREAD = ITEMS.register("bread_but_high_quality",
            () -> new BreadButHighQuality(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
}
