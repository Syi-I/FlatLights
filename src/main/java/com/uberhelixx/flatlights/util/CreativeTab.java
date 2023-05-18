package com.uberhelixx.flatlights.util;

import com.google.common.collect.Ordering;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CreativeTab {
    public final List<Item> tabOrder = Arrays.asList(
            //FLATBLOCKS
            ModBlocks.BLACK_FLATBLOCK.get().asItem(),
            ModBlocks.BLUE_FLATBLOCK.get().asItem(),
            ModBlocks.BROWN_FLATBLOCK.get().asItem(),
            ModBlocks.CYAN_FLATBLOCK.get().asItem(),
            ModBlocks.GRAY_FLATBLOCK.get().asItem(),
            ModBlocks.GREEN_FLATBLOCK.get().asItem(),
            ModBlocks.LIGHT_BLUE_FLATBLOCK.get().asItem(),
            ModBlocks.LIGHT_GRAY_FLATBLOCK.get().asItem(),
            ModBlocks.LIME_FLATBLOCK.get().asItem(),
            ModBlocks.MAGENTA_FLATBLOCK.get().asItem(),
            ModBlocks.ORANGE_FLATBLOCK.get().asItem(),
            ModBlocks.PINK_FLATBLOCK.get().asItem(),
            ModBlocks.PURPLE_FLATBLOCK.get().asItem(),
            ModBlocks.RED_FLATBLOCK.get().asItem(),
            ModBlocks.WHITE_FLATBLOCK.get().asItem(),
            ModBlocks.YELLOW_FLATBLOCK.get().asItem(),
            //HEXBLOCKS
            ModBlocks.BLACK_HEXBLOCK.get().asItem(),
            ModBlocks.BLUE_HEXBLOCK.get().asItem(),
            ModBlocks.BROWN_HEXBLOCK.get().asItem(),
            ModBlocks.CYAN_HEXBLOCK.get().asItem(),
            ModBlocks.GRAY_HEXBLOCK.get().asItem(),
            ModBlocks.GREEN_HEXBLOCK.get().asItem(),
            ModBlocks.LIGHT_BLUE_HEXBLOCK.get().asItem(),
            ModBlocks.LIGHT_GRAY_HEXBLOCK.get().asItem(),
            ModBlocks.LIME_HEXBLOCK.get().asItem(),
            ModBlocks.MAGENTA_HEXBLOCK.get().asItem(),
            ModBlocks.ORANGE_HEXBLOCK.get().asItem(),
            ModBlocks.PINK_HEXBLOCK.get().asItem(),
            ModBlocks.PURPLE_HEXBLOCK.get().asItem(),
            ModBlocks.RED_HEXBLOCK.get().asItem(),
            ModBlocks.WHITE_HEXBLOCK.get().asItem(),
            ModBlocks.YELLOW_HEXBLOCK.get().asItem(),
            //LARGE HEXBLOCKS
            ModBlocks.BLACK_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.BLUE_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.BROWN_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.CYAN_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.GRAY_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.GREEN_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.LIGHT_BLUE_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.LIGHT_GRAY_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.LIME_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.MAGENTA_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.ORANGE_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.PINK_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.PURPLE_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.RED_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.WHITE_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.YELLOW_LARGE_HEXBLOCK.get().asItem(),
            //TILES
            ModBlocks.BLACK_TILES.get().asItem(),
            ModBlocks.BLUE_TILES.get().asItem(),
            ModBlocks.BROWN_TILES.get().asItem(),
            ModBlocks.CYAN_TILES.get().asItem(),
            ModBlocks.GRAY_TILES.get().asItem(),
            ModBlocks.GREEN_TILES.get().asItem(),
            ModBlocks.LIGHT_BLUE_TILES.get().asItem(),
            ModBlocks.LIGHT_GRAY_TILES.get().asItem(),
            ModBlocks.LIME_TILES.get().asItem(),
            ModBlocks.MAGENTA_TILES.get().asItem(),
            ModBlocks.ORANGE_TILES.get().asItem(),
            ModBlocks.PINK_TILES.get().asItem(),
            ModBlocks.PURPLE_TILES.get().asItem(),
            ModBlocks.RED_TILES.get().asItem(),
            ModBlocks.WHITE_TILES.get().asItem(),
            ModBlocks.YELLOW_TILES.get().asItem(),
            //LARGE TILES
            ModBlocks.BLACK_LARGE_TILES.get().asItem(),
            ModBlocks.BLUE_LARGE_TILES.get().asItem(),
            ModBlocks.BROWN_LARGE_TILES.get().asItem(),
            ModBlocks.CYAN_LARGE_TILES.get().asItem(),
            ModBlocks.GRAY_LARGE_TILES.get().asItem(),
            ModBlocks.GREEN_LARGE_TILES.get().asItem(),
            ModBlocks.LIGHT_BLUE_LARGE_TILES.get().asItem(),
            ModBlocks.LIGHT_GRAY_LARGE_TILES.get().asItem(),
            ModBlocks.LIME_LARGE_TILES.get().asItem(),
            ModBlocks.MAGENTA_LARGE_TILES.get().asItem(),
            ModBlocks.ORANGE_LARGE_TILES.get().asItem(),
            ModBlocks.PINK_LARGE_TILES.get().asItem(),
            ModBlocks.PURPLE_LARGE_TILES.get().asItem(),
            ModBlocks.RED_LARGE_TILES.get().asItem(),
            ModBlocks.WHITE_LARGE_TILES.get().asItem(),
            ModBlocks.YELLOW_LARGE_TILES.get().asItem(),
            //GLASS
            ModBlocks.GLASS_FLATBLOCK.get().asItem(),
            ModBlocks.GLASS_HEXBLOCK.get().asItem(),
            ModBlocks.GLASS_LARGE_HEXBLOCK.get().asItem(),
            ModBlocks.GLASS_TILES.get().asItem(),
            ModBlocks.GLASS_LARGE_TILES.get().asItem(),
            //OTHER
            ModBlocks.PRISMATIC_BLOCK.get().asItem(),
            ModBlocks.LIME_BRICK.get().asItem(),
            //ITEMS
            ModItems.BIG_BREAD.get(),
            ModItems.GUN_RAT.get(),
            ModItems.PRISMATIC_BLADE.get(),
            ModItems.PRISMATIC_BLADEMK2.get(),
            ModItems.PRISMA_NUCLEUS.get(),
            ModItems.PRISMATIC_BOOTS.get(),
            ModItems.PRISMATIC_LEGGINGS.get(),
            ModItems.PRISMATIC_CHESTPLATE.get(),
            ModItems.PRISMATIC_HELMET.get(),
            //indev
            ModBlocks.MOB_B_GONE.get().asItem(),
            ModBlocks.PLATING_MACHINE.get().asItem(),
            ModBlocks.SPECTRUM_ANVIL.get().asItem()
    );
}
