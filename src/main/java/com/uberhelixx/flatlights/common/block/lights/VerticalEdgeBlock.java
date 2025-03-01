package com.uberhelixx.flatlights.block.lights;

import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

public class VerticalEdgeBlock extends Block implements IWaterLoggable {
    private final double thickness;
    static final float BLOCK_HARDNESS = 0.4f;
    static final float BLOCK_RESISTANCE = 100000000f;
    public static ToIntFunction<BlockState> EDGE_BLOCK_LIGHT_LEVEL = BlockState -> 15;

    public VerticalEdgeBlock(double thickness) {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(EDGE_BLOCK_LIGHT_LEVEL)
                .notSolid()
                .setOpaque(VerticalEdgeBlock::isntSolid)
                .sound(SoundType.GLASS));
        this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.WATERLOGGED, false));

        this.thickness = thickness;
        WEST = Block.makeCuboidShape(0, 0, 0, thickness, thickness, 16);
        EAST = Block.makeCuboidShape(16 - thickness, 0, 0,16, thickness, 16);
        SOUTH = Block.makeCuboidShape(0, 0,16 - thickness, 16, thickness, 16);
        NORTH = Block.makeCuboidShape(0, 0, 0, 16, thickness, thickness);

        SOUTH_TOP = Block.makeCuboidShape(0, 16 - thickness,16 - thickness, 16, 16, 16);
        NORTH_TOP = Block.makeCuboidShape(0, 16 - thickness, 0, 16, 16, thickness);
        EAST_TOP = Block.makeCuboidShape(16 - thickness, 16 - thickness, 0,16, 16, 16);
        WEST_TOP = Block.makeCuboidShape(0, 16 - thickness, 0, thickness, 16, 16);

        Q1 = Block.makeCuboidShape(0, 0, 0, thickness, 16, thickness);
        Q2 = Block.makeCuboidShape(16 - thickness, 0, 0, 16, 16, thickness);
        Q3 = Block.makeCuboidShape(16 - thickness, 0, 16 - thickness, 16, 16, 16);
        Q4 = Block.makeCuboidShape(0, 0, 16 - thickness, thickness, 16, 16);
    }

    public VoxelShape NORTH;
    public VoxelShape SOUTH;
    public VoxelShape WEST;
    public VoxelShape EAST;
    public VoxelShape NORTH_TOP;
    public VoxelShape SOUTH_TOP;
    public VoxelShape WEST_TOP;
    public VoxelShape EAST_TOP;
    //vertical voxel shapes, Q1-Q4 start at top left corner and go clockwise
    public VoxelShape Q1;
    public VoxelShape Q2;
    public VoxelShape Q3;
    public VoxelShape Q4;
    public static final BooleanProperty UP_ADJ = BooleanProperty.create("up_adj");
    public static final BooleanProperty DOWN_ADJ = BooleanProperty.create("down_adj");
    public static final BooleanProperty LEFT_ADJ = BooleanProperty.create("left_adj");
    public static final BooleanProperty RIGHT_ADJ = BooleanProperty.create("right_adj");
    public static final IntegerProperty FACING_INDEX = IntegerProperty.create("facing_index", 0, 3);

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, FACING_INDEX, UP_ADJ, DOWN_ADJ, LEFT_ADJ, RIGHT_ADJ);
    }

    private boolean checkSides(BlockItemUseContext context, Direction direction) {
        BlockPos pos = context.getPos().offset(direction);
        return hasEnoughSolidSide(context.getWorld(), pos, direction.getOpposite());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        Direction direction = context.getFace();
        int cardinalFacing = context.getPlacementHorizontalFacing().getHorizontalIndex();
        Direction leftAdj = Direction.WEST;
        Direction rightAdj = Direction.EAST;
        //get Index of this horizontal facing (order is S-W-N-E, 0-3)
        switch (cardinalFacing) {
            case 0:
                //facing SOUTH
                leftAdj = Direction.EAST;
                rightAdj = Direction.WEST;
                break;
            case 1:
                //facing WEST
                leftAdj = Direction.SOUTH;
                rightAdj = Direction.NORTH;
                break;
            default:
            case 2:
                //facing NORTH (default direction)
                break;
            case 3:
                //facing EAST
                leftAdj = Direction.NORTH;
                rightAdj = Direction.SOUTH;
                break;
        }
        boolean waterlogged = worldIn.getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        //sneaking makes you ignore adjacent blocks so no connecting to edges
        if (context.getPlayer() != null && worldIn.getBlockState(pos).getBlock() instanceof VerticalEdgeBlock && context.getPlayer().isCrouching()) {
            return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
                    .with(FACING_INDEX, context.getPlacementHorizontalFacing().getHorizontalIndex())
                    .with(UP_ADJ, false)
                    .with(DOWN_ADJ, false)
                    .with(LEFT_ADJ, false)
                    .with(RIGHT_ADJ, false);
        }
        return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
            .with(FACING_INDEX, context.getPlacementHorizontalFacing().getHorizontalIndex())
            .with(UP_ADJ, checkSides(context, Direction.UP))
            .with(DOWN_ADJ, checkSides(context, Direction.DOWN))
            .with(LEFT_ADJ, checkSides(context, leftAdj))
            .with(RIGHT_ADJ, checkSides(context, rightAdj));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader worldIn, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext ctx) {
        VoxelShape directionShape;
        int facing = blockState.get(FACING_INDEX);
        boolean mainSide = true;
        //get Index of this horizontal facing (order is S-W-N-E, 0-3)
        switch (facing) {
            case 0:
                //SOUTH
                if(!blockState.get(LEFT_ADJ) && blockState.get(RIGHT_ADJ)) {
                    directionShape = Q4;
                    mainSide = false;
                }
                else {
                    directionShape = Q3;
                }
                if(blockState.get(DOWN_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, SOUTH);
                }
                if(blockState.get(UP_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, SOUTH_TOP);
                }
                if(blockState.get(RIGHT_ADJ) && mainSide) {
                    directionShape = VoxelShapes.or(directionShape, Q4);
                }
                if(blockState.get(UP_ADJ) && blockState.get(DOWN_ADJ) && !blockState.get(LEFT_ADJ) && !blockState.get(RIGHT_ADJ)) {
                    directionShape = SOUTH;
                    directionShape = VoxelShapes.or(directionShape, SOUTH_TOP);
                }
                break;
            default:
            case 1:
                //WEST
                if(!blockState.get(LEFT_ADJ) && blockState.get(RIGHT_ADJ)) {
                    directionShape = Q1;
                    mainSide = false;
                }
                else {
                    directionShape = Q4;
                }
                if(blockState.get(DOWN_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, WEST);
                }
                if(blockState.get(UP_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, WEST_TOP);
                }
                if(blockState.get(RIGHT_ADJ) && mainSide) {
                    directionShape = VoxelShapes.or(directionShape, Q1);
                }
                if(blockState.get(UP_ADJ) && blockState.get(DOWN_ADJ) && !blockState.get(LEFT_ADJ) && !blockState.get(RIGHT_ADJ)) {
                    directionShape = WEST;
                    directionShape = VoxelShapes.or(directionShape, WEST_TOP);
                }
                break;
            case 2:
                //NORTH
                if(!blockState.get(LEFT_ADJ) && blockState.get(RIGHT_ADJ)) {
                    directionShape = Q2;
                    mainSide = false;
                }
                else {
                    directionShape = Q1;
                }
                if(blockState.get(DOWN_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, NORTH);
                }
                if(blockState.get(UP_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, NORTH_TOP);
                }
                if(blockState.get(RIGHT_ADJ) && mainSide) {
                    directionShape = VoxelShapes.or(directionShape, Q2);
                }
                if(blockState.get(UP_ADJ) && blockState.get(DOWN_ADJ) && !blockState.get(LEFT_ADJ) && !blockState.get(RIGHT_ADJ)) {
                    directionShape = NORTH;
                    directionShape = VoxelShapes.or(directionShape, NORTH_TOP);
                }
                break;
            case 3:
                //EAST
                if(!blockState.get(LEFT_ADJ) && blockState.get(RIGHT_ADJ)) {
                    directionShape = Q3;
                    mainSide = false;
                }
                else {
                    directionShape = Q2;
                }
                if(blockState.get(DOWN_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, EAST);
                }
                if(blockState.get(UP_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, EAST_TOP);
                }
                if(blockState.get(RIGHT_ADJ) && mainSide) {
                    directionShape = VoxelShapes.or(directionShape, Q3);
                }
                if(blockState.get(UP_ADJ) && blockState.get(DOWN_ADJ) && !blockState.get(LEFT_ADJ) && !blockState.get(RIGHT_ADJ)) {
                    directionShape = EAST;
                    directionShape = VoxelShapes.or(directionShape, EAST_TOP);
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String tooltipText = "" + MiscHelpers.coloredText(TextFormatting.GRAY, "Follows along the side of solid blocks.");
        ITextComponent tooltipComponent = ITextComponent.getTextComponentOrEmpty(tooltipText);
        tooltip.add(tooltipComponent);

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
