package com.uberhelixx.flatlights.common.block.blackout;

import com.uberhelixx.flatlights.common.block.lights.SlabLightBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BlackoutPillarLightBlock extends BlackoutRotatingBlock {
    private final double thickness;

    public BlackoutPillarLightBlock(double thickness) {
        super();

        //make shape based off of direction from RotatingBlock
        this.thickness = thickness;
        double initialWidth = 6;
        double totalWidth = initialWidth + thickness;
        UP = Block.makeCuboidShape(initialWidth,0, initialWidth, totalWidth, 16, totalWidth);
        DOWN = Block.makeCuboidShape(initialWidth,0, initialWidth, totalWidth, 16, totalWidth);
        EAST = Block.makeCuboidShape(0, initialWidth, initialWidth, 16, totalWidth, totalWidth);
        WEST = Block.makeCuboidShape(0, initialWidth, initialWidth, 16, totalWidth, totalWidth);
        NORTH = Block.makeCuboidShape(initialWidth, initialWidth,0, totalWidth, totalWidth,16);
        SOUTH = Block.makeCuboidShape(initialWidth, initialWidth,0, totalWidth, totalWidth,16);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos().offset(context.getFace().getOpposite());
        boolean waterlogged = worldIn.getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        if (context.getPlayer() != null && worldIn.getBlockState(pos).getBlock() instanceof SlabLightBlock && !context.getPlayer().isCrouching())
            return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(BlockStateProperties.FACING, worldIn.getBlockState(pos).get(BlockStateProperties.FACING));
        else
            return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(BlockStateProperties.FACING, context.getFace());
    }
}
