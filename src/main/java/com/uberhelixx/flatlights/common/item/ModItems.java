package com.uberhelixx.flatlights.common.item;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.armor.*;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBlade;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.common.item.tools.PrismaticSword;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MODID);
    public static final DeferredRegister<Item> NOGEN_BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MODID);
    public static final DeferredRegister<Item> TOGGLE_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MODID);
    public static final DeferredRegister<Item> MODEL_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MODID);
    
    //ingot
    public static final RegistryObject<Item> PRISMATIC_INGOT = ITEMS.register("prismatic_ingot",
            () -> new Item(new Item.Properties()));
    
    //armor cores
    public static final RegistryObject<Item> HELMET_CORE = ITEMS.register("helmet_core",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHEST_CORE = ITEMS.register("chest_core",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PANTS_CORE = ITEMS.register("pants_core",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BOOTS_CORE = ITEMS.register("boots_core",
            () -> new Item(new Item.Properties()));
    
    //armor
    public static final RegistryObject<Item> PRISMATIC_HELMET = ITEMS.register("prismatic_helmet",
            () -> new PrismaticHelm(ModArmorMaterial.PRISMATIC, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> PRISMATIC_CHESTPLATE = ITEMS.register("prismatic_chestplate",
            () -> new PrismaticChestplate(ModArmorMaterial.PRISMATIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> PRISMATIC_LEGGINGS = ITEMS.register("prismatic_leggings",
            () -> new PrismaticLeggings(ModArmorMaterial.PRISMATIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> PRISMATIC_BOOTS = ITEMS.register("prismatic_boots",
            () -> new PrismaticBoots(ModArmorMaterial.PRISMATIC, ArmorItem.Type.BOOTS, new Item.Properties()));
    
    //weapons
    public static final RegistryObject<Item> PRISMATIC_SWORD = TOGGLE_ITEMS.register("prismatic_sword",
            () -> new PrismaticSword(ModToolTier.PRISMATIC, -4, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> PRISMATIC_BLADE = MODEL_ITEMS.register("prismatic_blade",
            () -> new PrismaticBlade(ModToolTier.PRISMATIC, 0, -2f, new Item.Properties()));
    
    //misc items
    public static final RegistryObject<Item> PRISMA_NUCLEUS = ITEMS.register("prisma_nucleus",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BIG_BREAD = ITEMS.register("bread_but_high_quality",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(30).saturationMod(36).build())));
    public static final RegistryObject<Item> PORTABLE_BLACKHOLE = ITEMS.register("portable_blackhole",
            () -> new PortableBlackHoleItem(new Item.Properties()));
    public static final RegistryObject<Item> GRAVITY_LIFT = MODEL_ITEMS.register("gravity_lift",
            () -> new GravityLiftItem(new Item.Properties()));
    
    //reusable dyes
    public static final RegistryObject<Item> BLACK_REUSABLE_DYE = ITEMS.register("black_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_REUSABLE_DYE = ITEMS.register("blue_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> BROWN_REUSABLE_DYE = ITEMS.register("brown_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> CYAN_REUSABLE_DYE = ITEMS.register("cyan_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> GRAY_REUSABLE_DYE = ITEMS.register("gray_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_REUSABLE_DYE = ITEMS.register("green_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_BLUE_REUSABLE_DYE = ITEMS.register("light_blue_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_GRAY_REUSABLE_DYE = ITEMS.register("light_gray_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> LIME_REUSABLE_DYE = ITEMS.register("lime_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> MAGENTA_REUSABLE_DYE = ITEMS.register("magenta_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> ORANGE_REUSABLE_DYE = ITEMS.register("orange_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> PINK_REUSABLE_DYE = ITEMS.register("pink_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_REUSABLE_DYE = ITEMS.register("purple_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> RED_REUSABLE_DYE = ITEMS.register("red_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> WHITE_REUSABLE_DYE = ITEMS.register("white_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_REUSABLE_DYE = ITEMS.register("yellow_reusable_dye",
            () -> new ReusableDye(new Item.Properties()));
    public static final RegistryObject<Item> HUE_SHIFTING_VIAL = ITEMS.register("hue_shifting_vial",
            () -> new ReusableDye(new Item.Properties().stacksTo(1)));
    
    public static final RegistryObject<Item> BLACKOUT_POWDER = ITEMS.register("blackout_powder",
            () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> JOGOAT = ITEMS.register("jogoat",
            () -> new Item(new Item.Properties().fireResistant()));
    
    //creative only items
    public static final RegistryObject<Item> GUN_RAT = ITEMS.register("gun_rat",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRISMATIC_BLADEMK2 = MODEL_ITEMS.register("prismatic_blademk2",
            () -> new PrismaticBladeMk2(ModToolTier.PRISMATIC, -5, 1f, new Item.Properties()));
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
        TOGGLE_ITEMS.register(eventBus);
        MODEL_ITEMS.register(eventBus);
        NOGEN_BLOCK_ITEMS.register(eventBus);
    }
}
