package com.uberhelixx.flatlights.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public class HorizontalEdgeBlock extends Block implements IWaterLoggable {
    private final double thickness;
    static final float BLOCK_HARDNESS = 0.4f;
    static final float BLOCK_RESISTANCE = 100000000f;
    public static ToIntFunction<BlockState> EDGE_BLOCK_LIGHT_LEVEL = BlockState -> 15;

    public HorizontalEdgeBlock(double thickness) {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(EDGE_BLOCK_LIGHT_LEVEL)
                .notSolid()
                .setOpaque(HorizontalEdgeBlock::isntSolid)
                .sound(SoundType.GLASS));
        this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false));

        //make shape based off of direction from RotatingBlock
        this.thickness = thickness;
        EAST = Block.makeCuboidShape(0, 0, 0, thickness, thickness, 16);
        WEST = Block.makeCuboidShape(16 - thickness, 0, 0,16, thickness, 16);
        NORTH = Block.makeCuboidShape(0, 0,16 - thickness, 16, thickness, 16);
        SOUTH = Block.makeCuboidShape(0, 0, 0, 16, thickness, thickness);

        NORTH_TOP = Block.makeCuboidShape(0, 16 - thickness,16 - thickness, 16, 16, 16);
        SOUTH_TOP = Block.makeCuboidShape(0, 16 - thickness, 0, 16, 16, thickness);
        WEST_TOP = Block.makeCuboidShape(16 - thickness, 16 - thickness, 0,16, 16, 16);
        EAST_TOP = Block.makeCuboidShape(0, 16 - thickness, 0, thickness, 16, 16);
    }

    public VoxelShape NORTH;
    public VoxelShape SOUTH;
    public VoxelShape WEST;
    public VoxelShape EAST;
    public VoxelShape NORTH_TOP;
    public VoxelShape SOUTH_TOP;
    public VoxelShape WEST_TOP;
    public VoxelShape EAST_TOP;

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED, BlockStateProperties.HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos().offset(context.getNearestLookingDirection().getOpposite());
        Direction direction = context.getFace();
        boolean waterlogged = worldIn.getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        if (context.getPlayer() != null && worldIn.getBlockState(pos).getBlock() instanceof HorizontalEdgeBlock && !context.getPlayer().isCrouching())
            return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(BlockStateProperties.HORIZONTAL_FACING, worldIn.getBlockState(pos).get(BlockStateProperties.HORIZONTAL_FACING))
                    .with(BlockStateProperties.HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double)pos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP);
        else
            return getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite())
                    .with(BlockStateProperties.HALF, Half.BOTTOM).with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(BlockStateProperties.HALF, direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double)pos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader worldIn, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext ctx) {
        VoxelShape directionShape;
        Direction facing = blockState.get(BlockStateProperties.HORIZONTAL_FACING);
        //get Index of this horizontal facing (order is S-W-N-E, 0-3)
        switch (facing.getHorizontalIndex()) {
            case 0:
                directionShape = SOUTH;
                if(blockState.get(BlockStateProperties.HALF) == Half.TOP) {
                    directionShape = SOUTH_TOP;
                }
                break;
            default:
            case 1:
                directionShape = WEST;
                if(blockState.get(BlockStateProperties.HALF) == Half.TOP) {
                    directionShape = WEST_TOP;
                }
                break;
            case 2:
                directionShape = NORTH;
                if(blockState.get(BlockStateProperties.HALF) == Half.TOP) {
                    directionShape = NORTH_TOP;
                }
                break;
            case 3:
                directionShape = EAST;
                if(blockState.get(BlockStateProperties.HALF) == Half.TOP) {
                    directionShape = EAST_TOP;
                }
                break;
        }
        return directionShape;
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
