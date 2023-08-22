package com.uberhelixx.flatlights.data.client;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;


public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, FlatLights.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        FlatLights.LOGGER.info("[ModItemModelProvider] Generating generic item models...");
        generateRegularItems(itemGenerated);
        generateBlockItems();
        generatePanelItems();
        generateFlatblockItems();
        generatePillarItems();
        FlatLights.LOGGER.info("[ModItemModelProvider] Finished generating generic item models.");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", name);
    }

    //generates item models for regular items (no tools, no items with custom models)
    private void generateRegularItems(ModelFile itemGenerated) {
        for(RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
            String itemName = entry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            FlatLights.LOGGER.info("Generated " + trimmedName);
            builder(itemGenerated, "item/" + trimmedName);
        }
    }

    private void generateBlockItems() {
        for(RegistryObject<Item> block : ModItems.BLOCK_ITEMS.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            getBuilder(filePath).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + filePath)));
            FlatLights.LOGGER.info("[ModItemModelProvider] Generated " + filePath);
        }
    }

    //generate items of PANEL blocks
    private void generatePanelItems() {
        for(RegistryObject<Item> panelItem : ModItems.PANEL_ITEMS.getEntries()) {
            String filePath = panelItem.get().getRegistryName().getPath();
            FlatLights.LOGGER.info("[ModItemModelProvider] Item Model Provider PANEL path: " + filePath);
            getBuilder(filePath).parent(new ModelFile.UncheckedModelFile(modLoc("block/panel/" + filePath)))
                    .transforms().transform(ModelBuilder.Perspective.HEAD)
                    .scale(1f, 1f, 1f)
                    .translation(0, 14, 0).end();
        }
    }

    private void generateFlatblockItems() {
        float scale = 0.2f;
        for(RegistryObject<Item> flatItem : ModItems.FLATBLOCK_ITEMS.getEntries()) {
            String filePath = flatItem.get().getRegistryName().getPath();
            FlatLights.LOGGER.info("[ModItemModelProvider] Item Model Provider FLATBLOCK path: " + filePath);
            getBuilder(filePath).parent(new ModelFile.UncheckedModelFile(modLoc("block/flatblock/" + filePath)))
                    .transforms().transform(ModelBuilder.Perspective.HEAD)
                    .scale(scale, scale, scale)
                    .translation(0, 14, 0).end();
        }
    }

    private void generatePillarItems() {
        float scale = 0.2f;
        for(RegistryObject<Item> pillarItem : ModItems.PILLAR_ITEMS.getEntries()) {
            String filePath = pillarItem.get().getRegistryName().getPath();
            FlatLights.LOGGER.info("[ModItemModelProvider] Item Model Provider PILLAR path: " + filePath);
            getBuilder(filePath).parent(new ModelFile.UncheckedModelFile(modLoc("block/pillar/" + filePath)))
                    .transforms().transform(ModelBuilder.Perspective.HEAD)
                    .scale(scale, scale, scale)
                    .translation(0, 14, 0).end();
        }
    }
}
