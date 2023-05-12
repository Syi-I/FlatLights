package com.uberhelixx.flatlights.data;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.data.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModBlocks.PRISMATIC_BLOCK.get()), ModItems.PRISMATIC_INGOT.get(), 1.5f, 200)
                .addCriterion("has_item", hasItem(ModBlocks.PRISMATIC_BLOCK.get()))
                .build(consumer, modId("prismatic_ingot_smelting"));
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ModBlocks.PRISMATIC_BLOCK.get()), ModItems.PRISMATIC_INGOT.get(), 1.5f, 100)
                .addCriterion("has_item", hasItem(ModBlocks.PRISMATIC_BLOCK.get()))
                .build(consumer, modId("prismatic_ingot_blasting"));
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.PRISMATIC_BLOCK.get())
                .key('p', ModTags.Items.FLATBLOCKS)
                .patternLine("ppp")
                .patternLine("ppp")
                .patternLine("ppp")
                .addCriterion("has_item", hasItem(ModTags.Items.FLATBLOCKS))
                .build(consumer);
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(FlatLights.MOD_ID, path);
    }
}
