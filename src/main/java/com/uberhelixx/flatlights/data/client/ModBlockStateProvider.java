package com.uberhelixx.flatlights.data.client;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.HorizontalEdgeBlock;
import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    final String[] blockColors = {"black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray",
            "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow", "glass"};
    final String[] blockSuffixes = {"flatblock", "hexblock", "tiles"};
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, FlatLights.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        FlatLights.LOGGER.info("[ModBlockStateProvider] Generating generic blockstates...");
        regularBlocks();
        generatePanels();
        generateFlatblocks();
        generatePillars();
        //generateEdges();
        generateEdgeMultiparts();
        FlatLights.LOGGER.info("[ModBlockStateProvider] Finished generating generic blockstates.");

    }

    //for making PANEL block states from existing panel model files
    private void generatePanels() {
        for(RegistryObject<Block> block : ModBlocks.PANELS.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            rotationStates(block.get(), $ -> models().getExistingFile(modLoc("block/panel/" + filePath)));
        }
    }

    //for rotating blockstates
    private void rotationStates(Block block, Function<BlockState, ModelFile> modelFunc) {
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

    //for horizontal rotating blocks with up and down half (similar to stair positioning)
    private void rotationHalfStates(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
                    Half half = state.get(BlockStateProperties.HALF);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(half == Half.TOP ? 180 : 0)
                            .rotationY(half == Half.TOP ? (((int)facing.getHorizontalAngle() % 360) + 180) : (((int)facing.getHorizontalAngle() % 360)))
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }

    private void multipartEdges(Block block) {
        String filePath = block.getRegistryName().getPath();
        ModelFile edgeModel = models().getExistingFile(modLoc("block/horizontal_edge/" + filePath));
        //horizontal facing index (order is S-W-N-E, 0-3)
        getMultipartBuilder(block)
                .part()
                    .modelFile(edgeModel).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 2)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.NORTH_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationY(180).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 0)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.SOUTH_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationY(270).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 1)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.WEST_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationY(90).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 3)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, false)
                        .condition(HorizontalEdgeBlock.EAST_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationX(180).rotationY(180).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 2)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.NORTH_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationX(180).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 0)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.SOUTH_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationX(180).rotationY(90).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 1)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.WEST_ADJ, true)
                    .end()
                    .end()
                .part()
                    .modelFile(edgeModel).rotationX(180).rotationY(270).addModel().useOr()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.FACING_INDEX, 3)
                    .end()
                    .nestedGroup()
                        .condition(HorizontalEdgeBlock.PLACED_TOP, true)
                        .condition(HorizontalEdgeBlock.EAST_ADJ, true)
                    .end()
                    .end();
    }

    private void generatePillars() {
        for(RegistryObject<Block> block : ModBlocks.PILLARS.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            rotationStates(block.get(), $ -> models().getExistingFile(modLoc("block/pillar/" + filePath)));
        }
    }

    private void generateEdges() {
        for(RegistryObject<Block> block : ModBlocks.HORIZONTAL_EDGES.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            rotationHalfStates(block.get(), $ -> models().getExistingFile(modLoc("block/horizontal_edge/" + filePath)));
        }
    }

    private void generateEdgeMultiparts() {
        for(RegistryObject<Block> block : ModBlocks.HORIZONTAL_EDGES.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            multipartEdges(block.get());
        }
    }

    private void generateFlatblocks() {
        for(RegistryObject<Block> block : ModBlocks.FLATBLOCKS.getEntries()) {
            String filePath = block.get().getRegistryName().getPath();
            simpleBlock(block.get(), models().getExistingFile(modLoc("block/flatblock/" + filePath)));
        }
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
            if(!suffixPresent && filePath != "prismatic_block") {
                texturePath = ("block/" + filePath);
            }
            FlatLights.LOGGER.info("[ModBlockStateProvider] blockTexture path: " + texturePath);
            ResourceLocation blockTexture = new ResourceLocation(FlatLights.MOD_ID, texturePath);
            simpleBlock(block.get(), models().cubeAll(filePath, blockTexture));
            FlatLights.LOGGER.info("[ModBlockStateProvider] Generated " + filePath);
        }
    }
}
