package com.uberhelixx.flatlights.data.server;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, FlatLights.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        copy(ModTags.Blocks.FLATBLOCKS, ModTags.Items.FLATBLOCKS);
        getOrCreateBuilder(ModTags.Items.INGOTS_PRISMATIC).add(ModItems.PRISMATIC_INGOT.get());
        getOrCreateBuilder(Tags.Items.DYES_BLACK).add(ModItems.BLACK_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_BLUE).add(ModItems.BLUE_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_BROWN).add(ModItems.BROWN_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_CYAN).add(ModItems.CYAN_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_GRAY).add(ModItems.GRAY_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_GREEN).add(ModItems.GREEN_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_LIGHT_BLUE).add(ModItems.LIGHT_BLUE_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_LIGHT_GRAY).add(ModItems.LIGHT_GRAY_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_LIME).add(ModItems.LIME_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_MAGENTA).add(ModItems.MAGENTA_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_ORANGE).add(ModItems.ORANGE_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_PINK).add(ModItems.PINK_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_PURPLE).add(ModItems.PURPLE_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_RED).add(ModItems.RED_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_WHITE).add(ModItems.WHITE_REUSABLE_DYE.get());
        getOrCreateBuilder(Tags.Items.DYES_YELLOW).add(ModItems.YELLOW_REUSABLE_DYE.get());
    }
}
