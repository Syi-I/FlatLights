package com.uberhelixx.flatlights.data.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.datafixers.util.Pair;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation,  LootTable> map, ValidationTracker validationTracker) {
        map.forEach((p1, p2) -> LootTableManager.validateLootTable(validationTracker, p1, p2));
    }

    public static class ModBlockLootTables extends BlockLootTables {
        @Override
        protected void addTables() {
            for(RegistryObject<Block> blockEntry : ModBlocks.BLOCKS.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
            for(RegistryObject<Block> blockEntry : ModBlocks.SPECIAL_BLOCKS.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
            for(RegistryObject<Block> blockEntry : ModBlocks.PANELS.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
            for(RegistryObject<Block> blockEntry : ModBlocks.FLATBLOCKS.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
            for(RegistryObject<Block> blockEntry : ModBlocks.PILLARS.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
            for(RegistryObject<Block> blockEntry : ModBlocks.HORIZONTAL_EDGES.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
            for(RegistryObject<Block> blockEntry : ModBlocks.VERTICAL_EDGES.getEntries()) {
                registerDropSelfLootTable(blockEntry.get());
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            Iterable<Block> allBlocks;
            Collection<RegistryObject<Block>> blocks = ModBlocks.BLOCKS.getEntries();
            Collection<RegistryObject<Block>> specialBlocks = ModBlocks.SPECIAL_BLOCKS.getEntries();
            Collection<RegistryObject<Block>> panels = ModBlocks.PANELS.getEntries();
            Collection<RegistryObject<Block>> flatblocks = ModBlocks.FLATBLOCKS.getEntries();
            Collection<RegistryObject<Block>> pillars = ModBlocks.PILLARS.getEntries();
            Collection<RegistryObject<Block>> horizontalEdges = ModBlocks.HORIZONTAL_EDGES.getEntries();
            Collection<RegistryObject<Block>> verticalEdges = ModBlocks.VERTICAL_EDGES.getEntries();
            Collection<RegistryObject<Block>> combined = new ArrayList<>(blocks);
            combined.addAll(specialBlocks);
            combined.addAll(panels);
            combined.addAll(flatblocks);
            combined.addAll(pillars);
            combined.addAll(horizontalEdges);
            combined.addAll(verticalEdges);
            allBlocks = combined.stream().map(RegistryObject::get).collect(Collectors.toList());
            return allBlocks;
        }
    }

}
