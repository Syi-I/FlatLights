package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.startup.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FlatLights.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Blocks.FLATBLOCKS).add(
                ModBlocks.BLACK_FLATBLOCK.get(),
                ModBlocks.BLUE_FLATBLOCK.get(),
                ModBlocks.BROWN_FLATBLOCK.get(),
                ModBlocks.CYAN_FLATBLOCK.get(),
                ModBlocks.GRAY_FLATBLOCK.get(),
                ModBlocks.GREEN_FLATBLOCK.get(),
                ModBlocks.LIGHT_BLUE_FLATBLOCK.get(),
                ModBlocks.LIGHT_GRAY_FLATBLOCK.get(),
                ModBlocks.LIME_FLATBLOCK.get(),
                ModBlocks.MAGENTA_FLATBLOCK.get(),
                ModBlocks.ORANGE_FLATBLOCK.get(),
                ModBlocks.PINK_FLATBLOCK.get(),
                ModBlocks.PURPLE_FLATBLOCK.get(),
                ModBlocks.RED_FLATBLOCK.get(),
                ModBlocks.WHITE_FLATBLOCK.get(),
                ModBlocks.YELLOW_FLATBLOCK.get(),
                ModBlocks.SALMON_FLATBLOCK.get(),
                ModBlocks.GOLD_FLATBLOCK.get(),
                ModBlocks.SANDY_YELLOW_FLATBLOCK.get(),
                ModBlocks.PALE_YELLOW_FLATBLOCK.get(),
                ModBlocks.SPRING_GREEN_FLATBLOCK.get(),
                ModBlocks.PASTEL_GREEN_FLATBLOCK.get(),
                ModBlocks.TEAL_FLATBLOCK.get(),
                ModBlocks.CYAN_BLUE_FLATBLOCK.get(),
                ModBlocks.CERULEAN_FLATBLOCK.get(),
                ModBlocks.SAPPHIRE_FLATBLOCK.get(),
                ModBlocks.NAVY_BLUE_FLATBLOCK.get(),
                ModBlocks.INDIGO_FLATBLOCK.get(),
                ModBlocks.DARK_PURPLE_FLATBLOCK.get(),
                ModBlocks.RED_PURPLE_FLATBLOCK.get(),
                ModBlocks.DARK_PINK_FLATBLOCK.get(),
                ModBlocks.ROSY_PINK_FLATBLOCK.get(),
                ModBlocks.BLACK_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.BLUE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.BROWN_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.CYAN_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.GRAY_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.GREEN_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.LIGHT_BLUE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.LIGHT_GRAY_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.LIME_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.MAGENTA_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.ORANGE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.PINK_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.PURPLE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.RED_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.WHITE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.YELLOW_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.SALMON_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.GOLD_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.SANDY_YELLOW_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.PALE_YELLOW_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.SPRING_GREEN_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.PASTEL_GREEN_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.TEAL_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.CYAN_BLUE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.CERULEAN_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.SAPPHIRE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.NAVY_BLUE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.INDIGO_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.DARK_PURPLE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.RED_PURPLE_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.DARK_PINK_FLATBLOCK_BLACKOUT.get(),
                ModBlocks.ROSY_PINK_FLATBLOCK_BLACKOUT.get());
    }
}
