package com.uberhelixx.flatlights.data.client;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;

import static com.uberhelixx.flatlights.util.MiscHelpers.debugLogger;

public class ModBlockStateProvider extends BlockStateProvider {
    final String[] blockColors = {"black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray",
            "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow", "glass"};
    final String[] blockSuffixes = {"flatblock", "hexblock", "tiles"};
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, FlatLights.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        FlatLights.LOGGER.info("Generating generic blockstates...");

        /*for(RegistryObject<Block> blockEntry : ModBlocks.BLOCKS.getEntries()) {
            String itemName = blockEntry.getId().toString();
            String trimmedName = itemName.replace("flatlights:", "");
            FlatLights.LOGGER.info("Generated " + trimmedName);
            simpleBlock(blockEntry.get());
        }*/
        //simpleBlock(ModBlocks.BLACK_FLATBLOCK.get());
        regularBlocks();
        generatePanels();
        FlatLights.LOGGER.info("Finished generating generic blockstates.");

    }

    //for making PANEL block states from existing panel model files
    private void generatePanels() {
        for(RegistryObject<Block> block : ModBlocks.PANELS.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            panelStates(block.get(), $ -> models().getExistingFile(modLoc("block/panel/" + filePath)));

        }
        /*panelStates(ModBlocks.BLUE_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.BROWN_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.CYAN_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.GRAY_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.GREEN_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.LIGHT_BLUE_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.LIGHT_GRAY_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.LIME_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.MAGENTA_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.ORANGE_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.PINK_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.PURPLE_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.RED_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.WHITE_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));
        panelStates(ModBlocks.YELLOW_PANEL.get(), $ -> models().getExistingFile(modLoc("block/panel/black_panel")));*/
    }

    //for panel blockstates
    public void panelStates(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.get(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(facing == Direction.DOWN ? 180 : facing.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(facing.getAxis().isVertical() ? 0 : (((int)facing.getHorizontalAngle() % 360) + 180))
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }

    private void regularBlocks() {
        for(RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries()) {
            //just gets block registry name
            String filePath = block.get().getRegistryName().getPath();
            String texturePath = filePath;
            boolean suffixPresent = false;
            //sort through each basic block type to get texture path for use as a ResourceLocation
            for(String suffix : blockSuffixes) {
                if(filePath.contains(suffix)) {
                    if(filePath.contains("glass")) {
                        texturePath = ("block/" + "glass" + "/" + filePath);
                    }
                    else if(filePath.contains("large")) {
                        texturePath = ("block/" + "large_" + suffix + "/" + filePath);
                    }
                    else {
                        texturePath = ("block/" + suffix + "/" + filePath);
                    }
                    suffixPresent = true;
                }
            }
            if(!suffixPresent) {
                texturePath = ("block/" + filePath);
            }
            FlatLights.LOGGER.info("[ModBlockStateProvider] blockTexture path: " + texturePath);
            ResourceLocation blockTexture = new ResourceLocation(FlatLights.MOD_ID, texturePath);
            simpleBlock(block.get(), models().cubeAll(filePath, blockTexture));
            FlatLights.LOGGER.info("[ModBlockStateProvider] Generated " + filePath);
        }
    }
}
