package com.uberhelixx.flatlights.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SpectralizerRecipe implements ISpectralizerRecipe {

    public SpectralizerRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    @Override
    public boolean matches(Inventory inv, World worldIn) {
        if(recipeItems.get(0).test(inv.getStackInSlot(0)) &&
            recipeItems.get(1).test(inv.getStackInSlot(1)) &&
            recipeItems.get(2).test(inv.getStackInSlot(2)) &&
            recipeItems.get(3).test(inv.getStackInSlot(3)) &&
            recipeItems.get(4).test(inv.getStackInSlot(4))) {
                return recipeItems.get(5).test(inv.getStackInSlot(5));
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(Inventory inv) {
        return null;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.SPECTRALIZER.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.SPECTRALIZER_SERIALIZER.get();
    }

    public static class SpectralizerRecipeType implements IRecipeType<SpectralizerRecipe> {
        @Override
        public String toString() {
            return SpectralizerRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpectralizerRecipe> {

        @Override
        public SpectralizerRecipe read(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));

            JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(6, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.deserialize(ingredients.get(i)));
            }

            return new SpectralizerRecipe(recipeId, output, inputs);
        }

        @Nullable
        @Override
        public SpectralizerRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(6, Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.read(buffer));

            ItemStack output = buffer.readItemStack();
            return new SpectralizerRecipe(recipeId, output, inputs);
        }

        @Override
        public void write(PacketBuffer buffer, SpectralizerRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput(), false);
        }
    }
}
