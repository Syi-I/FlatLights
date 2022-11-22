package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItemGroup;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

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

    //light level, change the number to whatever light level value from 0-15
    public static ToIntFunction<BlockState> FLAT_LIGHT_LEVEL = BlockState -> 15;
    public static ToIntFunction<BlockState> PLATE_LIGHT_LEVEL = BlockState -> 10;
    public static ToIntFunction<BlockState> MOB_B_GONE_LIGHT_LEVEL = BlockState -> 7;

    //function that just gets the block properties so I don't have to copy-paste it 16 times since the blocks are all the same but reskinned
    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public static AbstractBlock.Properties getPlatingMachineProperties() {
        return AbstractBlock.Properties.create(Material.IRON)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .sound(SoundType.NETHERITE);
    }

    public static AbstractBlock.Properties getMobBGoneProperties() {
        return AbstractBlock.Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(MOB_B_GONE_LIGHT_LEVEL)
                .sound(SoundType.LANTERN);
    }

    // FLAT BLOCKS ##############################################################################################################################
    
    public static final RegistryObject<Block> FLATLIGHT_BLACK_BLOCK = registerBlock("flatlight_black_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BLUE_BLOCK = registerBlock("flatlight_blue_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BROWN_BLOCK = registerBlock("flatlight_brown_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_CYAN_BLOCK = registerBlock("flatlight_cyan_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GRAY_BLOCK = registerBlock("flatlight_gray_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GREEN_BLOCK = registerBlock("flatlight_green_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_BLOCK = registerBlock("flatlight_light_blue_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_BLOCK = registerBlock("flatlight_light_gray_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIME_BLOCK = registerBlock("flatlight_lime_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_BLOCK = registerBlock("flatlight_magenta_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_BLOCK = registerBlock("flatlight_orange_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PINK_BLOCK = registerBlock("flatlight_pink_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_BLOCK = registerBlock("flatlight_purple_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_RED_BLOCK = registerBlock("flatlight_red_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_WHITE_BLOCK = registerBlock("flatlight_white_block",
            FlatBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_BLOCK = registerBlock("flatlight_yellow_block",
            FlatBlock::new);

    // HEX BLOCKS ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_BLACK_HEXBLOCK = registerBlock("flatlight_black_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BLUE_HEXBLOCK = registerBlock("flatlight_blue_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BROWN_HEXBLOCK = registerBlock("flatlight_brown_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_CYAN_HEXBLOCK = registerBlock("flatlight_cyan_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GRAY_HEXBLOCK = registerBlock("flatlight_gray_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GREEN_HEXBLOCK = registerBlock("flatlight_green_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_HEXBLOCK = registerBlock("flatlight_light_blue_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_HEXBLOCK = registerBlock("flatlight_light_gray_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIME_HEXBLOCK = registerBlock("flatlight_lime_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_HEXBLOCK = registerBlock("flatlight_magenta_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_HEXBLOCK = registerBlock("flatlight_orange_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PINK_HEXBLOCK = registerBlock("flatlight_pink_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_HEXBLOCK = registerBlock("flatlight_purple_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_RED_HEXBLOCK = registerBlock("flatlight_red_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_WHITE_HEXBLOCK = registerBlock("flatlight_white_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_HEXBLOCK = registerBlock("flatlight_yellow_hexblock",
            PlateBlock::new);

    // LARGE HEXBLOCKS ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_BLACK_LARGE_HEXBLOCK = registerBlock("flatlight_black_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BLUE_LARGE_HEXBLOCK = registerBlock("flatlight_blue_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BROWN_LARGE_HEXBLOCK = registerBlock("flatlight_brown_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_CYAN_LARGE_HEXBLOCK = registerBlock("flatlight_cyan_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GRAY_LARGE_HEXBLOCK = registerBlock("flatlight_gray_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GREEN_LARGE_HEXBLOCK = registerBlock("flatlight_green_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_LARGE_HEXBLOCK = registerBlock("flatlight_light_blue_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_LARGE_HEXBLOCK = registerBlock("flatlight_light_gray_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIME_LARGE_HEXBLOCK = registerBlock("flatlight_lime_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_LARGE_HEXBLOCK = registerBlock("flatlight_magenta_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_LARGE_HEXBLOCK = registerBlock("flatlight_orange_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PINK_LARGE_HEXBLOCK = registerBlock("flatlight_pink_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_LARGE_HEXBLOCK = registerBlock("flatlight_purple_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_RED_LARGE_HEXBLOCK = registerBlock("flatlight_red_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_WHITE_LARGE_HEXBLOCK = registerBlock("flatlight_white_large_hexblock",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_LARGE_HEXBLOCK = registerBlock("flatlight_yellow_large_hexblock",
            PlateBlock::new);

    // TILES ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_BLACK_TILES = registerBlock("flatlight_black_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BLUE_TILES = registerBlock("flatlight_blue_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BROWN_TILES = registerBlock("flatlight_brown_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_CYAN_TILES = registerBlock("flatlight_cyan_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GRAY_TILES = registerBlock("flatlight_gray_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GREEN_TILES = registerBlock("flatlight_green_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_TILES = registerBlock("flatlight_light_blue_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_TILES = registerBlock("flatlight_light_gray_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIME_TILES = registerBlock("flatlight_lime_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_TILES = registerBlock("flatlight_magenta_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_TILES = registerBlock("flatlight_orange_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PINK_TILES = registerBlock("flatlight_pink_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_TILES = registerBlock("flatlight_purple_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_RED_TILES = registerBlock("flatlight_red_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_WHITE_TILES = registerBlock("flatlight_white_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_TILES = registerBlock("flatlight_yellow_tiles",
            PlateBlock::new);

    // LARGE TILES ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_BLACK_LARGE_TILES = registerBlock("flatlight_black_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BLUE_LARGE_TILES = registerBlock("flatlight_blue_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_BROWN_LARGE_TILES = registerBlock("flatlight_brown_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_CYAN_LARGE_TILES = registerBlock("flatlight_cyan_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GRAY_LARGE_TILES = registerBlock("flatlight_gray_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_GREEN_LARGE_TILES = registerBlock("flatlight_green_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_LARGE_TILES = registerBlock("flatlight_light_blue_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_LARGE_TILES = registerBlock("flatlight_light_gray_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_LIME_LARGE_TILES = registerBlock("flatlight_lime_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_LARGE_TILES = registerBlock("flatlight_magenta_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_LARGE_TILES = registerBlock("flatlight_orange_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PINK_LARGE_TILES = registerBlock("flatlight_pink_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_LARGE_TILES = registerBlock("flatlight_purple_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_RED_LARGE_TILES = registerBlock("flatlight_red_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_WHITE_LARGE_TILES = registerBlock("flatlight_white_large_tiles",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_LARGE_TILES = registerBlock("flatlight_yellow_large_tiles",
            PlateBlock::new);

    // EXTRA ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_LIME_BRICK = registerBlock("flatlight_lime_brick",
            PlateBlock::new);

    public static final RegistryObject<Block> FLATLIGHT_PLATING_MACHINE = registerBlock("flatlight_plating_machine",
            () -> new FlatlightsPlatingMachine(getPlatingMachineProperties()));

    public static final RegistryObject<Block> FLATLIGHT_MOB_B_GONE = registerBlock("mob_b_gone",
            () -> new Block(getMobBGoneProperties()));

    public static final RegistryObject<Block> PRISMATIC_BLOCK = registerBlock("prismatic_block",
            FlatBlock::new);

}
