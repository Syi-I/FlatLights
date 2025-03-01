package com.uberhelixx.flatlights.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.data.recipes.PlatingMachineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PlatingMachineRecipeCategory implements IRecipeCategory<PlatingMachineRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FlatLights.MOD_ID, "plating");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FlatLights.MOD_ID, "textures/gui/plating_machine_gui.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic progressBar;
    private final IDrawableAnimated progress;

    public PlatingMachineRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 50, 23, 72, 44);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.PLATING_MACHINE.get()));
        progressBar = helper.createDrawable(TEXTURE, 176, 0, 24, 26);
        progress = helper.createAnimatedDrawable(progressBar, 100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends PlatingMachineRecipe> getRecipeClass() {
        return PlatingMachineRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.PLATING_MACHINE.get().getTranslatedName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(PlatingMachineRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PlatingMachineRecipe recipe, IIngredients ingredients) {
        int xOffset = 51;
        int yOffset = 24;
        recipeLayout.getItemStacks().init(0, true,  53  - xOffset, 26 - yOffset);
        recipeLayout.getItemStacks().init(1, true,  53  - xOffset, 48 - yOffset);
        recipeLayout.getItemStacks().init(2, false, 103 - xOffset, 37 - yOffset);
        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public void draw(PlatingMachineRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, matrixStack, mouseX, mouseY);
        int xOffset = 50;
        int yOffset = 23;
        progress.draw(matrixStack, 74 - xOffset, 32 - yOffset);
    }
}
