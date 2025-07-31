package com.uberhelixx.flatlights.datagen;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.block.PlateBlock;
import com.uberhelixx.flatlights.common.block.WireGlassBlock;
import com.uberhelixx.flatlights.common.block.blackout.BlackoutFlatBlock;
import com.uberhelixx.flatlights.common.block.light.*;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.lib.LibBlockNames;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FlatLights.MODID, exFileHelper);
    }
    
    @Override
    protected void registerStatesAndModels() {
        //get all blocks from registry to make the block state and block models according to block type
        for(RegistryObject<Block> blockRegistryObject : ModBlocks.BLOCKS.getEntries()) {
            String filePath = blockRegistryObject.getId().getPath();
            MiscUtils.infoLog("File path is " + filePath);
            Block block = blockRegistryObject.get();
            
            //flatblocks use custom model, blackout flatblocks use normal cube model
            if(block instanceof FlatBlock && !(block instanceof BlackoutFlatBlock)) {
                makeFlatblock(block, filePath);
            }
            if(block instanceof BlackoutFlatBlock) {
                makeBlackoutFlatblock(block, filePath);
            }
            if(block instanceof PlateBlock) {
                makePlateBlock(block, filePath);
            }
            //should take both normal and blackout panels since both use custom models
            if(block instanceof SlabLightBlock) {
                makePanel(block, filePath);
            }
            if(block instanceof PillarLightBlock) {
                makePillar(block, filePath);
            }
            if(block instanceof HorizontalEdgeBlock) {
                makeEdgeH(block, filePath);
            }
            if(block instanceof VerticalEdgeBlock) {
                makeEdgeV(block, filePath);
            }
            if(block instanceof WireGlassBlock) {
                makeWireGlassBlock(block, filePath);
            }
        }
        
    }

    private void makeFlatblock(Block block, String filePath) {
        simpleBlock(block, models().getExistingFile(modLoc("block/flatblock/" + filePath)));
    }
    
    private void makeBlackoutFlatblock(Block block, String filePath) {
        simpleBlock(block, models().cubeAll(filePath, modLoc("block/blackout/" + filePath)));
    }
    
    private void makePlateBlock(Block block, String filePath) {
        if(filePath.contains(LibBlockNames.LARGE_SUFFIX)) {
            if(filePath.contains(LibBlockNames.HEXBLOCK_SUFFIX)) {
                simpleBlock(block, models().cubeAll(filePath, modLoc("block/large_hexblock/" + filePath)));
            }
            else if(filePath.contains(LibBlockNames.TILE_SUFFIX)) {
                simpleBlock(block, models().cubeAll(filePath, modLoc("block/large_tiles/" + filePath)));
            }
        }
        else {
            if(filePath.contains(LibBlockNames.HEXBLOCK_SUFFIX)) {
                simpleBlock(block, models().cubeAll(filePath, modLoc("block/hexblock/" + filePath)));
            }
            else if(filePath.contains(LibBlockNames.TILE_SUFFIX)) {
                simpleBlock(block, models().cubeAll(filePath, modLoc("block/tiles/" + filePath)));
            }
        }
        FlatLights.LOGGER.error("[BlockStateProvider] Issue generating plate block with file path: " + filePath);
    }
    
    //for rotating blockstates
    private void rotationStates(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        getVariantBuilder(block)
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(facing == Direction.DOWN ? 180 : facing.getAxis().isHorizontal() ? 90 : 0)
                            .rotationY(facing.getAxis().isVertical() ? 0 : (((int)facing.toYRot() + angleOffset) % 360) + 180)
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }
    
    private void makePanel(Block block, String filePath) {
        rotationStates(block, $ -> models().getExistingFile(modLoc("block/panel/" + filePath)), 0);
    }
    
    private void makePillar(Block block, String filePath) {
        rotationStates(block, $ -> models().getExistingFile(modLoc("block/pillar/" + filePath)), 0);
    }
    
    private void multipartEdgesH(Block block, String filePath) {
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
    
    private void makeEdgeH(Block block, String filePath) {
        multipartEdgesH(block, filePath);
    }
    
    private void multipartEdgesV(Block block, String filePath) {
        //uses same model as the horizontal edges, but we rotate this 90 degrees for the vertical parts
        ModelFile edgeModelH = models().getExistingFile(modLoc("block/horizontal_edge/" + filePath.replace("vertical", "horizontal")));
        ModelFile edgeModelV = models().getExistingFile(modLoc("block/vertical_edge/" + filePath));
        //horizontal facing index (order is S-W-N-E, 0-3)
        getMultipartBuilder(block)
                .part()
                    .modelFile(edgeModelH).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationY(180).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationY(270).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationY(90).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationX(180).rotationY(180).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationX(180).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationX(180).rotationY(90).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                    .end()
                .end()
                .part()
                    .modelFile(edgeModelH).rotationX(180).rotationY(270).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                    .end()
                .end()
                .part() //Q1
                    .modelFile(edgeModelV).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                .end()
                .part() //Q2
                    .modelFile(edgeModelV).rotationY(90).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 2)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                .end()
                .part() //Q3
                    .modelFile(edgeModelV).rotationY(180).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 3)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                .end()
                .part() //Q4
                    .modelFile(edgeModelV).rotationY(270).addModel().useOr()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, false)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 1)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, false)
                        .condition(VerticalEdgeBlock.UP_ADJ, true)
                        .condition(VerticalEdgeBlock.DOWN_ADJ, false)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, false)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                    .nestedGroup()
                        .condition(VerticalEdgeBlock.FACING_INDEX, 0)
                        .condition(VerticalEdgeBlock.LEFT_ADJ, true)
                        .condition(VerticalEdgeBlock.RIGHT_ADJ, true)
                    .end()
                .end();
    }
    
    private void makeEdgeV(Block block, String filePath) {
        multipartEdgesV(block, filePath);
    }
    
    private void makeWireGlassBlock(Block block, String filePath) {
        simpleBlock(block, models().cubeAll(filePath, modLoc("block/glass/" + filePath)).renderType("translucent"));
    }
}
