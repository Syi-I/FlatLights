package com.uberhelixx.flatlights.common.block.light;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.ToIntFunction;

public class VerticalEdgeBlock extends Block implements SimpleWaterloggedBlock {
    private final double thickness;
    static final float DESTROY_TIME = 0.4f;
    static final float EXPLOSION_RESISTANCE = 100000000f;
    
    public VerticalEdgeBlock(ToIntFunction<BlockState> lightLevel, double thickness) {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS)
                .noOcclusion()
                .emissiveRendering(VerticalEdgeBlock::isEmissive)
                .strength(DESTROY_TIME, EXPLOSION_RESISTANCE)
                .lightLevel(lightLevel)
                .sound(SoundType.GLASS));
        
        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
        
        //make shape based off of direction from RotatingBlock
        this.thickness = thickness;
        
        WEST = Block.box(0, 0, 0, thickness, thickness, 16);
        EAST = Block.box(16 - thickness, 0, 0,16, thickness, 16);
        SOUTH = Block.box(0, 0,16 - thickness, 16, thickness, 16);
        NORTH = Block.box(0, 0, 0, 16, thickness, thickness);
        
        SOUTH_TOP = Block.box(0, 16 - thickness,16 - thickness, 16, 16, 16);
        NORTH_TOP = Block.box(0, 16 - thickness, 0, 16, 16, thickness);
        EAST_TOP = Block.box(16 - thickness, 16 - thickness, 0,16, 16, 16);
        WEST_TOP = Block.box(0, 16 - thickness, 0, thickness, 16, 16);
        
        Q1 = Block.box(0, 0, 0, thickness, 16, thickness);
        Q2 = Block.box(16 - thickness, 0, 0, 16, 16, thickness);
        Q3 = Block.box(16 - thickness, 0, 16 - thickness, 16, 16, 16);
        Q4 = Block.box(0, 0, 16 - thickness, thickness, 16, 16);
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
    
    private static boolean isEmissive(BlockState state, BlockGetter getter, BlockPos pos) {return true;}
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if(FlatLightsCommonConfig.entityDamageableBlocks.get()) {
            Component mobDamageableTooltip = Component.literal("Mob Destructible: ").append("TRUE").withStyle(ChatFormatting.RED);
            pTooltip.add(mobDamageableTooltip);
        }
        
        Component tooltipTextComp = Component.literal("Follows along the side of solid blocks.").withStyle(ChatFormatting.GRAY);
        pTooltip.add(tooltipTextComp);
        
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
    
    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return FlatLightsCommonConfig.entityDamageableBlocks.get();
    }
    
    private boolean checkSides(BlockPlaceContext context, Direction direction) {
        BlockPos pos = context.getClickedPos().relative(direction);
        return canSupportCenter(context.getLevel(), pos, direction.getOpposite());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED, FACING_INDEX, UP_ADJ, DOWN_ADJ, LEFT_ADJ, RIGHT_ADJ);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level levelIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        int cardinalFacing = context.getHorizontalDirection().get2DDataValue();
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
        boolean waterlogged = levelIn.getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        //sneaking makes you ignore adjacent blocks so no connecting to edges
        if (context.getPlayer() != null && levelIn.getBlockState(pos).getBlock() instanceof VerticalEdgeBlock && context.getPlayer().isCrouching()) {
            return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                    .setValue(FACING_INDEX, context.getHorizontalDirection().get2DDataValue())
                    .setValue(UP_ADJ, false)
                    .setValue(DOWN_ADJ, false)
                    .setValue(LEFT_ADJ, false)
                    .setValue(RIGHT_ADJ, false);
        }
        return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                .setValue(FACING_INDEX, context.getHorizontalDirection().get2DDataValue())
                .setValue(UP_ADJ, checkSides(context, Direction.UP))
                .setValue(DOWN_ADJ, checkSides(context, Direction.DOWN))
                .setValue(LEFT_ADJ, checkSides(context, leftAdj))
                .setValue(RIGHT_ADJ, checkSides(context, rightAdj));
    }
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape directionShape;
        int facing = pState.getValue(FACING_INDEX);
        boolean mainSide = true;
        //get Index of this horizontal facing (order is S-W-N-E, 0-3)
        switch (facing) {
            case 0:
                //SOUTH
                if(!pState.getValue(LEFT_ADJ) && pState.getValue(RIGHT_ADJ)) {
                    directionShape = Q4;
                    mainSide = false;
                }
                else {
                    directionShape = Q3;
                }
                if(pState.getValue(DOWN_ADJ)) {
                    directionShape = Shapes.or(directionShape, SOUTH);
                }
                if(pState.getValue(UP_ADJ)) {
                    directionShape = Shapes.or(directionShape, SOUTH_TOP);
                }
                if(pState.getValue(RIGHT_ADJ) && mainSide) {
                    directionShape = Shapes.or(directionShape, Q4);
                }
                if(pState.getValue(UP_ADJ) && pState.getValue(DOWN_ADJ) && !pState.getValue(LEFT_ADJ) && !pState.getValue(RIGHT_ADJ)) {
                    directionShape = SOUTH;
                    directionShape = Shapes.or(directionShape, SOUTH_TOP);
                }
                break;
            default:
            case 1:
                //WEST
                if(!pState.getValue(LEFT_ADJ) && pState.getValue(RIGHT_ADJ)) {
                    directionShape = Q1;
                    mainSide = false;
                }
                else {
                    directionShape = Q4;
                }
                if(pState.getValue(DOWN_ADJ)) {
                    directionShape = Shapes.or(directionShape, WEST);
                }
                if(pState.getValue(UP_ADJ)) {
                    directionShape = Shapes.or(directionShape, WEST_TOP);
                }
                if(pState.getValue(RIGHT_ADJ) && mainSide) {
                    directionShape = Shapes.or(directionShape, Q1);
                }
                if(pState.getValue(UP_ADJ) && pState.getValue(DOWN_ADJ) && !pState.getValue(LEFT_ADJ) && !pState.getValue(RIGHT_ADJ)) {
                    directionShape = WEST;
                    directionShape = Shapes.or(directionShape, WEST_TOP);
                }
                break;
            case 2:
                //NORTH
                if(!pState.getValue(LEFT_ADJ) && pState.getValue(RIGHT_ADJ)) {
                    directionShape = Q2;
                    mainSide = false;
                }
                else {
                    directionShape = Q1;
                }
                if(pState.getValue(DOWN_ADJ)) {
                    directionShape = Shapes.or(directionShape, NORTH);
                }
                if(pState.getValue(UP_ADJ)) {
                    directionShape = Shapes.or(directionShape, NORTH_TOP);
                }
                if(pState.getValue(RIGHT_ADJ) && mainSide) {
                    directionShape = Shapes.or(directionShape, Q2);
                }
                if(pState.getValue(UP_ADJ) && pState.getValue(DOWN_ADJ) && !pState.getValue(LEFT_ADJ) && !pState.getValue(RIGHT_ADJ)) {
                    directionShape = NORTH;
                    directionShape = Shapes.or(directionShape, NORTH_TOP);
                }
                break;
            case 3:
                //EAST
                if(!pState.getValue(LEFT_ADJ) && pState.getValue(RIGHT_ADJ)) {
                    directionShape = Q3;
                    mainSide = false;
                }
                else {
                    directionShape = Q2;
                }
                if(pState.getValue(DOWN_ADJ)) {
                    directionShape = Shapes.or(directionShape, EAST);
                }
                if(pState.getValue(UP_ADJ)) {
                    directionShape = Shapes.or(directionShape, EAST_TOP);
                }
                if(pState.getValue(RIGHT_ADJ) && mainSide) {
                    directionShape = Shapes.or(directionShape, Q3);
                }
                if(pState.getValue(UP_ADJ) && pState.getValue(DOWN_ADJ) && !pState.getValue(LEFT_ADJ) && !pState.getValue(RIGHT_ADJ)) {
                    directionShape = EAST;
                    directionShape = Shapes.or(directionShape, EAST_TOP);
                }
                break;
        }
        return directionShape;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }
    
    @Override
    public boolean canPlaceLiquid(@Nonnull BlockGetter pLevel, @Nonnull BlockPos pPos, @Nonnull BlockState pState, @Nonnull Fluid pFluid) {
        return true;
    }
    
    @Nonnull
    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }
}
