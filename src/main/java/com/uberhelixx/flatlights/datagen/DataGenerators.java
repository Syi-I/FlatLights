package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        

        FlatLights.LOGGER.info("Generating block tags...");
        ModBlockTagsProvider blockTagGenerator = generator.addProvider(event.includeServer(), new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
        FlatLights.LOGGER.info("Finished generating block tags.");
        
        FlatLights.LOGGER.info("Generating item tags...");
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        FlatLights.LOGGER.info("Finished generating item tags.");
        
        FlatLights.LOGGER.info("Generating loot tables...");
        generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput));
        FlatLights.LOGGER.info("Finished generating loot tables.");
        
        FlatLights.LOGGER.info("Generating recipes...");
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
        FlatLights.LOGGER.info("Finished generating recipes.");
        
        FlatLights.LOGGER.info("Generating advancements...");
        generator.addProvider(event.includeClient(), new ForgeAdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of(new ModAdvancementProvider())));
        FlatLights.LOGGER.info("Finished generating advancements.");
        
        /*
        just ended up doing these two manually since the structure loot bugs out from not understanding forge tags
        and if I automate just the one then it double generates the forge:loot_modifiers/global_loot_modifier.json
        between my data and the generated data
        */
        //FlatLights.LOGGER.info("Generating global loot modifiers...");
        //generator.addProvider(event.includeClient(), new ModGlobalLootModifierProvider(packOutput));
        //FlatLights.LOGGER.info("Finished generating global loot modifiers...");
    }
}
