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
        WEST = Block.makeCuboidShape(0, 0, 0, thickness, thickness, 16);
        EAST = Block.makeCuboidShape(16 - thickness, 0, 0,16, thickness, 16);
        SOUTH = Block.makeCuboidShape(0, 0,16 - thickness, 16, thickness, 16);
        NORTH = Block.makeCuboidShape(0, 0, 0, 16, thickness, thickness);

        SOUTH_TOP = Block.makeCuboidShape(0, 16 - thickness,16 - thickness, 16, 16, 16);
        NORTH_TOP = Block.makeCuboidShape(0, 16 - thickness, 0, 16, 16, thickness);
        EAST_TOP = Block.makeCuboidShape(16 - thickness, 16 - thickness, 0,16, 16, 16);
        WEST_TOP = Block.makeCuboidShape(0, 16 - thickness, 0, thickness, 16, 16);
    }

    public VoxelShape NORTH;
    public VoxelShape SOUTH;
    public VoxelShape WEST;
    public VoxelShape EAST;
    public VoxelShape NORTH_TOP;
    public VoxelShape SOUTH_TOP;
    public VoxelShape WEST_TOP;
    public VoxelShape EAST_TOP;
    public static final BooleanProperty NORTH_ADJ = BooleanProperty.create("north_adj");
    public static final BooleanProperty SOUTH_ADJ = BooleanProperty.create("south_adj");
    public static final BooleanProperty EAST_ADJ = BooleanProperty.create("east_adj");
    public static final BooleanProperty WEST_ADJ = BooleanProperty.create("west_adj");
    public static final BooleanProperty PLACED_TOP = BooleanProperty.create("placed_top");
    public static final IntegerProperty FACING_INDEX = IntegerProperty.create("facing_index", 0, 3);

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, FACING_INDEX, PLACED_TOP, NORTH_ADJ, SOUTH_ADJ, EAST_ADJ, WEST_ADJ);
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
        boolean waterlogged = worldIn.getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        return getDefaultState().with(BlockStateProperties.WATERLOGGED, waterlogged)
            .with(FACING_INDEX, context.getPlacementHorizontalFacing().getHorizontalIndex())
            .with(NORTH_ADJ, checkSides(context, Direction.NORTH))
            .with(SOUTH_ADJ, checkSides(context, Direction.SOUTH))
            .with(EAST_ADJ, checkSides(context, Direction.EAST))
            .with(WEST_ADJ, checkSides(context, Direction.WEST))
            .with(PLACED_TOP, direction == Direction.DOWN || (direction != Direction.UP && context.getHitVec().y - (double) pos.getY() > 0.5D));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader worldIn, @Nonnull BlockPos blockPos, @Nonnull ISelectionContext ctx) {
        VoxelShape directionShape;
        int facing = blockState.get(FACING_INDEX);
        //get Index of this horizontal facing (order is S-W-N-E, 0-3)
        switch (facing) {
            case 0:
                directionShape = SOUTH;
                if(blockState.get(NORTH_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, NORTH);
                }
                if(blockState.get(EAST_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, EAST);
                }
                if(blockState.get(WEST_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, WEST);
                }
                if(blockState.get(PLACED_TOP)) {
                    directionShape = SOUTH_TOP;
                    if(blockState.get(NORTH_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, NORTH_TOP);
                    }
                    if(blockState.get(EAST_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, EAST_TOP);
                    }
                    if(blockState.get(WEST_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, WEST_TOP);
                    }
                }
                break;
            default:
            case 1:
                directionShape = WEST;
                if(blockState.get(NORTH_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, NORTH);
                }
                if(blockState.get(SOUTH_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, SOUTH);
                }
                if(blockState.get(EAST_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, EAST);
                }
                if(blockState.get(PLACED_TOP)) {
                    directionShape = WEST_TOP;
                    if(blockState.get(NORTH_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, NORTH_TOP);
                    }
                    if(blockState.get(SOUTH_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, SOUTH_TOP);
                    }
                    if(blockState.get(EAST_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, EAST_TOP);
                    }
                }
                break;
            case 2:
                directionShape = NORTH;
                if(blockState.get(SOUTH_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, SOUTH);
                }
                if(blockState.get(EAST_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, EAST);
                }
                if(blockState.get(WEST_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, WEST);
                }
                if(blockState.get(PLACED_TOP)) {
                    directionShape = NORTH_TOP;
                    if(blockState.get(SOUTH_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, SOUTH_TOP);
                    }
                    if(blockState.get(EAST_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, EAST_TOP);
                    }
                    if(blockState.get(WEST_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, WEST_TOP);
                    }
                }
                break;
            case 3:
                directionShape = EAST;
                if(blockState.get(NORTH_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, NORTH);
                }
                if(blockState.get(SOUTH_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, SOUTH);
                }
                if(blockState.get(WEST_ADJ)) {
                    directionShape = VoxelShapes.or(directionShape, WEST);
                }
                if(blockState.get(PLACED_TOP)) {
                    directionShape = EAST_TOP;
                    if(blockState.get(NORTH_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, NORTH_TOP);
                    }
                    if(blockState.get(SOUTH_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, SOUTH_TOP);
                    }
                    if(blockState.get(WEST_ADJ)) {
                        directionShape = VoxelShapes.or(directionShape, WEST_TOP);
                    }
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
