package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
        Advancement rootAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.JOGOAT.get()),
                        Component.translatable("advancement.flatlights.jogoat_title").withStyle(ChatFormatting.BOLD),
                        Component.translatable("advancement.flatlights.jogoat_desc").withStyle(ChatFormatting.ITALIC),
                        new ResourceLocation(FlatLights.MODID, "textures/block/large_hexblock/teal_large_hexblock.png"), FrameType.CHALLENGE,
                        true, true, true))
                .addCriterion("found_jogoat", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.JOGOAT.get()))
                .rewards(new AdvancementRewards.Builder().addExperience(1000000))
                .save(consumer, new ResourceLocation(FlatLights.MODID, "jogoat"), existingFileHelper);
    }
}
