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

    final String[] blockColors = {"black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray",
            "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow", "glass"};
    final String[] blockSuffixes = {"flatblock", "hexblock", "tiles"};

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        FlatLights.LOGGER.info("Generating generic item models...");
        generateRegularItems(itemGenerated);
        generateRegularBlockItems();
        generatePanelItems();
        FlatLights.LOGGER.info("Finished generating generic item models.");
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

    //generate item models for blocks with normal models and all same sided textures
    private void generateRegularBlockItems() {
        for(RegistryObject<Item> entry : ModItems.BLOCK_ITEMS.getEntries()) {
            String itemName = entry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            boolean suffixPresent = false;
            for(String suffix : blockSuffixes) {
                if(trimmedName.contains(suffix)) {
                    if(trimmedName.contains("glass")) {
                        withExistingParent("" + trimmedName, modLoc("block/" + "glass" + "/" + trimmedName));
                    }
                    else if(trimmedName.contains("large")) {
                        withExistingParent("" + trimmedName, modLoc("block/" + "large_" + suffix + "/" + trimmedName));
                    }
                    else {
                        withExistingParent("" + trimmedName, modLoc("block/" + suffix + "/" + trimmedName));
                    }
                    suffixPresent = true;
                }
            }
            if(!suffixPresent) {
                withExistingParent("" + trimmedName, modLoc("block/" + trimmedName));
            }
            FlatLights.LOGGER.info("Generated " + trimmedName);
        }
    }

    //generate items of PANEL blocks
    private void generatePanelItems() {
        for(RegistryObject<Item> panelItem : ModItems.PANEL_ITEMS.getEntries()) {
            String filePath = panelItem.get().getRegistryName().getPath();
            MiscHelpers.debugLogger("[Data Generators] Item Model Provider PANEL path: " + filePath);
            getBuilder(filePath).parent(new ModelFile.UncheckedModelFile(modLoc("block/panel/" + filePath)))
                    .transforms().transform(ModelBuilder.Perspective.HEAD)
                    .scale(1f, 1f, 1f)
                    .translation(0, 14, 0).end();
        }
    }
}
