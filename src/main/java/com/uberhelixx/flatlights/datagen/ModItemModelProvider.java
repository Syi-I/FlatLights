package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.block.PlateBlock;
import com.uberhelixx.flatlights.common.block.WireGlassBlock;
import com.uberhelixx.flatlights.common.block.blackout.BlackoutFlatBlock;
import com.uberhelixx.flatlights.common.block.light.*;
import com.uberhelixx.flatlights.common.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FlatLights.MODID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        //get all blocks from registry and create item models from the parent block models
        for(RegistryObject<Block> blockRegistryObject : ModBlocks.BLOCKS.getEntries()) {
            String filePath = blockRegistryObject.getId().getPath();
            Block block = blockRegistryObject.get();
            if(block instanceof FlatBlock && !(block instanceof BlackoutFlatBlock) && !filePath.endsWith("_blackout")) {
                makeFlatblockModel(filePath);
            }
            //normal block models
            if(block instanceof BlackoutFlatBlock || block instanceof PlateBlock || block instanceof WireGlassBlock) {
                withExistingParent(filePath, modLoc("block/" + filePath));
            }
            //should take both normal and blackout since both use custom models
            if(block instanceof SlabLightBlock) {
                makePanelModel(filePath);
            }
            if(block instanceof PillarLightBlock) {
                makePillarModel(filePath);
            }
            if(block instanceof HorizontalEdgeBlock) {
                makeEdgeHModel(filePath);
            }
            if(block instanceof VerticalEdgeBlock) {
                makeEdgeVModel(filePath);
            }
        }
        //item models
        for(var itemRegistryObject : ModItems.ITEMS.getEntries()) {
            String filePath = itemRegistryObject.getId().getPath();
            singleTexture(filePath, mcLoc("item/generated"), "layer0", modLoc("item/" + filePath));
        }
        //toggle item models
        for(var itemRegistryObject : ModItems.TOGGLE_ITEMS.getEntries()) {
            String filePath = itemRegistryObject.getId().getPath();
            makeToggleItemModel(filePath);
        }
    }
    
    private void makeFlatblockModel(String filePath) {
        withExistingParent(filePath, modLoc("block/flatblock/" + filePath));
    }
    
    private void makePanelModel(String filePath) {
        withExistingParent(filePath, modLoc("block/panel/" + filePath));
    }
    
    private void makePillarModel(String filePath) {
        withExistingParent(filePath, modLoc("block/pillar/" + filePath));
    }
    
    private void makeEdgeHModel(String filePath) {
        withExistingParent(filePath, modLoc("block/horizontal_edge/" + filePath));
    }
    
    private void makeEdgeVModel(String filePath) {
        withExistingParent(filePath, modLoc("block/vertical_edge/" + filePath));
    }
    
    private void makeToggleItemModel(String filePath) {
        ResourceLocation enabledModelPath = modLoc("item/" + filePath + "_active"); // Path to your enabled model
        ResourceLocation defaultModelPath = modLoc("item/" + filePath); // Path to your default model
        
        // Start building your item model
        getBuilder(filePath) // This should match your item's registry name
                .parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", defaultModelPath)
                .override()
                .predicate(ResourceLocation.tryBuild(FlatLights.MODID, "mode"), 1.0F) // Using custom property
                .model(singleTexture(filePath + "_active", mcLoc("item/handheld"), "layer0", enabledModelPath))
                .end();
    }
}
