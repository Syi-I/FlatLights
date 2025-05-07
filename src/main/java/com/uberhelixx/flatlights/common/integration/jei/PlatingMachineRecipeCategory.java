package com.uberhelixx.flatlights.common.integration.jei;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.recipe.PlatingMachineRecipe;
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

public class PlatingMachineRecipeCategory implements IRecipeCategory<PlatingMachineRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FlatLights.MODID, "plating");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FlatLights.MODID, "textures/gui/plating_machine_gui.png");
    public static final RecipeType<PlatingMachineRecipe> PLATING_TYPE = new RecipeType<>(UID, PlatingMachineRecipe.class);
    
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic progressBar;
    private final IDrawableAnimated progress;
    
    public PlatingMachineRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 50, 23, 72, 44);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.PLATING_MACHINE.get()));
        progressBar = helper.createDrawable(TEXTURE, 176, 0, 24, 26);
        progress = helper.createAnimatedDrawable(progressBar, 100, IDrawableAnimated.StartDirection.LEFT, false);
    }
    
    @Override
    public RecipeType<PlatingMachineRecipe> getRecipeType() {
        return PLATING_TYPE;
    }
    
    @Override
    public Component getTitle() {
        return Component.translatable("block.flatlights.plating_machine");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, PlatingMachineRecipe platingMachineRecipe, IFocusGroup iFocusGroup) {
        int xOffset = 50;
        int yOffset = 23;
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 53  - xOffset, 26 - yOffset).addIngredients(platingMachineRecipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 53  - xOffset, 48 - yOffset).addIngredients(platingMachineRecipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 103 - xOffset, 37 - yOffset).addItemStack(platingMachineRecipe.getResultItem(null));
    }
    
    @Override
    public void draw(PlatingMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        int xOffset = 50;
        int yOffset = 23;
        progress.draw(guiGraphics, 74 - xOffset, 32 - yOffset);
    }
}
