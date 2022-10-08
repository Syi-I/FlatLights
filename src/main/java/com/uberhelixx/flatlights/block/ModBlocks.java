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
    public static ToIntFunction<BlockState> PLATE_LIGHT_LEVEL = BlockState -> 7;

    //function that just gets the block properties so I don't have to copy-paste it 16 times since the blocks are all the same but reskinned
    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 10000000f;
    public static AbstractBlock.Properties getFlatBlockProperties() {
        return AbstractBlock.Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(FLAT_LIGHT_LEVEL)
                .sound(SoundType.GLASS);
    }

    public static AbstractBlock.Properties getPlateBlockProperties() {
        return AbstractBlock.Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(PLATE_LIGHT_LEVEL)
                .sound(SoundType.LODESTONE);
    }

    //white flat light block, just copy-paste with each new color and change the names
    //basic example of how blocks work
    /*public static final RegistryObject<Block> FLATLIGHT_WHITE_BLOCK = registerBlock("flatlight_white_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 3600000f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));*/

    // FLAT BLOCKS ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_BLACK_BLOCK = registerBlock("flatlight_black_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_BLUE_BLOCK = registerBlock("flatlight_blue_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_BROWN_BLOCK = registerBlock("flatlight_brown_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_CYAN_BLOCK = registerBlock("flatlight_cyan_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_GRAY_BLOCK = registerBlock("flatlight_gray_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_GREEN_BLOCK = registerBlock("flatlight_green_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_BLOCK = registerBlock("flatlight_light_blue_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_BLOCK = registerBlock("flatlight_light_gray_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_LIME_BLOCK = registerBlock("flatlight_lime_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_BLOCK = registerBlock("flatlight_magenta_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_BLOCK = registerBlock("flatlight_orange_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_PINK_BLOCK = registerBlock("flatlight_pink_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_BLOCK = registerBlock("flatlight_purple_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_RED_BLOCK = registerBlock("flatlight_red_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_WHITE_BLOCK = registerBlock("flatlight_white_block",
            () -> new Block(getFlatBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_BLOCK = registerBlock("flatlight_yellow_block",
            () -> new Block(getFlatBlockProperties()));

    // HEX BLOCKS ##############################################################################################################################

    public static final RegistryObject<Block> FLATLIGHT_BLACK_HEXBLOCK = registerBlock("flatlight_black_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_BLUE_HEXBLOCK = registerBlock("flatlight_blue_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_BROWN_HEXBLOCK = registerBlock("flatlight_brown_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_CYAN_HEXBLOCK = registerBlock("flatlight_cyan_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_GRAY_HEXBLOCK = registerBlock("flatlight_gray_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_GREEN_HEXBLOCK = registerBlock("flatlight_green_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_HEXBLOCK = registerBlock("flatlight_light_blue_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_HEXBLOCK = registerBlock("flatlight_light_gray_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_LIME_HEXBLOCK = registerBlock("flatlight_lime_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_HEXBLOCK = registerBlock("flatlight_magenta_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_HEXBLOCK = registerBlock("flatlight_orange_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_PINK_HEXBLOCK = registerBlock("flatlight_pink_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_HEXBLOCK = registerBlock("flatlight_purple_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_RED_HEXBLOCK = registerBlock("flatlight_red_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_WHITE_HEXBLOCK = registerBlock("flatlight_white_hexblock",
            () -> new Block(getPlateBlockProperties()));

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_HEXBLOCK = registerBlock("flatlight_yellow_hexblock",
            () -> new Block(getPlateBlockProperties()));
}
