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

    //different DeferredRegisters for the various block types to try and make datagen easier
    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);
    //helper function for registering block as an item, so it exists as a drop and can actually be crafted/used
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }
    //helper function for registering blocks
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static final DeferredRegister<Block> SPECIAL_BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);
    private static <T extends Block>RegistryObject<T> registerSpecialBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = SPECIAL_BLOCKS.register(name, block);
        registerSpecialBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerSpecialBlockItem(String name, RegistryObject<T> block) {
        ModItems.SPECIAL_BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    public static final DeferredRegister<Block> PANELS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);
    private static <T extends Block>RegistryObject<T> registerPanel(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = PANELS.register(name, block);
        registerPanelItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerPanelItem(String name, RegistryObject<T> block) {
        ModItems.PANEL_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    public static final DeferredRegister<Block> FLATBLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);
    private static <T extends Block>RegistryObject<T> registerFlatblock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = FLATBLOCKS.register(name, block);
        registerFlatblockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerFlatblockItem(String name, RegistryObject<T> block) {
        ModItems.FLATBLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    public static final DeferredRegister<Block> PILLARS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);
    private static <T extends Block>RegistryObject<T> registerPillar(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = PILLARS.register(name, block);
        registerPillarItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerPillarItem(String name, RegistryObject<T> block) {
        ModItems.PILLAR_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    public static final DeferredRegister<Block> HORIZONTAL_EDGES
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FlatLights.MOD_ID);
    private static <T extends Block>RegistryObject<T> registerEdgeH(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = HORIZONTAL_EDGES.register(name, block);
        registerEdgeItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerEdgeItem(String name, RegistryObject<T> block) {
        ModItems.EDGE_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.FLATLIGHTS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        SPECIAL_BLOCKS.register(eventBus);
        PANELS.register(eventBus);
        FLATBLOCKS.register(eventBus);
        PILLARS.register(eventBus);
        HORIZONTAL_EDGES.register(eventBus);
    }

    // FLAT BLOCKS ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_FLATBLOCK = registerFlatblock("black_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> BLUE_FLATBLOCK = registerFlatblock("blue_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> BROWN_FLATBLOCK = registerFlatblock("brown_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> CYAN_FLATBLOCK = registerFlatblock("cyan_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> GRAY_FLATBLOCK = registerFlatblock("gray_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> GREEN_FLATBLOCK = registerFlatblock("green_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> LIGHT_BLUE_FLATBLOCK = registerFlatblock("light_blue_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> LIGHT_GRAY_FLATBLOCK = registerFlatblock("light_gray_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> LIME_FLATBLOCK = registerFlatblock("lime_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> MAGENTA_FLATBLOCK = registerFlatblock("magenta_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> ORANGE_FLATBLOCK = registerFlatblock("orange_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> PINK_FLATBLOCK = registerFlatblock("pink_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> PURPLE_FLATBLOCK = registerFlatblock("purple_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> RED_FLATBLOCK = registerFlatblock("red_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> WHITE_FLATBLOCK = registerFlatblock("white_flatblock", FlatBlock::new);
    public static final RegistryObject<Block> YELLOW_FLATBLOCK = registerFlatblock("yellow_flatblock", FlatBlock::new);

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

    // PANEL ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_PANEL = registerPanel("black_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> BLUE_PANEL = registerPanel("blue_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> BROWN_PANEL = registerPanel("brown_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> CYAN_PANEL = registerPanel("cyan_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> GRAY_PANEL = registerPanel("gray_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> GREEN_PANEL = registerPanel("green_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> LIGHT_BLUE_PANEL = registerPanel("light_blue_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> LIGHT_GRAY_PANEL = registerPanel("light_gray_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> LIME_PANEL = registerPanel("lime_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> MAGENTA_PANEL = registerPanel("magenta_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> ORANGE_PANEL = registerPanel("orange_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> PINK_PANEL = registerPanel("pink_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> PURPLE_PANEL = registerPanel("purple_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> RED_PANEL = registerPanel("red_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> WHITE_PANEL = registerPanel("white_panel", () -> new SlabLightBlock(2));
    public static final RegistryObject<Block> YELLOW_PANEL = registerPanel("yellow_panel", () -> new SlabLightBlock(2));

    // PILLAR ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_PILLAR = registerPillar("black_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> BLUE_PILLAR = registerPillar("blue_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> BROWN_PILLAR = registerPillar("brown_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> CYAN_PILLAR = registerPillar("cyan_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> GRAY_PILLAR = registerPillar("gray_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> GREEN_PILLAR = registerPillar("green_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> LIGHT_BLUE_PILLAR = registerPillar("light_blue_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> LIGHT_GRAY_PILLAR = registerPillar("light_gray_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> LIME_PILLAR = registerPillar("lime_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> MAGENTA_PILLAR = registerPillar("magenta_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> ORANGE_PILLAR = registerPillar("orange_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> PINK_PILLAR = registerPillar("pink_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> PURPLE_PILLAR = registerPillar("purple_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> RED_PILLAR = registerPillar("red_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> WHITE_PILLAR = registerPillar("white_pillar", () -> new PillarLightBlock(4));
    public static final RegistryObject<Block> YELLOW_PILLAR = registerPillar("yellow_pillar", () -> new PillarLightBlock(4));

    // HORIZONTAL EDGE ##############################################################################################################################
    public static final RegistryObject<Block> BLACK_HORIZONTAL_EDGE = registerEdgeH("black_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> BLUE_HORIZONTAL_EDGE = registerEdgeH("blue_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> BROWN_HORIZONTAL_EDGE = registerEdgeH("brown_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> CYAN_HORIZONTAL_EDGE = registerEdgeH("cyan_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> GRAY_HORIZONTAL_EDGE = registerEdgeH("gray_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> GREEN_HORIZONTAL_EDGE = registerEdgeH("green_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> LIGHT_BLUE_HORIZONTAL_EDGE = registerEdgeH("light_blue_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> LIGHT_GRAY_HORIZONTAL_EDGE = registerEdgeH("light_gray_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> LIME_HORIZONTAL_EDGE = registerEdgeH("lime_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> MAGENTA_HORIZONTAL_EDGE = registerEdgeH("magenta_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> ORANGE_HORIZONTAL_EDGE = registerEdgeH("orange_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> PINK_HORIZONTAL_EDGE = registerEdgeH("pink_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> PURPLE_HORIZONTAL_EDGE = registerEdgeH("purple_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> RED_HORIZONTAL_EDGE = registerEdgeH("red_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> WHITE_HORIZONTAL_EDGE = registerEdgeH("white_horizontal_edge", () -> new HorizontalEdgeBlock(2));
    public static final RegistryObject<Block> YELLOW_HORIZONTAL_EDGE = registerEdgeH("yellow_horizontal_edge", () -> new HorizontalEdgeBlock(2));

    // GLASS ##############################################################################################################################
    public static final RegistryObject<Block> GLASS_HEXBLOCK = registerBlock("glass_hexblock", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_LARGE_HEXBLOCK = registerBlock("glass_large_hexblock", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_TILES = registerBlock("glass_tiles", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_LARGE_TILES = registerBlock("glass_large_tiles", WireGlassBlock::new);
    public static final RegistryObject<Block> GLASS_FLATBLOCK = registerBlock("glass_flatblock", WireGlassBlock::new);

    // EXTRA ##############################################################################################################################
    public static final RegistryObject<Block> LIME_BRICK = registerBlock("lime_brick", PlateBlock::new);
    public static final RegistryObject<Block> PLATING_MACHINE = registerSpecialBlock("plating_machine", PlatingMachineBlock::new);
    public static final RegistryObject<Block> MOB_B_GONE = registerSpecialBlock("mob_b_gone", Mob_B_Gone::new);
    public static final RegistryObject<Block> PRISMATIC_BLOCK = registerFlatblock("prismatic_block", FlatBlock::new);
    public static final RegistryObject<Block> SPECTRUM_ANVIL = registerSpecialBlock("spectrum_anvil", SpectrumAnvilBlock::new);
    public static final RegistryObject<Block> LIGHT_STORAGE = registerSpecialBlock("light_storage", LightStorageBlock::new);
    public static final RegistryObject<Block> SPECTRALIZER = registerSpecialBlock("spectralizer", SpectralizerBlock::new);
    public static final RegistryObject<Block> MOTIVATIONAL_CHAIR = registerSpecialBlock("motivational_chair", MotivationalChairBlock::new);

}
