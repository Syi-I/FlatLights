package com.uberhelixx.flatlights.data;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.data.client.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
        //generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(generator, existingFileHelper);
        FlatLights.LOGGER.info("Generating block tags...");
        generator.addProvider(blockTags);
        FlatLights.LOGGER.info("Finished generating block tags.");

        FlatLights.LOGGER.info("Generating item tags...");
        generator.addProvider(new ModItemTagsProvider(generator, blockTags, existingFileHelper));
        FlatLights.LOGGER.info("Finished generating item tags.");

        FlatLights.LOGGER.info("Generating loot tables...");
        generator.addProvider(new ModLootTableProvider(generator));
        FlatLights.LOGGER.info("Finished generating loot tables.");

        FlatLights.LOGGER.info("Generating recipes...");
        generator.addProvider(new ModRecipeProvider(generator));
        FlatLights.LOGGER.info("Finished generating recipes.");

    }
}
