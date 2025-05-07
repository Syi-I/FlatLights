package com.uberhelixx.flatlights.common.block;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.blackout.*;
import com.uberhelixx.flatlights.common.block.entity.LightStorageBlock;
import com.uberhelixx.flatlights.common.block.entity.PlatingMachineBlock;
import com.uberhelixx.flatlights.common.block.entity.SpectralizerBlock;
import com.uberhelixx.flatlights.common.block.light.*;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.util.ColorPairUtils;
import com.uberhelixx.flatlights.util.lib.LibBlockNames;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MODID);
    public static final DeferredRegister<Block> NOGEN_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MODID);
    
    //register each block to the registry, adds block item automatically with each block
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    
    //function to make a block item from each registered block
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    //register each block to the registry, adds block item automatically with each block
    private static <T extends Block> RegistryObject<T> registerNogenBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = NOGEN_BLOCKS.register(name, block);
        registerNogenItem(name, toReturn);
        return toReturn;
    }
    
    //function to make a block item from each registered block
    private static <T extends Block> RegistryObject<Item> registerNogenItem(String name, RegistryObject<T> block) {
        return ModItems.NOGEN_BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        NOGEN_BLOCKS.register(eventBus);
    }
    
    public static final ToIntFunction<BlockState> BRIGHT_LIGHT_LEVEL = BlockState -> 15;
    public static final ToIntFunction<BlockState> BLACKOUT_LIGHT_LEVEL = BlockState -> 7;
    
    // FLAT BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_FLATBLOCK = makeFlatblock(DyeColor.BLACK);
    public static final RegistryObject<Block> BLUE_FLATBLOCK = makeFlatblock(DyeColor.BLUE);
    public static final RegistryObject<Block> BROWN_FLATBLOCK = makeFlatblock(DyeColor.BROWN);
    public static final RegistryObject<Block> CYAN_FLATBLOCK = makeFlatblock(DyeColor.CYAN);
    public static final RegistryObject<Block> GRAY_FLATBLOCK = makeFlatblock(DyeColor.GRAY);
    public static final RegistryObject<Block> GREEN_FLATBLOCK = makeFlatblock(DyeColor.GREEN);
    public static final RegistryObject<Block> LIGHT_BLUE_FLATBLOCK = makeFlatblock(DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Block> LIGHT_GRAY_FLATBLOCK = makeFlatblock(DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Block> LIME_FLATBLOCK = makeFlatblock(DyeColor.LIME);
    public static final RegistryObject<Block> MAGENTA_FLATBLOCK = makeFlatblock(DyeColor.MAGENTA);
    public static final RegistryObject<Block> ORANGE_FLATBLOCK = makeFlatblock(DyeColor.ORANGE);
    public static final RegistryObject<Block> PINK_FLATBLOCK = makeFlatblock(DyeColor.PINK);
    public static final RegistryObject<Block> PURPLE_FLATBLOCK = makeFlatblock(DyeColor.PURPLE);
    public static final RegistryObject<Block> RED_FLATBLOCK = makeFlatblock(DyeColor.RED);
    public static final RegistryObject<Block> WHITE_FLATBLOCK = makeFlatblock(DyeColor.WHITE);
    public static final RegistryObject<Block> YELLOW_FLATBLOCK = makeFlatblock(DyeColor.YELLOW);
    
    // SHIFTED FLAT BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_FLATBLOCK = makeFlatblockShifted(DyeColor.BLACK);
    public static final RegistryObject<Block> GOLD_FLATBLOCK = makeFlatblockShifted(DyeColor.BLUE);
    public static final RegistryObject<Block> SANDY_YELLOW_FLATBLOCK = makeFlatblockShifted(DyeColor.BROWN);
    public static final RegistryObject<Block> PALE_YELLOW_FLATBLOCK = makeFlatblockShifted(DyeColor.CYAN);
    public static final RegistryObject<Block> SPRING_GREEN_FLATBLOCK = makeFlatblockShifted(DyeColor.GRAY);
    public static final RegistryObject<Block> PASTEL_GREEN_FLATBLOCK = makeFlatblockShifted(DyeColor.GREEN);
    public static final RegistryObject<Block> TEAL_FLATBLOCK = makeFlatblockShifted(DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Block> CYAN_BLUE_FLATBLOCK = makeFlatblockShifted(DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Block> CERULEAN_FLATBLOCK = makeFlatblockShifted(DyeColor.LIME);
    public static final RegistryObject<Block> SAPPHIRE_FLATBLOCK = makeFlatblockShifted(DyeColor.MAGENTA);
    public static final RegistryObject<Block> NAVY_BLUE_FLATBLOCK = makeFlatblockShifted(DyeColor.ORANGE);
    public static final RegistryObject<Block> INDIGO_FLATBLOCK = makeFlatblockShifted(DyeColor.PINK);
    public static final RegistryObject<Block> DARK_PURPLE_FLATBLOCK = makeFlatblockShifted(DyeColor.PURPLE);
    public static final RegistryObject<Block> RED_PURPLE_FLATBLOCK = makeFlatblockShifted(DyeColor.RED);
    public static final RegistryObject<Block> DARK_PINK_FLATBLOCK = makeFlatblockShifted(DyeColor.WHITE);
    public static final RegistryObject<Block> ROSY_PINK_FLATBLOCK = makeFlatblockShifted(DyeColor.YELLOW);
    
    // BLACKOUT FLAT BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.BLACK);
    public static final RegistryObject<Block> BLUE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.BLUE);
    public static final RegistryObject<Block> BROWN_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.BROWN);
    public static final RegistryObject<Block> CYAN_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.CYAN);
    public static final RegistryObject<Block> GRAY_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.GRAY);
    public static final RegistryObject<Block> GREEN_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.GREEN);
    public static final RegistryObject<Block> LIGHT_BLUE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Block> LIGHT_GRAY_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Block> LIME_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.LIME);
    public static final RegistryObject<Block> MAGENTA_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.MAGENTA);
    public static final RegistryObject<Block> ORANGE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.ORANGE);
    public static final RegistryObject<Block> PINK_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.PINK);
    public static final RegistryObject<Block> PURPLE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.PURPLE);
    public static final RegistryObject<Block> RED_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.RED);
    public static final RegistryObject<Block> WHITE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.WHITE);
    public static final RegistryObject<Block> YELLOW_FLATBLOCK_BLACKOUT = makeBlackoutFlatblock(DyeColor.YELLOW);
    
    // BLACKOUT SHIFTED FLAT BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.BLACK);
    public static final RegistryObject<Block> GOLD_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.BLUE);
    public static final RegistryObject<Block> SANDY_YELLOW_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.BROWN);
    public static final RegistryObject<Block> PALE_YELLOW_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.CYAN);
    public static final RegistryObject<Block> SPRING_GREEN_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.GRAY);
    public static final RegistryObject<Block> PASTEL_GREEN_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.GREEN);
    public static final RegistryObject<Block> TEAL_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Block> CYAN_BLUE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Block> CERULEAN_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.LIME);
    public static final RegistryObject<Block> SAPPHIRE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.MAGENTA);
    public static final RegistryObject<Block> NAVY_BLUE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.ORANGE);
    public static final RegistryObject<Block> INDIGO_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.PINK);
    public static final RegistryObject<Block> DARK_PURPLE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.PURPLE);
    public static final RegistryObject<Block> RED_PURPLE_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.RED);
    public static final RegistryObject<Block> DARK_PINK_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.WHITE);
    public static final RegistryObject<Block> ROSY_PINK_FLATBLOCK_BLACKOUT = makeBlackoutFlatblockShifted(DyeColor.YELLOW);
    
    // HEX BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_HEXBLOCK = makePlateBlock(DyeColor.BLACK, "hexblock", null);
    public static final RegistryObject<Block> BLUE_HEXBLOCK = makePlateBlock(DyeColor.BLUE, "hexblock", null);
    public static final RegistryObject<Block> BROWN_HEXBLOCK = makePlateBlock(DyeColor.BROWN, "hexblock", null);
    public static final RegistryObject<Block> CYAN_HEXBLOCK = makePlateBlock(DyeColor.CYAN, "hexblock", null);
    public static final RegistryObject<Block> GRAY_HEXBLOCK = makePlateBlock(DyeColor.GRAY, "hexblock", null);
    public static final RegistryObject<Block> GREEN_HEXBLOCK = makePlateBlock(DyeColor.GREEN, "hexblock", null);
    public static final RegistryObject<Block> LIGHT_BLUE_HEXBLOCK = makePlateBlock(DyeColor.LIGHT_BLUE, "hexblock", null);
    public static final RegistryObject<Block> LIGHT_GRAY_HEXBLOCK = makePlateBlock(DyeColor.LIGHT_GRAY, "hexblock", null);
    public static final RegistryObject<Block> LIME_HEXBLOCK = makePlateBlock(DyeColor.LIME, "hexblock", null);
    public static final RegistryObject<Block> MAGENTA_HEXBLOCK = makePlateBlock(DyeColor.MAGENTA, "hexblock", null);
    public static final RegistryObject<Block> ORANGE_HEXBLOCK = makePlateBlock(DyeColor.ORANGE, "hexblock", null);
    public static final RegistryObject<Block> PINK_HEXBLOCK = makePlateBlock(DyeColor.PINK, "hexblock", null);
    public static final RegistryObject<Block> PURPLE_HEXBLOCK = makePlateBlock(DyeColor.PURPLE, "hexblock", null);
    public static final RegistryObject<Block> RED_HEXBLOCK = makePlateBlock(DyeColor.RED, "hexblock", null);
    public static final RegistryObject<Block> WHITE_HEXBLOCK = makePlateBlock(DyeColor.WHITE, "hexblock", null);
    public static final RegistryObject<Block> YELLOW_HEXBLOCK = makePlateBlock(DyeColor.YELLOW, "hexblock", null);
    
    // SHIFTED HEX BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_HEXBLOCK = makePlateBlockShifted(DyeColor.BLACK, "hexblock", null);
    public static final RegistryObject<Block> GOLD_HEXBLOCK = makePlateBlockShifted(DyeColor.BLUE, "hexblock", null);
    public static final RegistryObject<Block> SANDY_YELLOW_HEXBLOCK = makePlateBlockShifted(DyeColor.BROWN, "hexblock", null);
    public static final RegistryObject<Block> PALE_YELLOW_HEXBLOCK = makePlateBlockShifted(DyeColor.CYAN, "hexblock", null);
    public static final RegistryObject<Block> SPRING_GREEN_HEXBLOCK = makePlateBlockShifted(DyeColor.GRAY, "hexblock", null);
    public static final RegistryObject<Block> PASTEL_GREEN_HEXBLOCK = makePlateBlockShifted(DyeColor.GREEN, "hexblock", null);
    public static final RegistryObject<Block> TEAL_HEXBLOCK = makePlateBlockShifted(DyeColor.LIGHT_BLUE, "hexblock", null);
    public static final RegistryObject<Block> CYAN_BLUE_HEXBLOCK = makePlateBlockShifted(DyeColor.LIGHT_GRAY, "hexblock", null);
    public static final RegistryObject<Block> CERULEAN_HEXBLOCK = makePlateBlockShifted(DyeColor.LIME, "hexblock", null);
    public static final RegistryObject<Block> SAPPHIRE_HEXBLOCK = makePlateBlockShifted(DyeColor.MAGENTA, "hexblock", null);
    public static final RegistryObject<Block> NAVY_BLUE_HEXBLOCK = makePlateBlockShifted(DyeColor.ORANGE, "hexblock", null);
    public static final RegistryObject<Block> INDIGO_HEXBLOCK = makePlateBlockShifted(DyeColor.PINK, "hexblock", null);
    public static final RegistryObject<Block> DARK_PURPLE_HEXBLOCK = makePlateBlockShifted(DyeColor.PURPLE, "hexblock", null);
    public static final RegistryObject<Block> RED_PURPLE_HEXBLOCK = makePlateBlockShifted(DyeColor.RED, "hexblock", null);
    public static final RegistryObject<Block> DARK_PINK_HEXBLOCK = makePlateBlockShifted(DyeColor.WHITE, "hexblock", null);
    public static final RegistryObject<Block> ROSY_PINK_HEXBLOCK = makePlateBlockShifted(DyeColor.YELLOW, "hexblock", null);
    
    // LARGE HEXBLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_LARGE_HEXBLOCK = makePlateBlock(DyeColor.BLACK, "hexblock", "large");
    public static final RegistryObject<Block> BLUE_LARGE_HEXBLOCK = makePlateBlock(DyeColor.BLUE, "hexblock", "large");
    public static final RegistryObject<Block> BROWN_LARGE_HEXBLOCK = makePlateBlock(DyeColor.BROWN, "hexblock", "large");
    public static final RegistryObject<Block> CYAN_LARGE_HEXBLOCK = makePlateBlock(DyeColor.CYAN, "hexblock", "large");
    public static final RegistryObject<Block> GRAY_LARGE_HEXBLOCK = makePlateBlock(DyeColor.GRAY, "hexblock", "large");
    public static final RegistryObject<Block> GREEN_LARGE_HEXBLOCK = makePlateBlock(DyeColor.GREEN, "hexblock", "large");
    public static final RegistryObject<Block> LIGHT_BLUE_LARGE_HEXBLOCK = makePlateBlock(DyeColor.LIGHT_BLUE, "hexblock", "large");
    public static final RegistryObject<Block> LIGHT_GRAY_LARGE_HEXBLOCK = makePlateBlock(DyeColor.LIGHT_GRAY, "hexblock", "large");
    public static final RegistryObject<Block> LIME_LARGE_HEXBLOCK = makePlateBlock(DyeColor.LIME, "hexblock", "large");
    public static final RegistryObject<Block> MAGENTA_LARGE_HEXBLOCK = makePlateBlock(DyeColor.MAGENTA, "hexblock", "large");
    public static final RegistryObject<Block> ORANGE_LARGE_HEXBLOCK = makePlateBlock(DyeColor.ORANGE, "hexblock", "large");
    public static final RegistryObject<Block> PINK_LARGE_HEXBLOCK = makePlateBlock(DyeColor.PINK, "hexblock", "large");
    public static final RegistryObject<Block> PURPLE_LARGE_HEXBLOCK = makePlateBlock(DyeColor.PURPLE, "hexblock", "large");
    public static final RegistryObject<Block> RED_LARGE_HEXBLOCK = makePlateBlock(DyeColor.RED, "hexblock", "large");
    public static final RegistryObject<Block> WHITE_LARGE_HEXBLOCK = makePlateBlock(DyeColor.WHITE, "hexblock", "large");
    public static final RegistryObject<Block> YELLOW_LARGE_HEXBLOCK = makePlateBlock(DyeColor.YELLOW, "hexblock", "large");
    
    // SHIFTED LARGE HEX BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.BLACK, "hexblock", "large");
    public static final RegistryObject<Block> GOLD_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.BLUE, "hexblock", "large");
    public static final RegistryObject<Block> SANDY_YELLOW_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.BROWN, "hexblock", "large");
    public static final RegistryObject<Block> PALE_YELLOW_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.CYAN, "hexblock", "large");
    public static final RegistryObject<Block> SPRING_GREEN_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.GRAY, "hexblock", "large");
    public static final RegistryObject<Block> PASTEL_GREEN_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.GREEN, "hexblock", "large");
    public static final RegistryObject<Block> TEAL_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.LIGHT_BLUE, "hexblock", "large");
    public static final RegistryObject<Block> CYAN_BLUE_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.LIGHT_GRAY, "hexblock", "large");
    public static final RegistryObject<Block> CERULEAN_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.LIME, "hexblock", "large");
    public static final RegistryObject<Block> SAPPHIRE_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.MAGENTA, "hexblock", "large");
    public static final RegistryObject<Block> NAVY_BLUE_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.ORANGE, "hexblock", "large");
    public static final RegistryObject<Block> INDIGO_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.PINK, "hexblock", "large");
    public static final RegistryObject<Block> DARK_PURPLE_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.PURPLE, "hexblock", "large");
    public static final RegistryObject<Block> RED_PURPLE_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.RED, "hexblock", "large");
    public static final RegistryObject<Block> DARK_PINK_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.WHITE, "hexblock", "large");
    public static final RegistryObject<Block> ROSY_PINK_LARGE_HEXBLOCK = makePlateBlockShifted(DyeColor.YELLOW, "hexblock", "large");
    
    // TILES ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_TILES = makePlateBlock(DyeColor.BLACK, "tiles", null);
    public static final RegistryObject<Block> BLUE_TILES = makePlateBlock(DyeColor.BLUE, "tiles", null);
    public static final RegistryObject<Block> BROWN_TILES = makePlateBlock(DyeColor.BROWN, "tiles", null);
    public static final RegistryObject<Block> CYAN_TILES = makePlateBlock(DyeColor.CYAN, "tiles", null);
    public static final RegistryObject<Block> GRAY_TILES = makePlateBlock(DyeColor.GRAY, "tiles", null);
    public static final RegistryObject<Block> GREEN_TILES = makePlateBlock(DyeColor.GREEN, "tiles", null);
    public static final RegistryObject<Block> LIGHT_BLUE_TILES = makePlateBlock(DyeColor.LIGHT_BLUE, "tiles", null);
    public static final RegistryObject<Block> LIGHT_GRAY_TILES = makePlateBlock(DyeColor.LIGHT_GRAY, "tiles", null);
    public static final RegistryObject<Block> LIME_TILES = makePlateBlock(DyeColor.LIME, "tiles", null);
    public static final RegistryObject<Block> MAGENTA_TILES = makePlateBlock(DyeColor.MAGENTA, "tiles", null);
    public static final RegistryObject<Block> ORANGE_TILES = makePlateBlock(DyeColor.ORANGE, "tiles", null);
    public static final RegistryObject<Block> PINK_TILES = makePlateBlock(DyeColor.PINK, "tiles", null);
    public static final RegistryObject<Block> PURPLE_TILES = makePlateBlock(DyeColor.PURPLE, "tiles", null);
    public static final RegistryObject<Block> RED_TILES = makePlateBlock(DyeColor.RED, "tiles", null);
    public static final RegistryObject<Block> WHITE_TILES = makePlateBlock(DyeColor.WHITE, "tiles", null);
    public static final RegistryObject<Block> YELLOW_TILES = makePlateBlock(DyeColor.YELLOW, "tiles", null);
    
    // SHIFTED TILES BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_TILES = makePlateBlockShifted(DyeColor.BLACK, "tiles", null);
    public static final RegistryObject<Block> GOLD_TILES = makePlateBlockShifted(DyeColor.BLUE, "tiles", null);
    public static final RegistryObject<Block> SANDY_YELLOW_TILES = makePlateBlockShifted(DyeColor.BROWN, "tiles", null);
    public static final RegistryObject<Block> PALE_YELLOW_TILES = makePlateBlockShifted(DyeColor.CYAN, "tiles", null);
    public static final RegistryObject<Block> SPRING_GREEN_TILES = makePlateBlockShifted(DyeColor.GRAY, "tiles", null);
    public static final RegistryObject<Block> PASTEL_GREEN_TILES = makePlateBlockShifted(DyeColor.GREEN, "tiles", null);
    public static final RegistryObject<Block> TEAL_TILES = makePlateBlockShifted(DyeColor.LIGHT_BLUE, "tiles", null);
    public static final RegistryObject<Block> CYAN_BLUE_TILES = makePlateBlockShifted(DyeColor.LIGHT_GRAY, "tiles", null);
    public static final RegistryObject<Block> CERULEAN_TILES = makePlateBlockShifted(DyeColor.LIME, "tiles", null);
    public static final RegistryObject<Block> SAPPHIRE_TILES = makePlateBlockShifted(DyeColor.MAGENTA, "tiles", null);
    public static final RegistryObject<Block> NAVY_BLUE_TILES = makePlateBlockShifted(DyeColor.ORANGE, "tiles", null);
    public static final RegistryObject<Block> INDIGO_TILES = makePlateBlockShifted(DyeColor.PINK, "tiles", null);
    public static final RegistryObject<Block> DARK_PURPLE_TILES = makePlateBlockShifted(DyeColor.PURPLE, "tiles", null);
    public static final RegistryObject<Block> RED_PURPLE_TILES = makePlateBlockShifted(DyeColor.RED, "tiles", null);
    public static final RegistryObject<Block> DARK_PINK_TILES = makePlateBlockShifted(DyeColor.WHITE, "tiles", null);
    public static final RegistryObject<Block> ROSY_PINK_TILES = makePlateBlockShifted(DyeColor.YELLOW, "tiles", null);
    
    // LARGE TILES ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_LARGE_TILES = makePlateBlock(DyeColor.BLACK, "tiles", "large");
    public static final RegistryObject<Block> BLUE_LARGE_TILES = makePlateBlock(DyeColor.BLUE, "tiles", "large");
    public static final RegistryObject<Block> BROWN_LARGE_TILES = makePlateBlock(DyeColor.BROWN, "tiles", "large");
    public static final RegistryObject<Block> CYAN_LARGE_TILES = makePlateBlock(DyeColor.CYAN, "tiles", "large");
    public static final RegistryObject<Block> GRAY_LARGE_TILES = makePlateBlock(DyeColor.GRAY, "tiles", "large");
    public static final RegistryObject<Block> GREEN_LARGE_TILES = makePlateBlock(DyeColor.GREEN, "tiles", "large");
    public static final RegistryObject<Block> LIGHT_BLUE_LARGE_TILES = makePlateBlock(DyeColor.LIGHT_BLUE, "tiles", "large");
    public static final RegistryObject<Block> LIGHT_GRAY_LARGE_TILES = makePlateBlock(DyeColor.LIGHT_GRAY, "tiles", "large");
    public static final RegistryObject<Block> LIME_LARGE_TILES = makePlateBlock(DyeColor.LIME, "tiles", "large");
    public static final RegistryObject<Block> MAGENTA_LARGE_TILES = makePlateBlock(DyeColor.MAGENTA, "tiles", "large");
    public static final RegistryObject<Block> ORANGE_LARGE_TILES = makePlateBlock(DyeColor.ORANGE, "tiles", "large");
    public static final RegistryObject<Block> PINK_LARGE_TILES = makePlateBlock(DyeColor.PINK, "tiles", "large");
    public static final RegistryObject<Block> PURPLE_LARGE_TILES = makePlateBlock(DyeColor.PURPLE, "tiles", "large");
    public static final RegistryObject<Block> RED_LARGE_TILES = makePlateBlock(DyeColor.RED, "tiles", "large");
    public static final RegistryObject<Block> WHITE_LARGE_TILES = makePlateBlock(DyeColor.WHITE, "tiles", "large");
    public static final RegistryObject<Block> YELLOW_LARGE_TILES = makePlateBlock(DyeColor.YELLOW, "tiles", "large");
    
    // SHIFTED LARGE TILES BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_LARGE_TILES = makePlateBlockShifted(DyeColor.BLACK, "tiles", "large");
    public static final RegistryObject<Block> GOLD_LARGE_TILES = makePlateBlockShifted(DyeColor.BLUE, "tiles", "large");
    public static final RegistryObject<Block> SANDY_YELLOW_LARGE_TILES = makePlateBlockShifted(DyeColor.BROWN, "tiles", "large");
    public static final RegistryObject<Block> PALE_YELLOW_LARGE_TILES = makePlateBlockShifted(DyeColor.CYAN, "tiles", "large");
    public static final RegistryObject<Block> SPRING_GREEN_LARGE_TILES = makePlateBlockShifted(DyeColor.GRAY, "tiles", "large");
    public static final RegistryObject<Block> PASTEL_GREEN_LARGE_TILES = makePlateBlockShifted(DyeColor.GREEN, "tiles", "large");
    public static final RegistryObject<Block> TEAL_LARGE_TILES = makePlateBlockShifted(DyeColor.LIGHT_BLUE, "tiles", "large");
    public static final RegistryObject<Block> CYAN_BLUE_LARGE_TILES = makePlateBlockShifted(DyeColor.LIGHT_GRAY, "tiles", "large");
    public static final RegistryObject<Block> CERULEAN_LARGE_TILES = makePlateBlockShifted(DyeColor.LIME, "tiles", "large");
    public static final RegistryObject<Block> SAPPHIRE_LARGE_TILES = makePlateBlockShifted(DyeColor.MAGENTA, "tiles", "large");
    public static final RegistryObject<Block> NAVY_BLUE_LARGE_TILES = makePlateBlockShifted(DyeColor.ORANGE, "tiles", "large");
    public static final RegistryObject<Block> INDIGO_LARGE_TILES = makePlateBlockShifted(DyeColor.PINK, "tiles", "large");
    public static final RegistryObject<Block> DARK_PURPLE_LARGE_TILES = makePlateBlockShifted(DyeColor.PURPLE, "tiles", "large");
    public static final RegistryObject<Block> RED_PURPLE_LARGE_TILES = makePlateBlockShifted(DyeColor.RED, "tiles", "large");
    public static final RegistryObject<Block> DARK_PINK_LARGE_TILES = makePlateBlockShifted(DyeColor.WHITE, "tiles", "large");
    public static final RegistryObject<Block> ROSY_PINK_LARGE_TILES = makePlateBlockShifted(DyeColor.YELLOW, "tiles", "large");
    
    // PANEL ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_PANEL = makePanel(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> BLUE_PANEL = makePanel(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> BROWN_PANEL = makePanel(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> CYAN_PANEL = makePanel(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> GRAY_PANEL = makePanel(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> GREEN_PANEL = makePanel(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> LIGHT_BLUE_PANEL = makePanel(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> LIGHT_GRAY_PANEL = makePanel(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> LIME_PANEL = makePanel(DyeColor.LIME, 2);
    public static final RegistryObject<Block> MAGENTA_PANEL = makePanel(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> ORANGE_PANEL = makePanel(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> PINK_PANEL = makePanel(DyeColor.PINK, 2);
    public static final RegistryObject<Block> PURPLE_PANEL = makePanel(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PANEL = makePanel(DyeColor.RED, 2);
    public static final RegistryObject<Block> WHITE_PANEL = makePanel(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> YELLOW_PANEL = makePanel(DyeColor.YELLOW, 2);
    
    // SHIFTED PANELS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_PANEL = makePanelShifted(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> GOLD_PANEL = makePanelShifted(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> SANDY_YELLOW_PANEL = makePanelShifted(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> PALE_YELLOW_PANEL = makePanelShifted(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> SPRING_GREEN_PANEL = makePanelShifted(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> PASTEL_GREEN_PANEL = makePanelShifted(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> TEAL_PANEL = makePanelShifted(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> CYAN_BLUE_PANEL = makePanelShifted(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> CERULEAN_PANEL = makePanelShifted(DyeColor.LIME, 2);
    public static final RegistryObject<Block> SAPPHIRE_PANEL = makePanelShifted(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> NAVY_BLUE_PANEL = makePanelShifted(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> INDIGO_PANEL = makePanelShifted(DyeColor.PINK, 2);
    public static final RegistryObject<Block> DARK_PURPLE_PANEL = makePanelShifted(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PURPLE_PANEL = makePanelShifted(DyeColor.RED, 2);
    public static final RegistryObject<Block> DARK_PINK_PANEL = makePanelShifted(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> ROSY_PINK_PANEL = makePanelShifted(DyeColor.YELLOW, 2);
    
    // BLACKOUT PANEL ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> BLUE_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> BROWN_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> CYAN_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> GRAY_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> GREEN_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> LIGHT_BLUE_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> LIGHT_GRAY_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> LIME_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.LIME, 2);
    public static final RegistryObject<Block> MAGENTA_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> ORANGE_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> PINK_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.PINK, 2);
    public static final RegistryObject<Block> PURPLE_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.RED, 2);
    public static final RegistryObject<Block> WHITE_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> YELLOW_PANEL_BLACKOUT = makeBlackoutPanel(DyeColor.YELLOW, 2);
    
    // BLACKOUT SHIFTED PANELS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> GOLD_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> SANDY_YELLOW_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> PALE_YELLOW_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> SPRING_GREEN_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> PASTEL_GREEN_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> TEAL_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> CYAN_BLUE_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> CERULEAN_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.LIME, 2);
    public static final RegistryObject<Block> SAPPHIRE_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> NAVY_BLUE_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> INDIGO_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.PINK, 2);
    public static final RegistryObject<Block> DARK_PURPLE_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PURPLE_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.RED, 2);
    public static final RegistryObject<Block> DARK_PINK_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> ROSY_PINK_PANEL_BLACKOUT = makeBlackoutPanelShifted(DyeColor.YELLOW, 2);
    
    // PILLAR ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_PILLAR = makePillar(DyeColor.BLACK, 4);
    public static final RegistryObject<Block> BLUE_PILLAR = makePillar(DyeColor.BLUE, 4);
    public static final RegistryObject<Block> BROWN_PILLAR = makePillar(DyeColor.BROWN, 4);
    public static final RegistryObject<Block> CYAN_PILLAR = makePillar(DyeColor.CYAN, 4);
    public static final RegistryObject<Block> GRAY_PILLAR = makePillar(DyeColor.GRAY, 4);
    public static final RegistryObject<Block> GREEN_PILLAR = makePillar(DyeColor.GREEN, 4);
    public static final RegistryObject<Block> LIGHT_BLUE_PILLAR = makePillar(DyeColor.LIGHT_BLUE, 4);
    public static final RegistryObject<Block> LIGHT_GRAY_PILLAR = makePillar(DyeColor.LIGHT_GRAY, 4);
    public static final RegistryObject<Block> LIME_PILLAR = makePillar(DyeColor.LIME, 4);
    public static final RegistryObject<Block> MAGENTA_PILLAR = makePillar(DyeColor.MAGENTA, 4);
    public static final RegistryObject<Block> ORANGE_PILLAR = makePillar(DyeColor.ORANGE, 4);
    public static final RegistryObject<Block> PINK_PILLAR = makePillar(DyeColor.PINK, 4);
    public static final RegistryObject<Block> PURPLE_PILLAR = makePillar(DyeColor.PURPLE, 4);
    public static final RegistryObject<Block> RED_PILLAR = makePillar(DyeColor.RED, 4);
    public static final RegistryObject<Block> WHITE_PILLAR = makePillar(DyeColor.WHITE, 4);
    public static final RegistryObject<Block> YELLOW_PILLAR = makePillar(DyeColor.YELLOW, 4);
    
    // SHIFTED PILLARS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_PILLAR = makePillarShifted(DyeColor.BLACK, 4);
    public static final RegistryObject<Block> GOLD_PILLAR = makePillarShifted(DyeColor.BLUE, 4);
    public static final RegistryObject<Block> SANDY_YELLOW_PILLAR = makePillarShifted(DyeColor.BROWN, 4);
    public static final RegistryObject<Block> PALE_YELLOW_PILLAR = makePillarShifted(DyeColor.CYAN, 4);
    public static final RegistryObject<Block> SPRING_GREEN_PILLAR = makePillarShifted(DyeColor.GRAY, 4);
    public static final RegistryObject<Block> PASTEL_GREEN_PILLAR = makePillarShifted(DyeColor.GREEN, 4);
    public static final RegistryObject<Block> TEAL_PILLAR = makePillarShifted(DyeColor.LIGHT_BLUE, 4);
    public static final RegistryObject<Block> CYAN_BLUE_PILLAR = makePillarShifted(DyeColor.LIGHT_GRAY, 4);
    public static final RegistryObject<Block> CERULEAN_PILLAR = makePillarShifted(DyeColor.LIME, 4);
    public static final RegistryObject<Block> SAPPHIRE_PILLAR = makePillarShifted(DyeColor.MAGENTA, 4);
    public static final RegistryObject<Block> NAVY_BLUE_PILLAR = makePillarShifted(DyeColor.ORANGE, 4);
    public static final RegistryObject<Block> INDIGO_PILLAR = makePillarShifted(DyeColor.PINK, 4);
    public static final RegistryObject<Block> DARK_PURPLE_PILLAR = makePillarShifted(DyeColor.PURPLE, 4);
    public static final RegistryObject<Block> RED_PURPLE_PILLAR = makePillarShifted(DyeColor.RED, 4);
    public static final RegistryObject<Block> DARK_PINK_PILLAR = makePillarShifted(DyeColor.WHITE, 4);
    public static final RegistryObject<Block> ROSY_PINK_PILLAR = makePillarShifted(DyeColor.YELLOW, 4);
    
    // BLACKOUT PILLAR ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.BLACK, 4);
    public static final RegistryObject<Block> BLUE_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.BLUE, 4);
    public static final RegistryObject<Block> BROWN_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.BROWN, 4);
    public static final RegistryObject<Block> CYAN_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.CYAN, 4);
    public static final RegistryObject<Block> GRAY_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.GRAY, 4);
    public static final RegistryObject<Block> GREEN_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.GREEN, 4);
    public static final RegistryObject<Block> LIGHT_BLUE_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.LIGHT_BLUE, 4);
    public static final RegistryObject<Block> LIGHT_GRAY_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.LIGHT_GRAY, 4);
    public static final RegistryObject<Block> LIME_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.LIME, 4);
    public static final RegistryObject<Block> MAGENTA_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.MAGENTA, 4);
    public static final RegistryObject<Block> ORANGE_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.ORANGE, 4);
    public static final RegistryObject<Block> PINK_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.PINK, 4);
    public static final RegistryObject<Block> PURPLE_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.PURPLE, 4);
    public static final RegistryObject<Block> RED_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.RED, 4);
    public static final RegistryObject<Block> WHITE_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.WHITE, 4);
    public static final RegistryObject<Block> YELLOW_PILLAR_BLACKOUT = makeBlackoutPillar(DyeColor.YELLOW, 4);
    
    // BLACKOUT SHIFTED PILLARS ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.BLACK, 4);
    public static final RegistryObject<Block> GOLD_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.BLUE, 4);
    public static final RegistryObject<Block> SANDY_YELLOW_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.BROWN, 4);
    public static final RegistryObject<Block> PALE_YELLOW_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.CYAN, 4);
    public static final RegistryObject<Block> SPRING_GREEN_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.GRAY, 4);
    public static final RegistryObject<Block> PASTEL_GREEN_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.GREEN, 4);
    public static final RegistryObject<Block> TEAL_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.LIGHT_BLUE, 4);
    public static final RegistryObject<Block> CYAN_BLUE_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.LIGHT_GRAY, 4);
    public static final RegistryObject<Block> CERULEAN_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.LIME, 4);
    public static final RegistryObject<Block> SAPPHIRE_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.MAGENTA, 4);
    public static final RegistryObject<Block> NAVY_BLUE_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.ORANGE, 4);
    public static final RegistryObject<Block> INDIGO_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.PINK, 4);
    public static final RegistryObject<Block> DARK_PURPLE_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.PURPLE, 4);
    public static final RegistryObject<Block> RED_PURPLE_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.RED, 4);
    public static final RegistryObject<Block> DARK_PINK_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.WHITE, 4);
    public static final RegistryObject<Block> ROSY_PINK_PILLAR_BLACKOUT = makeBlackoutPillarShifted(DyeColor.YELLOW, 4);
    
    // HORIZONTAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_HORIZONTAL_EDGE = makeEdgeH(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> BLUE_HORIZONTAL_EDGE = makeEdgeH(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> BROWN_HORIZONTAL_EDGE = makeEdgeH(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> CYAN_HORIZONTAL_EDGE = makeEdgeH(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> GRAY_HORIZONTAL_EDGE = makeEdgeH(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> GREEN_HORIZONTAL_EDGE = makeEdgeH(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> LIGHT_BLUE_HORIZONTAL_EDGE = makeEdgeH(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> LIGHT_GRAY_HORIZONTAL_EDGE = makeEdgeH(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> LIME_HORIZONTAL_EDGE = makeEdgeH(DyeColor.LIME, 2);
    public static final RegistryObject<Block> MAGENTA_HORIZONTAL_EDGE = makeEdgeH(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> ORANGE_HORIZONTAL_EDGE = makeEdgeH(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> PINK_HORIZONTAL_EDGE = makeEdgeH(DyeColor.PINK, 2);
    public static final RegistryObject<Block> PURPLE_HORIZONTAL_EDGE = makeEdgeH(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_HORIZONTAL_EDGE = makeEdgeH(DyeColor.RED, 2);
    public static final RegistryObject<Block> WHITE_HORIZONTAL_EDGE = makeEdgeH(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> YELLOW_HORIZONTAL_EDGE = makeEdgeH(DyeColor.YELLOW, 2);
    
    // SHIFTED HORIZONTAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> GOLD_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> SANDY_YELLOW_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> PALE_YELLOW_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> SPRING_GREEN_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> PASTEL_GREEN_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> TEAL_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> CYAN_BLUE_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> CERULEAN_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.LIME, 2);
    public static final RegistryObject<Block> SAPPHIRE_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> NAVY_BLUE_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> INDIGO_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.PINK, 2);
    public static final RegistryObject<Block> DARK_PURPLE_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PURPLE_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.RED, 2);
    public static final RegistryObject<Block> DARK_PINK_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> ROSY_PINK_HORIZONTAL_EDGE = makeEdgeHShifted(DyeColor.YELLOW, 2);
    
    // BLACKOUT HORIZONTAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> BLUE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> BROWN_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> CYAN_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> GRAY_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> GREEN_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> LIGHT_BLUE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> LIGHT_GRAY_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> LIME_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.LIME, 2);
    public static final RegistryObject<Block> MAGENTA_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> ORANGE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> PINK_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.PINK, 2);
    public static final RegistryObject<Block> PURPLE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.RED, 2);
    public static final RegistryObject<Block> WHITE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> YELLOW_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeH(DyeColor.YELLOW, 2);
    
    // BLACKOUT SHIFTED HORIZONTAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> GOLD_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> SANDY_YELLOW_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> PALE_YELLOW_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> SPRING_GREEN_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> PASTEL_GREEN_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> TEAL_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> CYAN_BLUE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> CERULEAN_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.LIME, 2);
    public static final RegistryObject<Block> SAPPHIRE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> NAVY_BLUE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> INDIGO_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.PINK, 2);
    public static final RegistryObject<Block> DARK_PURPLE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PURPLE_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.RED, 2);
    public static final RegistryObject<Block> DARK_PINK_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> ROSY_PINK_HORIZONTAL_EDGE_BLACKOUT = makeBlackoutEdgeHShifted(DyeColor.YELLOW, 2);
    
    // VERTICAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_VERTICAL_EDGE = makeEdgeV(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> BLUE_VERTICAL_EDGE = makeEdgeV(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> BROWN_VERTICAL_EDGE = makeEdgeV(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> CYAN_VERTICAL_EDGE = makeEdgeV(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> GRAY_VERTICAL_EDGE = makeEdgeV(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> GREEN_VERTICAL_EDGE = makeEdgeV(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> LIGHT_BLUE_VERTICAL_EDGE = makeEdgeV(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> LIGHT_GRAY_VERTICAL_EDGE = makeEdgeV(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> LIME_VERTICAL_EDGE = makeEdgeV(DyeColor.LIME, 2);
    public static final RegistryObject<Block> MAGENTA_VERTICAL_EDGE = makeEdgeV(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> ORANGE_VERTICAL_EDGE = makeEdgeV(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> PINK_VERTICAL_EDGE = makeEdgeV(DyeColor.PINK, 2);
    public static final RegistryObject<Block> PURPLE_VERTICAL_EDGE = makeEdgeV(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_VERTICAL_EDGE = makeEdgeV(DyeColor.RED, 2);
    public static final RegistryObject<Block> WHITE_VERTICAL_EDGE = makeEdgeV(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> YELLOW_VERTICAL_EDGE = makeEdgeV(DyeColor.YELLOW, 2);
    
    // SHIFTED VERTICAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> GOLD_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> SANDY_YELLOW_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> PALE_YELLOW_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> SPRING_GREEN_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> PASTEL_GREEN_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> TEAL_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> CYAN_BLUE_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> CERULEAN_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.LIME, 2);
    public static final RegistryObject<Block> SAPPHIRE_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> NAVY_BLUE_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> INDIGO_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.PINK, 2);
    public static final RegistryObject<Block> DARK_PURPLE_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PURPLE_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.RED, 2);
    public static final RegistryObject<Block> DARK_PINK_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> ROSY_PINK_VERTICAL_EDGE = makeEdgeVShifted(DyeColor.YELLOW, 2);
    
    // BLACKOUT VERTICAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> BLUE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> BROWN_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> CYAN_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> GRAY_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> GREEN_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> LIGHT_BLUE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> LIGHT_GRAY_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> LIME_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.LIME, 2);
    public static final RegistryObject<Block> MAGENTA_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> ORANGE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> PINK_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.PINK, 2);
    public static final RegistryObject<Block> PURPLE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.RED, 2);
    public static final RegistryObject<Block> WHITE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> YELLOW_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeV(DyeColor.YELLOW, 2);
    
    // BLACKOUT SHIFTED VERTICAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> SALMON_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.BLACK, 2);
    public static final RegistryObject<Block> GOLD_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.BLUE, 2);
    public static final RegistryObject<Block> SANDY_YELLOW_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.BROWN, 2);
    public static final RegistryObject<Block> PALE_YELLOW_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.CYAN, 2);
    public static final RegistryObject<Block> SPRING_GREEN_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.GRAY, 2);
    public static final RegistryObject<Block> PASTEL_GREEN_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.GREEN, 2);
    public static final RegistryObject<Block> TEAL_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.LIGHT_BLUE, 2);
    public static final RegistryObject<Block> CYAN_BLUE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.LIGHT_GRAY, 2);
    public static final RegistryObject<Block> CERULEAN_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.LIME, 2);
    public static final RegistryObject<Block> SAPPHIRE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.MAGENTA, 2);
    public static final RegistryObject<Block> NAVY_BLUE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.ORANGE, 2);
    public static final RegistryObject<Block> INDIGO_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.PINK, 2);
    public static final RegistryObject<Block> DARK_PURPLE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.PURPLE, 2);
    public static final RegistryObject<Block> RED_PURPLE_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.RED, 2);
    public static final RegistryObject<Block> DARK_PINK_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.WHITE, 2);
    public static final RegistryObject<Block> ROSY_PINK_VERTICAL_EDGE_BLACKOUT = makeBlackoutEdgeVShifted(DyeColor.YELLOW, 2);
    
    // GLASS ##############################################################################################################################
    public static final RegistryObject<Block> GLASS_HEXBLOCK = registerBlock("glass" + LibBlockNames.HEXBLOCK_SUFFIX, WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_LARGE_HEXBLOCK = registerBlock("glass" + LibBlockNames.LARGE_SUFFIX + LibBlockNames.HEXBLOCK_SUFFIX, WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_TILES = registerBlock("glass" + LibBlockNames.TILE_SUFFIX, WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_LARGE_TILES = registerBlock("glass" + LibBlockNames.LARGE_SUFFIX + LibBlockNames.TILE_SUFFIX, WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_FLATBLOCK = registerBlock("glass" + LibBlockNames.FLATBLOCK_SUFFIX, WireGlassBlock::new);
    
    // EXTRA ##############################################################################################################################
    public static final RegistryObject<Block> PRISMATIC_BLOCK = registerBlock("prismatic_block", () -> new FlatBlock(BRIGHT_LIGHT_LEVEL));
    public static final RegistryObject<Block> PRISMATIC_BLOCK_BLACKOUT = registerBlock("prismatic_block_blackout", () -> new BlackoutFlatBlock(BLACKOUT_LIGHT_LEVEL));
    public static final RegistryObject<Block> MOTIVATIONAL_CHAIR = registerNogenBlock("motivational_chair", MotivationalChairBlock::new);
    public static final RegistryObject<Block> LIGHT_STORAGE = registerNogenBlock("light_storage", LightStorageBlock::new);
    public static final RegistryObject<Block> PLATING_MACHINE = registerNogenBlock("plating_machine", PlatingMachineBlock::new);
    public static final RegistryObject<Block> SPECTRALIZER = registerNogenBlock("spectralizer", SpectralizerBlock::new);
    
    
    // FUNCTIONS ##########################################################################################################################
    private static RegistryObject<Block> makeFlatblock(DyeColor dyeColor) {
        return registerBlock(dyeColor.getName() + LibBlockNames.FLATBLOCK_SUFFIX, () -> new FlatBlock(BRIGHT_LIGHT_LEVEL));
    }
    
    private static RegistryObject<Block> makeFlatblockShifted(DyeColor dyeColor) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.FLATBLOCK_SUFFIX, () -> new FlatBlock(BRIGHT_LIGHT_LEVEL));
    }
    
    private static RegistryObject<Block> makeBlackoutFlatblock(DyeColor dyeColor) {
        return registerBlock(dyeColor.getName() + LibBlockNames.FLATBLOCK_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutFlatBlock(BLACKOUT_LIGHT_LEVEL));
    }
    
    private static RegistryObject<Block> makeBlackoutFlatblockShifted(DyeColor dyeColor) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.FLATBLOCK_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutFlatBlock(BLACKOUT_LIGHT_LEVEL));
    }
    
    private static RegistryObject<Block> makePlateBlock(DyeColor dyeColor, String type, @Nullable String size) {
        String dyeName = dyeColor.getName();
        if(size != null && size.equals("large")) {
            if (type.equals("hexblock")) {
                return registerBlock(dyeName + LibBlockNames.LARGE_SUFFIX + LibBlockNames.HEXBLOCK_SUFFIX, PlateBlock::new);
            }
            if (type.equals("tiles")) {
                return registerBlock(dyeName + LibBlockNames.LARGE_SUFFIX + LibBlockNames.TILE_SUFFIX, PlateBlock::new);
            }
        }
        else {
            if (type.equals("hexblock")) {
                return registerBlock(dyeName + LibBlockNames.HEXBLOCK_SUFFIX, PlateBlock::new);
            }
            if (type.equals("tiles")) {
                return registerBlock(dyeName + LibBlockNames.TILE_SUFFIX, PlateBlock::new);
            }
        }
        FlatLights.LOGGER.error("[ModBlocks/makePlateBlock] Incorrect block registration.");
        return null;
    }
    
    private static RegistryObject<Block> makePlateBlockShifted(DyeColor dyeColor, String type, @Nullable String size) {
        String dyeName = ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase());
        if(size != null && size.equals("large")) {
            if (type.equals("hexblock")) {
                return registerBlock(dyeName + LibBlockNames.LARGE_SUFFIX + LibBlockNames.HEXBLOCK_SUFFIX, PlateBlock::new);
            }
            if (type.equals("tiles")) {
                return registerBlock(dyeName + LibBlockNames.LARGE_SUFFIX + LibBlockNames.TILE_SUFFIX, PlateBlock::new);
            }
        }
        else {
            if (type.equals("hexblock")) {
                return registerBlock(dyeName + LibBlockNames.HEXBLOCK_SUFFIX, PlateBlock::new);
            }
            if (type.equals("tiles")) {
                return registerBlock(dyeName + LibBlockNames.TILE_SUFFIX, PlateBlock::new);
            }
        }
        FlatLights.LOGGER.error("[ModBlocks/makePlateBlockShifted] Incorrect block registration.");
        return null;
    }
    
    private static RegistryObject<Block> makePanel(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.PANEL_SUFFIX, () -> new SlabLightBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makePanelShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.PANEL_SUFFIX, () -> new SlabLightBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutPanel(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.PANEL_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutSlabLightBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutPanelShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.PANEL_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutSlabLightBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makePillar(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.PILLAR_SUFFIX, () -> new PillarLightBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makePillarShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.PILLAR_SUFFIX, () -> new PillarLightBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutPillar(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.PILLAR_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutPillarLightBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutPillarShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.PILLAR_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutPillarLightBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeEdgeH(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.HORIZONTAL_EDGE_SUFFIX, () -> new HorizontalEdgeBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeEdgeHShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.HORIZONTAL_EDGE_SUFFIX, () -> new HorizontalEdgeBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutEdgeH(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.HORIZONTAL_EDGE_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutHorizontalEdgeBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutEdgeHShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.HORIZONTAL_EDGE_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutHorizontalEdgeBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeEdgeV(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.VERTICAL_EDGE_SUFFIX, () -> new VerticalEdgeBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeEdgeVShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.VERTICAL_EDGE_SUFFIX, () -> new VerticalEdgeBlock(BRIGHT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutEdgeV(DyeColor dyeColor, int thickness) {
        return registerBlock(dyeColor.getName() + LibBlockNames.VERTICAL_EDGE_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutVerticalEdgeBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
    
    private static RegistryObject<Block> makeBlackoutEdgeVShifted(DyeColor dyeColor, int thickness) {
        return registerBlock(ColorPairUtils.colorPairs.get(dyeColor.getName().toLowerCase()) + LibBlockNames.VERTICAL_EDGE_SUFFIX + LibBlockNames.BLACKOUT_SUFFIX, () -> new BlackoutVerticalEdgeBlock(BLACKOUT_LIGHT_LEVEL, thickness));
    }
}
