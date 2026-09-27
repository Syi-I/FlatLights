package com.uberhelixx.flatlights.common.integration.jei;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.recipe.PlatingMachineRecipe;
import com.uberhelixx.flatlights.common.recipe.SpectralizerRecipe;
import com.uberhelixx.flatlights.util.ClientUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class FlatLightsJEI implements IModPlugin {
    
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FlatLights.MODID, "jei_plugin") ;
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SpectralizerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PlatingMachineRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SPECTRALIZER.get()), SpectralizerRecipeCategory.SPECTRALIZER_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PLATING_MACHINE.get()), PlatingMachineRecipeCategory.PLATING_TYPE);
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(ClientUtils.getLevel()).getRecipeManager();
        List<SpectralizerRecipe> spectralizerRecipes = rm.getAllRecipesFor(SpectralizerRecipe.Type.INSTANCE);
        List<PlatingMachineRecipe> platingMachineRecipes = rm.getAllRecipesFor(PlatingMachineRecipe.Type.INSTANCE);
        
        registration.addRecipes(SpectralizerRecipeCategory.SPECTRALIZER_TYPE, spectralizerRecipes);
        registration.addRecipes(PlatingMachineRecipeCategory.PLATING_TYPE, platingMachineRecipes);
    }
    
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }
}
