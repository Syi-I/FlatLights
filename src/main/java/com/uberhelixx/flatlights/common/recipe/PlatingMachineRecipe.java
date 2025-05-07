package com.uberhelixx.flatlights.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PlatingMachineRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    
    public PlatingMachineRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }
    
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }
        
        return inputItems.get(0).test(pContainer.getItem(0))
                && inputItems.get(1).test(pContainer.getItem(1));
    }
    
    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }
    
    public static class Type implements RecipeType<PlatingMachineRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "plating";
    }
    
    public static class Serializer implements RecipeSerializer<PlatingMachineRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(FlatLights.MODID, "plating");
        
        @Override
        public PlatingMachineRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            
            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);
            
            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            
            return new PlatingMachineRecipe(inputs, output, pRecipeId);
        }
        
        @Override
        public @Nullable PlatingMachineRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            
            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            
            ItemStack output = pBuffer.readItem();
            return new PlatingMachineRecipe(inputs, output, pRecipeId);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, PlatingMachineRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());
            
            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
