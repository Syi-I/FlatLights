package com.uberhelixx.flatlights.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class PillarLightBlock extends RotatingBlock {
    private final double thickness;
    
    public PillarLightBlock(ToIntFunction<BlockState> lightLevel, double thickness) {
        super(lightLevel);
        
        //make shape based off of direction from RotatingBlock
        this.thickness = thickness;
        double initialWidth = 6;
        double totalWidth = initialWidth + thickness;
        UP = Block.box(initialWidth,0, initialWidth, totalWidth, 16, totalWidth);
        DOWN = Block.box(initialWidth,0, initialWidth, totalWidth, 16, totalWidth);
        EAST = Block.box(0, initialWidth, initialWidth, 16, totalWidth, totalWidth);
        WEST = Block.box(0, initialWidth, initialWidth, 16, totalWidth, totalWidth);
        NORTH = Block.box(initialWidth, initialWidth,0, totalWidth, totalWidth,16);
        SOUTH = Block.box(initialWidth, initialWidth,0, totalWidth, totalWidth,16);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level levelIn = context.getLevel();
        BlockPos pos = context.getClickedPos().offset(context.getClickedFace().getOpposite().getNormal());
        boolean waterlogged = levelIn.getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        if (context.getPlayer() != null && levelIn.getBlockState(pos).getBlock() instanceof PillarLightBlock && !context.getPlayer().isCrouching())
            return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                    .setValue(BlockStateProperties.FACING, levelIn.getBlockState(pos).getValue(BlockStateProperties.FACING));
        else
            return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                    .setValue(BlockStateProperties.FACING, context.getClickedFace());
    }
}
