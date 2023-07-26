package com.uberhelixx.flatlights.data.recipes;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);
        Registry.register(Registry.RECIPE_TYPE, PlatingMachineRecipe.TYPE_ID, PLATING_RECIPE);
    }

    public static final RegistryObject<PlatingMachineRecipe.Serializer> PLATING_SERIALIZER =
            RECIPE_SERIALIZER.register("plating", PlatingMachineRecipe.Serializer::new);

    public static IRecipeType<PlatingMachineRecipe> PLATING_RECIPE = new PlatingMachineRecipe.PlatingRecipeType();
}
