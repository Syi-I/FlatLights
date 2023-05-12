package com.uberhelixx.flatlights.data.client;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, FlatLights.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        FlatLights.LOGGER.info("Generating generic blockstates...");

        for(RegistryObject<Block> blockEntry : ModBlocks.BLOCKS.getEntries()) {
            String itemName = blockEntry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            FlatLights.LOGGER.info("Generated " + trimmedName);
            simpleBlock(blockEntry.get());
        }
        //simpleBlock(ModBlocks.BLACK_FLATBLOCK.get());
        FlatLights.LOGGER.info("Finished generating generic blockstates.");

    }
}
