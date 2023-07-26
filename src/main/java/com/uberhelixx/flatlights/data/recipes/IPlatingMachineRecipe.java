package com.uberhelixx.flatlights.data.recipes;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IPlatingMachineRecipe extends IRecipe<Inventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(FlatLights.MOD_ID, "plating");

    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canFit(int width, int height) {
        return true;
    }

    @Override
    default boolean isDynamic() {
        return true;
    }
}
