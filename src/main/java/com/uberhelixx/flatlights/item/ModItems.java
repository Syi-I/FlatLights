package com.uberhelixx.flatlights.item;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.armor.*;
import com.uberhelixx.flatlights.item.tools.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    //special items are for items with specific models and stuff (eg from blockbench) so datagen sucks less
    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> SPECIAL_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> SPECIAL_BLOCK_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> PANEL_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> FLATBLOCK_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> PILLAR_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> EDGEH_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);
    public static final DeferredRegister<Item> EDGEV_ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        SPECIAL_ITEMS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
        SPECIAL_BLOCK_ITEMS.register(eventBus);
        PANEL_ITEMS.register(eventBus);
        FLATBLOCK_ITEMS.register(eventBus);
        PILLAR_ITEMS.register(eventBus);
        EDGEH_ITEMS.register(eventBus);
        EDGEV_ITEMS.register(eventBus);
    }

    //adding any additional items, just copy this and change the names e.g. 'POWERED_RAT' and 'powered_rat' to something else
    //item properties can all be added in that Item constructor e.g. 'ItemGroup' determines where in the Creative Menu the item can be found

    //ingot
    public static final RegistryObject<Item> PRISMATIC_INGOT = ITEMS.register("prismatic_ingot",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    //armor cores
    public static final RegistryObject<Item> HELMET_CORE = ITEMS.register("helmet_core",
            () -> new BreadButHighQuality(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> CHEST_CORE = ITEMS.register("chest_core",
            () -> new BreadButHighQuality(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PANTS_CORE = ITEMS.register("pants_core",
            () -> new BreadButHighQuality(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> BOOTS_CORE = ITEMS.register("boots_core",
            () -> new BreadButHighQuality(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    //armor
    public static final RegistryObject<Item> PRISMATIC_BOOTS = ITEMS.register("prismatic_boots",
            () -> new PrismaticBoots(ModArmorMaterial.PRISMATIC, EquipmentSlotType.FEET, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_LEGGINGS = ITEMS.register("prismatic_leggings",
            () -> new PrismaticLeggings(ModArmorMaterial.PRISMATIC, EquipmentSlotType.LEGS, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_CHESTPLATE = ITEMS.register("prismatic_chestplate",
            () -> new PrismaticChestplate(ModArmorMaterial.PRISMATIC, EquipmentSlotType.CHEST, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_HELMET = ITEMS.register("prismatic_helmet",
            () -> new PrismaticHelm(ModArmorMaterial.PRISMATIC, EquipmentSlotType.HEAD, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    //weapons
    public static final RegistryObject<Item> PRISMATIC_SWORD = SPECIAL_ITEMS.register("prismatic_sword",
            () -> new PrismaticSword(ModItemTier.PRISMATIC, -4, -2.4f, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_BLADE = SPECIAL_ITEMS.register("prismatic_blade",
            () -> new PrismaticBlade(ModItemTier.PRISMATIC, 0, -2f, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    //misc items
    public static final RegistryObject<Item> PRISMA_NUCLEUS = ITEMS.register("prisma_nucleus",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> BIG_BREAD = ITEMS.register("bread_but_high_quality",
            () -> new BreadButHighQuality(new Item.Properties().group(ModItemGroup.FLATLIGHTS).food(new Food.Builder().hunger(30).saturation(36).build())));
    public static final RegistryObject<Item> PORTABLE_BLACKHOLE = ITEMS.register("portable_blackhole",
            () -> new PortableBlackHoleItem(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));

    //reusable dyes
    public static final RegistryObject<Item> BLACK_REUSABLE_DYE = ITEMS.register("black_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> BLUE_REUSABLE_DYE = ITEMS.register("blue_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> BROWN_REUSABLE_DYE = ITEMS.register("brown_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> CYAN_REUSABLE_DYE = ITEMS.register("cyan_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> GRAY_REUSABLE_DYE = ITEMS.register("gray_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> GREEN_REUSABLE_DYE = ITEMS.register("green_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> LIGHT_BLUE_REUSABLE_DYE = ITEMS.register("light_blue_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> LIGHT_GRAY_REUSABLE_DYE = ITEMS.register("light_gray_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> LIME_REUSABLE_DYE = ITEMS.register("lime_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> MAGENTA_REUSABLE_DYE = ITEMS.register("magenta_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> ORANGE_REUSABLE_DYE = ITEMS.register("orange_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PINK_REUSABLE_DYE = ITEMS.register("pink_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PURPLE_REUSABLE_DYE = ITEMS.register("purple_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> RED_REUSABLE_DYE = ITEMS.register("red_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> WHITE_REUSABLE_DYE = ITEMS.register("white_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> YELLOW_REUSABLE_DYE = ITEMS.register("yellow_reusable_dye",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> HUE_SHIFTING_VIAL = ITEMS.register("hue_shifting_vial",
            () -> new ReusableDye(new Item.Properties().group(ModItemGroup.FLATLIGHTS).maxStackSize(1)));

    public static final RegistryObject<Item> JOGOAT = ITEMS.register("jogoat",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS).isImmuneToFire()));

    //creative only items
    public static final RegistryObject<Item> GUN_RAT = ITEMS.register("gun_rat",
            () -> new Item(new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    public static final RegistryObject<Item> PRISMATIC_BLADEMK2 = SPECIAL_ITEMS.register("prismatic_blademk2",
            () -> new PrismaticBladeMk2(ModItemTier.PRISMATIC, -5, 1f, new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
}
