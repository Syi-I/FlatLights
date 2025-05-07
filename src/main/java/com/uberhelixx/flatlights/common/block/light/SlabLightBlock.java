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

public class SlabLightBlock extends RotatingBlock {
    
    private final double thickness;
    
    public SlabLightBlock(ToIntFunction<BlockState> lightLevel, double thickness) {
        super(lightLevel);
        
        //make shape based off of direction from RotatingBlock
        this.thickness = thickness;
        UP = Block.box(0,0,0, 16, thickness,16);
        DOWN = Block.box(0,16 - thickness,0, 16,16,16);
        EAST = Block.box(0,0,0, thickness,16,16);
        WEST = Block.box(16 - thickness,0,0, 16,16,16);
        NORTH = Block.box(0,0,16 - thickness, 16,16,16);
        SOUTH = Block.box(0,0,0, 16,16, thickness);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level levelIn = context.getLevel();
        BlockPos pos = context.getClickedPos().offset(context.getClickedFace().getOpposite().getNormal());
        boolean waterlogged = levelIn.getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        if (context.getPlayer() != null && levelIn.getBlockState(pos).getBlock() instanceof SlabLightBlock && !context.getPlayer().isCrouching())
            return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                    .setValue(BlockStateProperties.FACING, levelIn.getBlockState(pos).getValue(BlockStateProperties.FACING));
        else
            return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                    .setValue(BlockStateProperties.FACING, context.getClickedFace());
    }
}
