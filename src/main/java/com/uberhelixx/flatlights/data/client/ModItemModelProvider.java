package com.uberhelixx.flatlights.data.client;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
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

        for(RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
            String itemName = entry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            FlatLights.LOGGER.info("Generated " + trimmedName);
            builder(itemGenerated, "item/" + trimmedName);
        }
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
        FlatLights.LOGGER.info("Finished generating generic item models.");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", name);
    }
}
