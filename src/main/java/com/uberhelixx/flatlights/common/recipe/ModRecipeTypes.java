package com.uberhelixx.flatlights.common.recipe;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FlatLights.MODID);
    
    public static final RegistryObject<RecipeSerializer<PlatingMachineRecipe>> PLATING_SERIALIZER =
            SERIALIZERS.register("plating", () -> PlatingMachineRecipe.Serializer.INSTANCE);
    
    public static final RegistryObject<RecipeSerializer<SpectralizerRecipe>> SPECTRALIZER_SERIALIZER =
            SERIALIZERS.register("spectralizer", () -> SpectralizerRecipe.Serializer.INSTANCE);
    
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
