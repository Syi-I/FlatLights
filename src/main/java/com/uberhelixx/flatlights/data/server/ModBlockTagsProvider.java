package com.uberhelixx.flatlights.data.server;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, FlatLights.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.BLACK_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.BLUE_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.BROWN_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.CYAN_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.GRAY_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.GREEN_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.LIGHT_BLUE_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.LIGHT_GRAY_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.LIME_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.MAGENTA_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.ORANGE_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.PINK_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.PURPLE_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.RED_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.WHITE_FLATBLOCK.get());
        getOrCreateBuilder(ModTags.Blocks.FLATBLOCKS).add(ModBlocks.YELLOW_FLATBLOCK.get());
    }
}
