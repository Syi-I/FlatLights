package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.startup.registry.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    private static final List<ItemLike> SMELTABLE = List.of(ModBlocks.PRISMATIC_BLOCK.get(), ModBlocks.PRISMATIC_BLOCK_BLACKOUT.get());
    private static final List<Item> MOD_DYES = List.of(
        ModItems.BLACK_REUSABLE_DYE.get(),
        ModItems.BLUE_REUSABLE_DYE.get(),
        ModItems.BROWN_REUSABLE_DYE.get(),
        ModItems.CYAN_REUSABLE_DYE.get(),
        ModItems.GRAY_REUSABLE_DYE.get(),
        ModItems.GREEN_REUSABLE_DYE.get(),
        ModItems.LIGHT_BLUE_REUSABLE_DYE.get(),
        ModItems.LIGHT_GRAY_REUSABLE_DYE.get(),
        ModItems.LIME_REUSABLE_DYE.get(),
        ModItems.MAGENTA_REUSABLE_DYE.get(),
        ModItems.ORANGE_REUSABLE_DYE.get(),
        ModItems.PINK_REUSABLE_DYE.get(),
        ModItems.PURPLE_REUSABLE_DYE.get(),
        ModItems.RED_REUSABLE_DYE.get(),
        ModItems.WHITE_REUSABLE_DYE.get(),
        ModItems.YELLOW_REUSABLE_DYE.get()
    );
    
    private static final List<TagKey<Item>> COLORS = List.of(
        Tags.Items.DYES_BLACK,
        Tags.Items.DYES_BLUE,
        Tags.Items.DYES_BROWN,
        Tags.Items.DYES_CYAN,
        Tags.Items.DYES_GRAY,
        Tags.Items.DYES_GREEN,
        Tags.Items.DYES_LIGHT_BLUE,
        Tags.Items.DYES_LIGHT_GRAY,
        Tags.Items.DYES_LIME,
        Tags.Items.DYES_MAGENTA,
        Tags.Items.DYES_ORANGE,
        Tags.Items.DYES_PINK,
        Tags.Items.DYES_PURPLE,
        Tags.Items.DYES_RED,
        Tags.Items.DYES_WHITE,
        Tags.Items.DYES_YELLOW
    );
    
    private static final List<Item> WOOLS = List.of(
        Items.BLACK_WOOL,
        Items.BLUE_WOOL,
        Items.BROWN_WOOL,
        Items.CYAN_WOOL,
        Items.GRAY_WOOL,
        Items.GREEN_WOOL,
        Items.LIGHT_BLUE_WOOL,
        Items.LIGHT_GRAY_WOOL,
        Items.LIME_WOOL,
        Items.MAGENTA_WOOL,
        Items.ORANGE_WOOL,
        Items.PINK_WOOL,
        Items.PURPLE_WOOL,
        Items.RED_WOOL,
        Items.WHITE_WOOL,
        Items.YELLOW_WOOL
    );
    
    private static final List<Item> CONC_POWDER = List.of(
        Items.BLACK_CONCRETE_POWDER,
        Items.BLUE_CONCRETE_POWDER,
        Items.BROWN_CONCRETE_POWDER,
        Items.CYAN_CONCRETE_POWDER,
        Items.GRAY_CONCRETE_POWDER,
        Items.GREEN_CONCRETE_POWDER,
        Items.LIGHT_BLUE_CONCRETE_POWDER,
        Items.LIGHT_GRAY_CONCRETE_POWDER,
        Items.LIME_CONCRETE_POWDER,
        Items.MAGENTA_CONCRETE_POWDER,
        Items.ORANGE_CONCRETE_POWDER,
        Items.PINK_CONCRETE_POWDER,
        Items.PURPLE_CONCRETE_POWDER,
        Items.RED_CONCRETE_POWDER,
        Items.WHITE_CONCRETE_POWDER,
        Items.YELLOW_CONCRETE_POWDER
    );
    
    private static final List<Item> GLASS = List.of(
        Items.BLACK_STAINED_GLASS,
        Items.BLUE_STAINED_GLASS,
        Items.BROWN_STAINED_GLASS,
        Items.CYAN_STAINED_GLASS,
        Items.GRAY_STAINED_GLASS,
        Items.GREEN_STAINED_GLASS,
        Items.LIGHT_BLUE_STAINED_GLASS,
        Items.LIGHT_GRAY_STAINED_GLASS,
        Items.LIME_STAINED_GLASS,
        Items.MAGENTA_STAINED_GLASS,
        Items.ORANGE_STAINED_GLASS,
        Items.PINK_STAINED_GLASS,
        Items.PURPLE_STAINED_GLASS,
        Items.RED_STAINED_GLASS,
        Items.WHITE_STAINED_GLASS,
        Items.YELLOW_STAINED_GLASS
    );
    
    private static final List<Item> GLASS_PANE = List.of(
        Items.BLACK_STAINED_GLASS_PANE,
        Items.BLUE_STAINED_GLASS_PANE,
        Items.BROWN_STAINED_GLASS_PANE,
        Items.CYAN_STAINED_GLASS_PANE,
        Items.GRAY_STAINED_GLASS_PANE,
        Items.GREEN_STAINED_GLASS_PANE,
        Items.LIGHT_BLUE_STAINED_GLASS_PANE,
        Items.LIGHT_GRAY_STAINED_GLASS_PANE,
        Items.LIME_STAINED_GLASS_PANE,
        Items.MAGENTA_STAINED_GLASS_PANE,
        Items.ORANGE_STAINED_GLASS_PANE,
        Items.PINK_STAINED_GLASS_PANE,
        Items.PURPLE_STAINED_GLASS_PANE,
        Items.RED_STAINED_GLASS_PANE,
        Items.WHITE_STAINED_GLASS_PANE,
        Items.YELLOW_STAINED_GLASS_PANE
    );
    
    private static final List<Item> TERRA = List.of(
        Items.BLACK_TERRACOTTA,
        Items.BLUE_TERRACOTTA,
        Items.BROWN_TERRACOTTA,
        Items.CYAN_TERRACOTTA,
        Items.GRAY_TERRACOTTA,
        Items.GREEN_TERRACOTTA,
        Items.LIGHT_BLUE_TERRACOTTA,
        Items.LIGHT_GRAY_TERRACOTTA,
        Items.LIME_TERRACOTTA,
        Items.MAGENTA_TERRACOTTA,
        Items.ORANGE_TERRACOTTA,
        Items.PINK_TERRACOTTA,
        Items.PURPLE_TERRACOTTA,
        Items.RED_TERRACOTTA,
        Items.WHITE_TERRACOTTA,
        Items.YELLOW_TERRACOTTA
    );
    
    private static final List<Item> CARPET = List.of(
        Items.BLACK_CARPET,
        Items.BLUE_CARPET,
        Items.BROWN_CARPET,
        Items.CYAN_CARPET,
        Items.GRAY_CARPET,
        Items.GREEN_CARPET,
        Items.LIGHT_BLUE_CARPET,
        Items.LIGHT_GRAY_CARPET,
        Items.LIME_CARPET,
        Items.MAGENTA_CARPET,
        Items.ORANGE_CARPET,
        Items.PINK_CARPET,
        Items.PURPLE_CARPET,
        Items.RED_CARPET,
        Items.WHITE_CARPET,
        Items.YELLOW_CARPET
    );
    
    private static final List<Item> CANDLE = List.of(
        Items.BLACK_CANDLE,
        Items.BLUE_CANDLE,
        Items.BROWN_CANDLE,
        Items.CYAN_CANDLE,
        Items.GRAY_CANDLE,
        Items.GREEN_CANDLE,
        Items.LIGHT_BLUE_CANDLE,
        Items.LIGHT_GRAY_CANDLE,
        Items.LIME_CANDLE,
        Items.MAGENTA_CANDLE,
        Items.ORANGE_CANDLE,
        Items.PINK_CANDLE,
        Items.PURPLE_CANDLE,
        Items.RED_CANDLE,
        Items.WHITE_CANDLE,
        Items.YELLOW_CANDLE
    );
    
    private static final List<Item> BED = List.of(
            Items.BLACK_BED,
            Items.BLUE_BED,
            Items.BROWN_BED,
            Items.CYAN_BED,
            Items.GRAY_BED,
            Items.GREEN_BED,
            Items.LIGHT_BLUE_BED,
            Items.LIGHT_GRAY_BED,
            Items.LIME_BED,
            Items.MAGENTA_BED,
            Items.ORANGE_BED,
            Items.PINK_BED,
            Items.PURPLE_BED,
            Items.RED_BED,
            Items.WHITE_BED,
            Items.YELLOW_BED
    );
    
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }
    
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        oreSmelting(consumer, SMELTABLE, RecipeCategory.MISC, ModItems.PRISMATIC_INGOT.get(), 1.5f, 200, "prismatic_ingot");
        oreBlasting(consumer, SMELTABLE, RecipeCategory.MISC, ModItems.PRISMATIC_INGOT.get(), 1.5f, 100, "prismatic_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PRISMATIC_BLOCK.get())
                .pattern("ppp")
                .pattern("ppp")
                .pattern("ppp")
                .define('p', ModTags.Items.FLATBLOCKS)
                .unlockedBy(getHasName(Items.GLOWSTONE_DUST), has(ModTags.Items.FLATBLOCKS))
                .save(consumer);
        
        //color dye tag recipes
        for(int i = 0; i < COLORS.size(); i++) {
            concretePowder(consumer, COLORS.get(i), CONC_POWDER.get(i));
            woolDyes(consumer, COLORS.get(i), WOOLS.get(i));
            carpetDyes(consumer, COLORS.get(i), CARPET.get(i));
            glassDyes(consumer, COLORS.get(i), GLASS.get(i));
            glassPanes(consumer, COLORS.get(i), GLASS_PANE.get(i));
            terraDyes(consumer, COLORS.get(i), TERRA.get(i));
            candleDyes(consumer, COLORS.get(i), CANDLE.get(i));
            bedDyes(consumer, COLORS.get(i), BED.get(i));
        }
        /*
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SAPPHIRE.get(), 9)
                .requires(ModBlocks.SAPPHIRE_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.SAPPHIRE_BLOCK.get()), has(ModBlocks.SAPPHIRE_BLOCK.get()))
                .save(consumer);
         */
    }
    
    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }
    
    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }
    
    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
    
    protected static void concretePowder(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, 8)
                .requires(color)
                .requires(Blocks.SAND)
                .requires(Blocks.SAND)
                .requires(Blocks.SAND)
                .requires(Blocks.SAND)
                .requires(Blocks.GRAVEL)
                .requires(Blocks.GRAVEL)
                .requires(Blocks.GRAVEL)
                .requires(Blocks.GRAVEL)
                .unlockedBy(getHasName(Blocks.SAND), has(Blocks.SAND))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult));
    }
    
    protected static void woolDyes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, 1)
                .requires(color)
                .requires(ItemTags.WOOL)
                .unlockedBy(getHasName(Blocks.WHITE_WOOL), has(ItemTags.WOOL))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult));
    }
    
    protected static void carpetDyes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, 1)
                .requires(color)
                .requires(ItemTags.WOOL_CARPETS)
                .unlockedBy(getHasName(Blocks.WHITE_WOOL), has(ItemTags.WOOL))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + "dye_" + getItemName(pResult));
    }
    
    protected static void glassDyes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 8)
                .pattern("ppp")
                .pattern("pdp")
                .pattern("ppp")
                .define('p', Blocks.GLASS)
                .define('d', color)
                .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult));
    }
    
    protected static void glassPanes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 8)
                .pattern("ppp")
                .pattern("pdp")
                .pattern("ppp")
                .define('p', Blocks.GLASS_PANE)
                .define('d', color)
                .unlockedBy(getHasName(Blocks.GLASS_PANE), has(Blocks.GLASS_PANE))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult) + "_from_glass_pane");
    }
    
    protected static void terraDyes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 8)
                .pattern("ppp")
                .pattern("pdp")
                .pattern("ppp")
                .define('p', Blocks.TERRACOTTA)
                .define('d', color)
                .unlockedBy(getHasName(Blocks.TERRACOTTA), has(Blocks.TERRACOTTA))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult));
    }
    
    protected static void candleDyes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, 1)
                .requires(Blocks.CANDLE)
                .requires(color)
                .unlockedBy(getHasName(Blocks.CANDLE), has(ItemTags.CANDLES))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + getItemName(pResult));
    }
    
    protected static void bedDyes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> color, ItemLike pResult) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, 1)
                .requires(color)
                .requires(ItemTags.BEDS)
                .unlockedBy(getHasName(Blocks.RED_BED), has(ItemTags.BEDS))
                .save(pFinishedRecipeConsumer, FlatLights.MODID + ":" + "dye_" + getItemName(pResult));
    }
}
