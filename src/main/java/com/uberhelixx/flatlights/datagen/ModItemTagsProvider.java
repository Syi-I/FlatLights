package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.startup.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    
    public ModItemTagsProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, FlatLights.MODID, existingFileHelper);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(ModTags.Blocks.FLATBLOCKS, ModTags.Items.FLATBLOCKS);
        this.tag(ModTags.Items.INGOTS_PRISMATIC).add(ModItems.PRISMATIC_INGOT.get());
        
        //forge tag namespace 'forge'
        this.tag(Tags.Items.DYES_BLACK).add(ModItems.BLACK_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_BLUE).add(ModItems.BLUE_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_BROWN).add(ModItems.BROWN_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_CYAN).add(ModItems.CYAN_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_GRAY).add(ModItems.GRAY_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_GREEN).add(ModItems.GREEN_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_LIGHT_BLUE).add(ModItems.LIGHT_BLUE_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_LIGHT_GRAY).add(ModItems.LIGHT_GRAY_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_LIME).add(ModItems.LIME_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_MAGENTA).add(ModItems.MAGENTA_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_ORANGE).add(ModItems.ORANGE_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_PINK).add(ModItems.PINK_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_PURPLE).add(ModItems.PURPLE_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_RED).add(ModItems.RED_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_WHITE).add(ModItems.WHITE_REUSABLE_DYE.get());
        this.tag(Tags.Items.DYES_YELLOW).add(ModItems.YELLOW_REUSABLE_DYE.get());
        
        //common tag namespace 'c'
        this.tag(ModTags.Items.C_DYES_BLACK).add(ModItems.BLACK_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_BLUE).add(ModItems.BLUE_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_BROWN).add(ModItems.BROWN_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_CYAN).add(ModItems.CYAN_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_GRAY).add(ModItems.GRAY_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_GREEN).add(ModItems.GREEN_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_LIGHT_BLUE).add(ModItems.LIGHT_BLUE_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_LIGHT_GRAY).add(ModItems.LIGHT_GRAY_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_LIME).add(ModItems.LIME_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_MAGENTA).add(ModItems.MAGENTA_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_ORANGE).add(ModItems.ORANGE_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_PINK).add(ModItems.PINK_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_PURPLE).add(ModItems.PURPLE_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_RED).add(ModItems.RED_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_WHITE).add(ModItems.WHITE_REUSABLE_DYE.get());
        this.tag(ModTags.Items.C_DYES_YELLOW).add(ModItems.YELLOW_REUSABLE_DYE.get());
    }
}
