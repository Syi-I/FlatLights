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


public class ModItemModelProvider  extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, FlatLights.MOD_ID, existingFileHelper);
    }

    final String[] blockColors = {"black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray",
            "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow", "glass"};
    final String[] suffixes = {"flatblock", "hexblock", "large_hexblock", "tiles", "large_tiles"};
    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        builder(itemGenerated, "item/gun_rat");
        builder(itemGenerated, "item/prismatic_ingot");
        builder(itemGenerated, "item/prismatic_boots");
        builder(itemGenerated, "item/prismatic_leggings");
        builder(itemGenerated, "item/prismatic_chestplate");
        builder(itemGenerated, "item/prismatic_helmet");
        builder(itemGenerated, "item/prisma_nucleus");
        builder(itemGenerated, "item/bread_but_high_quality");
        blockModelCombos("flatblock");
        blockModelCombos("hexblock");
        blockModelCombos("large_hexblock");
        blockModelCombos("tiles");
        blockModelCombos("large_tiles");
        FlatLights.LOGGER.info("Testing to see what the getID() function does");
        for(RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
            String itemName = entry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            FlatLights.LOGGER.info("Entry: " + trimmedName);
        }
        for(RegistryObject<Item> entry : ModItems.BLOCK_ITEMS.getEntries()) {
            String itemName = entry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            FlatLights.LOGGER.info("Entry: " + trimmedName);
        }
        FlatLights.LOGGER.info("Items from ModItems register: they are indeed somewhere.");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", name);
    }

    private void blockModelCombos(String suffix) {
        for(String blockColor : blockColors) {
            String blockName = blockColor + "_" + suffix;
            if(blockColor.equals("glass")) {
                withExistingParent("" + blockName, modLoc("block/" + "glass" + "/" + blockName));

            }
            else {
                withExistingParent("" + blockName, modLoc("block/" + suffix + "/" + blockName));
            }
        }
    }
}
