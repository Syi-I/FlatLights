package com.uberhelixx.flatlights.block.blackout;

import com.uberhelixx.flatlights.block.lights.RotatingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlackoutSlabLightBlock extends BlackoutRotatingBlock {
    private final double thickness;

    public BlackoutSlabLightBlock(double thickness) {
        super();

        //make shape based off of direction from RotatingBlock
        this.thickness = thickness;
        UP = Block.makeCuboidShape(0,0,0, 16, thickness,16);
        DOWN = Block.makeCuboidShape(0,16 - thickness,0, 16,16,16);
        EAST = Block.makeCuboidShape(0,0,0, thickness,16,16);
        WEST = Block.makeCuboidShape(16 - thickness,0,0, 16,16,16);
        NORTH = Block.makeCuboidShape(0,0,16 - thickness, 16,16,16);
        SOUTH = Block.makeCuboidShape(0,0,0, 16,16, thickness);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos().offset(context.getFace().getOpposite());
        boolean waterlogged = worldIn.getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        if (context.getPlayer() != null && worldIn.getBlockState(pos).getBlock() instanceof BlackoutSlabLightBlock && !context.getPlayer().isCrouching())
            return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(BlockStateProperties.FACING, worldIn.getBlockState(pos).get(BlockStateProperties.FACING));
        else
            return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(BlockStateProperties.FACING, context.getFace());
    }
}
