package com.uberhelixx.flatlights.integration.jei;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.data.recipes.ModRecipeTypes;
import com.uberhelixx.flatlights.data.recipes.PlatingMachineRecipe;
import com.uberhelixx.flatlights.data.recipes.SpectralizerRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class FlatLightsJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FlatLights.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SpectralizerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PlatingMachineRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SPECTRALIZER.get()), SpectralizerRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PLATING_MACHINE.get()), PlatingMachineRecipeCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().world).getRecipeManager();
        registration.addRecipes(rm.getRecipesForType(ModRecipeTypes.SPECTRALIZER_RECIPE).stream()
                .filter(r -> r instanceof SpectralizerRecipe).collect(Collectors.toList()), SpectralizerRecipeCategory.UID);
        registration.addRecipes(rm.getRecipesForType(ModRecipeTypes.PLATING_RECIPE).stream()
                .filter(r -> r instanceof PlatingMachineRecipe).collect(Collectors.toList()), PlatingMachineRecipeCategory.UID);
    }
}
