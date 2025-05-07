package com.uberhelixx.flatlights.common.integration.jei;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.recipe.SpectralizerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SpectralizerRecipeCategory implements IRecipeCategory<SpectralizerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FlatLights.MODID, "spectralizer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FlatLights.MODID, "textures/gui/spectralizer_gui.png");
    public static final RecipeType<SpectralizerRecipe> SPECTRALIZER_TYPE = new RecipeType<>(UID, SpectralizerRecipe.class);
    
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
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SPECTRALIZER.get()));
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
    public RecipeType<SpectralizerRecipe> getRecipeType() {
        return SPECTRALIZER_TYPE;
    }
    
    @Override
    public Component getTitle() {
        return Component.translatable("block.flatlights.spectralizer");
    }
    
    @Override
    public @Nullable IDrawable getBackground() {
        return this.background;
    }
    
    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, SpectralizerRecipe spectralizerRecipe, IFocusGroup iFocusGroup) {
        int xOffset = 42;
        int yOffset = 10;
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 80  - xOffset, 12 - yOffset).addIngredients(spectralizerRecipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 116 - xOffset, 33 - yOffset).addIngredients(spectralizerRecipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 116 - xOffset, 72 - yOffset).addIngredients(spectralizerRecipe.getIngredients().get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 80  - xOffset, 92 - yOffset).addIngredients(spectralizerRecipe.getIngredients().get(3));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44  - xOffset, 72 - yOffset).addIngredients(spectralizerRecipe.getIngredients().get(4));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 44  - xOffset, 33 - yOffset).addIngredients(spectralizerRecipe.getIngredients().get(5));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 80  - xOffset, 52 - yOffset).addItemStack(spectralizerRecipe.getResultItem(null));
    }
    
    @Override
    public void draw(SpectralizerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        int xOffset = 45;
        int yOffset = 13;
        int xOffsetBar = 42;
        int yOffsetBar = 10;
        outlineRed.draw(guiGraphics, 80 - xOffset, 12 - yOffset);
        outlineOrange.draw(guiGraphics, 116 - xOffset, 33 - yOffset);
        outlineYellow.draw(guiGraphics, 116 - xOffset, 72 - yOffset);
        outlineGreen.draw(guiGraphics, 80  - xOffset, 92 - yOffset);
        outlineBlue.draw(guiGraphics, 44  - xOffset, 72 - yOffset);
        outlinePurple.draw(guiGraphics, 44  - xOffset, 33 - yOffset);
        progress.draw(guiGraphics, 73 - xOffsetBar, 45 - yOffsetBar);
    }
}
