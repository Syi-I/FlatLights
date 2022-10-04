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

    //helper function
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    //helper function
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    //light level, change the number to whatever light level value from 0-15
    public static ToIntFunction<BlockState> lightLevel = BlockState -> 15;

    //white flat light block, just copy-paste with each new color and change the names
    /*public static final RegistryObject<Block> FLATLIGHT_WHITE_BLOCK = registerBlock("flatlight_white_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));*/

    public static final RegistryObject<Block> FLATLIGHT_BLACK_BLOCK = registerBlock("flatlight_black_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_BLUE_BLOCK = registerBlock("flatlight_blue_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_BROWN_BLOCK = registerBlock("flatlight_brown_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_CYAN_BLOCK = registerBlock("flatlight_cyan_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_GRAY_BLOCK = registerBlock("flatlight_gray_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_GREEN_BLOCK = registerBlock("flatlight_green_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_BLUE_BLOCK = registerBlock("flatlight_light_blue_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_LIGHT_GRAY_BLOCK = registerBlock("flatlight_light_gray_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_LIME_BLOCK = registerBlock("flatlight_lime_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_MAGENTA_BLOCK = registerBlock("flatlight_magenta_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_ORANGE_BLOCK = registerBlock("flatlight_orange_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_PINK_BLOCK = registerBlock("flatlight_pink_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_PURPLE_BLOCK = registerBlock("flatlight_purple_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_RED_BLOCK = registerBlock("flatlight_red_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_WHITE_BLOCK = registerBlock("flatlight_white_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FLATLIGHT_YELLOW_BLOCK = registerBlock("flatlight_yellow_block",
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS)
                    .hardnessAndResistance(0.4f, 30f)
                    .setLightLevel(lightLevel)
                    .sound(SoundType.GLASS)));
}
