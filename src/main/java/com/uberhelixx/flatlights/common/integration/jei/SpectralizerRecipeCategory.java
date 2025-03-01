package com.uberhelixx.flatlights.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.data.recipes.SpectralizerRecipe;
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

public class SpectralizerRecipeCategory implements IRecipeCategory<SpectralizerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FlatLights.MOD_ID, "spectralizer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FlatLights.MOD_ID, "textures/gui/spectralizer_gui.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic outlineRed;
    private final IDrawableStatic outlineOrange;
    private final IDrawableStatic outlineYellow;
    private final IDrawableStatic outlineGreen;
    private final IDrawableStatic outlineBlue;
    private final IDrawableStatic outlinePurple;
    private final IDrawableStatic progressBar;
    private final IDrawableAnimated progress;

    public SpectralizerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 42, 10, 92, 100);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.SPECTRALIZER.get()));
        this.outlineRed = helper.createDrawable(TEXTURE, 176, 22, 22, 22);
        this.outlineOrange = helper.createDrawable(TEXTURE, 176, 110, 22, 22);
        this.outlineYellow = helper.createDrawable(TEXTURE, 176, 88, 22, 22);
        this.outlineGreen = helper.createDrawable(TEXTURE, 176, 0, 22, 22);
        this.outlineBlue = helper.createDrawable(TEXTURE, 176, 44, 22, 22);
        this.outlinePurple = helper.createDrawable(TEXTURE, 176, 66, 22, 22);
        this.progressBar = helper.createDrawable(TEXTURE, 198, 0, 30, 30);
        this.progress = helper.createAnimatedDrawable(progressBar, 80, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SpectralizerRecipe> getRecipeClass() {
        return SpectralizerRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.SPECTRALIZER.get().getTranslatedName().getString();
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
    public void setIngredients(SpectralizerRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SpectralizerRecipe recipe, IIngredients ingredients) {
        int xOffset = 43;
        int yOffset = 11;
        recipeLayout.getItemStacks().init(0, true, 80  - xOffset, 12 - yOffset);
        recipeLayout.getItemStacks().init(1, true, 116 - xOffset, 33 - yOffset);
        recipeLayout.getItemStacks().init(2, true, 116 - xOffset, 72 - yOffset);
        recipeLayout.getItemStacks().init(3, true, 80  - xOffset, 92 - yOffset);
        recipeLayout.getItemStacks().init(4, true, 44  - xOffset, 72 - yOffset);
        recipeLayout.getItemStacks().init(5, true, 44  - xOffset, 33 - yOffset);
        recipeLayout.getItemStacks().init(6, false,80  - xOffset, 52 - yOffset);
        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public void draw(SpectralizerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, matrixStack, mouseX, mouseY);
        int xOffset = 45;
        int yOffset = 13;
        int xOffsetBar = 42;
        int yOffsetBar = 10;
        outlineRed.draw(matrixStack, 80 - xOffset, 12 - yOffset);
        outlineOrange.draw(matrixStack, 116 - xOffset, 33 - yOffset);
        outlineYellow.draw(matrixStack, 116 - xOffset, 72 - yOffset);
        outlineGreen.draw(matrixStack, 80  - xOffset, 92 - yOffset);
        outlineBlue.draw(matrixStack, 44  - xOffset, 72 - yOffset);
        outlinePurple.draw(matrixStack, 44  - xOffset, 33 - yOffset);
        progress.draw(matrixStack, 73 - xOffsetBar, 45 - yOffsetBar);
    }
}
