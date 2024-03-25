package com.uberhelixx.flatlights.block.lights;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.ToIntFunction;

public class RotatingBlock extends DirectionalBlock implements IWaterLoggable {

    static final float BLOCK_HARDNESS = 0.4f;
    static final float BLOCK_RESISTANCE = 100000000f;
    public static ToIntFunction<BlockState> ROTATING_BLOCK_LIGHT_LEVEL = BlockState -> 15;

    public RotatingBlock() {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(ROTATING_BLOCK_LIGHT_LEVEL)
                .notSolid()
                .setOpaque(RotatingBlock::isntSolid)
                .sound(SoundType.GLASS));
        this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false));
    }

    public VoxelShape DOWN;
    public VoxelShape UP;
    public VoxelShape NORTH;
    public VoxelShape SOUTH;
    public VoxelShape WEST;
    public VoxelShape EAST;

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader worldIn, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext ctx) {
        VoxelShape directionShape;
        Direction facing = blockState.get(BlockStateProperties.FACING);
        //get Index of this facing (order is D-U-N-S-W-E, 0-5)
        switch (facing.getIndex()) {
            case 0:
                directionShape = DOWN;
                break;
            default:
            case 1:
                directionShape = UP;
                break;
            case 2:
                directionShape = NORTH;
                break;
            case 3:
                directionShape = SOUTH;
                break;
            case 4:
                directionShape = WEST;
                break;
            case 5:
                directionShape = EAST;
                break;
        }
        return directionShape;
    }


    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, BlockStateProperties.WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean waterlogged = context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) { return 1.0F; }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) { return true; }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }
}
