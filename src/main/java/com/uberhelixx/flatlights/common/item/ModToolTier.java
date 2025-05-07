package com.uberhelixx.flatlights.common.item;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.startup.registry.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTier {
    public static final Tier PRISMATIC = TierSortingRegistry.registerTier(
            new ForgeTier(6, 10240, 4.5f, 9.0f, 100,
                    ModTags.Blocks.NEEDS_PRISMATIC_TOOL,
                    () -> Ingredient.of(ModItems.PRISMATIC_INGOT.get())),
                    new ResourceLocation(FlatLights.MODID, "prismatic"),
                    List.of(Tiers.NETHERITE), List.of());
}
