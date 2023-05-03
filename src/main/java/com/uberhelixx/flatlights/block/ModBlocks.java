package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItemGroup;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    //helper function for registering blocks
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    //helper function for registering block as an item, so it exists as a drop and can actually be crafted/used
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    // FLAT BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_FLATBLOCK = registerBlock("black_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> BLUE_FLATBLOCK = registerBlock("blue_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> BROWN_FLATBLOCK = registerBlock("brown_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> CYAN_FLATBLOCK = registerBlock("cyan_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> GRAY_FLATBLOCK = registerBlock("gray_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> GREEN_FLATBLOCK = registerBlock("green_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> LIGHT_BLUE_FLATBLOCK = registerBlock("light_blue_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> LIGHT_GRAY_FLATBLOCK = registerBlock("light_gray_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> LIME_FLATBLOCK = registerBlock("lime_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> MAGENTA_FLATBLOCK = registerBlock("magenta_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> ORANGE_FLATBLOCK = registerBlock("orange_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> PINK_FLATBLOCK = registerBlock("pink_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> PURPLE_FLATBLOCK = registerBlock("purple_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> RED_FLATBLOCK = registerBlock("red_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> WHITE_FLATBLOCK = registerBlock("white_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> YELLOW_FLATBLOCK = registerBlock("yellow_flatblock", FlatBlock::new);

    // HEX BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_HEXBLOCK = registerBlock("black_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> BLUE_HEXBLOCK = registerBlock("blue_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> BROWN_HEXBLOCK = registerBlock("brown_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> CYAN_HEXBLOCK = registerBlock("cyan_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> GRAY_HEXBLOCK = registerBlock("gray_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> GREEN_HEXBLOCK = registerBlock("green_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_BLUE_HEXBLOCK = registerBlock("light_blue_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_GRAY_HEXBLOCK = registerBlock("light_gray_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> LIME_HEXBLOCK = registerBlock("lime_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> MAGENTA_HEXBLOCK = registerBlock("magenta_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> ORANGE_HEXBLOCK = registerBlock("orange_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> PINK_HEXBLOCK = registerBlock("pink_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> PURPLE_HEXBLOCK = registerBlock("purple_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> RED_HEXBLOCK = registerBlock("red_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> WHITE_HEXBLOCK = registerBlock("white_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> YELLOW_HEXBLOCK = registerBlock("yellow_hexblock", PlateBlock::new);

    // LARGE HEXBLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_LARGE_HEXBLOCK = registerBlock("black_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> BLUE_LARGE_HEXBLOCK = registerBlock("blue_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> BROWN_LARGE_HEXBLOCK = registerBlock("brown_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> CYAN_LARGE_HEXBLOCK = registerBlock("cyan_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> GRAY_LARGE_HEXBLOCK = registerBlock("gray_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> GREEN_LARGE_HEXBLOCK = registerBlock("green_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_BLUE_LARGE_HEXBLOCK = registerBlock("light_blue_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_GRAY_LARGE_HEXBLOCK = registerBlock("light_gray_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> LIME_LARGE_HEXBLOCK = registerBlock("lime_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> MAGENTA_LARGE_HEXBLOCK = registerBlock("magenta_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> ORANGE_LARGE_HEXBLOCK = registerBlock("orange_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> PINK_LARGE_HEXBLOCK = registerBlock("pink_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> PURPLE_LARGE_HEXBLOCK = registerBlock("purple_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> RED_LARGE_HEXBLOCK = registerBlock("red_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> WHITE_LARGE_HEXBLOCK = registerBlock("white_large_hexblock", PlateBlock::new);
    public static final RegistryObject<Block> YELLOW_LARGE_HEXBLOCK = registerBlock("yellow_large_hexblock", PlateBlock::new);

    // TILES ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_TILES = registerBlock("black_tiles", PlateBlock::new);
    public static final RegistryObject<Block> BLUE_TILES = registerBlock("blue_tiles", PlateBlock::new);
    public static final RegistryObject<Block> BROWN_TILES = registerBlock("brown_tiles", PlateBlock::new);
    public static final RegistryObject<Block> CYAN_TILES = registerBlock("cyan_tiles", PlateBlock::new);
    public static final RegistryObject<Block> GRAY_TILES = registerBlock("gray_tiles", PlateBlock::new);
    public static final RegistryObject<Block> GREEN_TILES = registerBlock("green_tiles", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_BLUE_TILES = registerBlock("light_blue_tiles", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_GRAY_TILES = registerBlock("light_gray_tiles", PlateBlock::new);
    public static final RegistryObject<Block> LIME_TILES = registerBlock("lime_tiles", PlateBlock::new);
    public static final RegistryObject<Block> MAGENTA_TILES = registerBlock("magenta_tiles", PlateBlock::new);
    public static final RegistryObject<Block> ORANGE_TILES = registerBlock("orange_tiles", PlateBlock::new);
    public static final RegistryObject<Block> PINK_TILES = registerBlock("pink_tiles", PlateBlock::new);
    public static final RegistryObject<Block> PURPLE_TILES = registerBlock("purple_tiles", PlateBlock::new);
    public static final RegistryObject<Block> RED_TILES = registerBlock("red_tiles", PlateBlock::new);
    public static final RegistryObject<Block> WHITE_TILES = registerBlock("white_tiles", PlateBlock::new);
    public static final RegistryObject<Block> YELLOW_TILES = registerBlock("yellow_tiles", PlateBlock::new);

    // LARGE TILES ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_LARGE_TILES = registerBlock("black_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> BLUE_LARGE_TILES = registerBlock("blue_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> BROWN_LARGE_TILES = registerBlock("brown_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> CYAN_LARGE_TILES = registerBlock("cyan_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> GRAY_LARGE_TILES = registerBlock("gray_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> GREEN_LARGE_TILES = registerBlock("green_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_BLUE_LARGE_TILES = registerBlock("light_blue_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> LIGHT_GRAY_LARGE_TILES = registerBlock("light_gray_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> LIME_LARGE_TILES = registerBlock("lime_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> MAGENTA_LARGE_TILES = registerBlock("magenta_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> ORANGE_LARGE_TILES = registerBlock("orange_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> PINK_LARGE_TILES = registerBlock("pink_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> PURPLE_LARGE_TILES = registerBlock("purple_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> RED_LARGE_TILES = registerBlock("red_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> WHITE_LARGE_TILES = registerBlock("white_large_tiles", PlateBlock::new);
    public static final RegistryObject<Block> YELLOW_LARGE_TILES = registerBlock("yellow_large_tiles", PlateBlock::new);

    // GLASS ##############################################################################################################################
    public static final RegistryObject<Block> GLASS_HEXBLOCK = registerBlock("glass_hexblock", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_LARGE_HEXBLOCK = registerBlock("glass_large_hexblock", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_TILES = registerBlock("glass_tiles", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_LARGE_TILES = registerBlock("glass_large_tiles", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_FLATBLOCK = registerBlock("glass_flatblock", WireGlassBlock::new);

    // EXTRA ##############################################################################################################################
    public static final RegistryObject<Block> LIME_BRICK = registerBlock("lime_brick", PlateBlock::new);
    public static final RegistryObject<Block> PLATING_MACHINE = registerBlock("plating_machine", PlatingMachineBlock::new);
    public static final RegistryObject<Block> MOB_B_GONE = registerBlock("mob_b_gone", Mob_B_Gone::new);
    public static final RegistryObject<Block> PRISMATIC_BLOCK = registerBlock("prismatic_block", FlatBlock::new);
    public static final RegistryObject<Block> SPECTRUM_ANVIL = registerBlock("spectrum_anvil", SpectrumAnvilBlock::new);

}
