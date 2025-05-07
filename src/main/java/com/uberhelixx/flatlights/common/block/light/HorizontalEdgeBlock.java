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

public class HorizontalEdgeBlock extends Block implements SimpleWaterloggedBlock {
    private final double thickness;
    static final float DESTROY_TIME = 0.4f;
    static final float EXPLOSION_RESISTANCE = 100000000f;
    
    public HorizontalEdgeBlock(ToIntFunction<BlockState> lightLevel, double thickness) {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS)
                .noOcclusion()
                .emissiveRendering(HorizontalEdgeBlock::isEmissive)
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
        pBuilder.add(BlockStateProperties.WATERLOGGED, FACING_INDEX, PLACED_TOP, NORTH_ADJ, SOUTH_ADJ, EAST_ADJ, WEST_ADJ);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        boolean waterlogged = worldIn.getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, waterlogged)
                .setValue(FACING_INDEX, context.getHorizontalDirection().get2DDataValue())
                .setValue(NORTH_ADJ, checkSides(context, Direction.NORTH))
                .setValue(SOUTH_ADJ, checkSides(context, Direction.SOUTH))
                .setValue(EAST_ADJ, checkSides(context, Direction.EAST))
                .setValue(WEST_ADJ, checkSides(context, Direction.WEST))
                .setValue(PLACED_TOP, direction == Direction.DOWN || (direction != Direction.UP && context.getClickLocation().y - (double) pos.getY() > 0.5D));
    }
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape directionShape;
        int facing = pState.getValue(FACING_INDEX);
        //get Index of this horizontal facing (order is S-W-N-E, 0-3)
        switch (facing) {
            case 0:
                directionShape = SOUTH;
                if(pState.getValue(NORTH_ADJ)) {
                    directionShape = Shapes.or(directionShape, NORTH);
                }
                if(pState.getValue(EAST_ADJ)) {
                    directionShape = Shapes.or(directionShape, EAST);
                }
                if(pState.getValue(WEST_ADJ)) {
                    directionShape = Shapes.or(directionShape, WEST);
                }
                if(pState.getValue(PLACED_TOP)) {
                    directionShape = SOUTH_TOP;
                    if(pState.getValue(NORTH_ADJ)) {
                        directionShape = Shapes.or(directionShape, NORTH_TOP);
                    }
                    if(pState.getValue(EAST_ADJ)) {
                        directionShape = Shapes.or(directionShape, EAST_TOP);
                    }
                    if(pState.getValue(WEST_ADJ)) {
                        directionShape = Shapes.or(directionShape, WEST_TOP);
                    }
                }
                break;
            default:
            case 1:
                directionShape = WEST;
                if(pState.getValue(NORTH_ADJ)) {
                    directionShape = Shapes.or(directionShape, NORTH);
                }
                if(pState.getValue(SOUTH_ADJ)) {
                    directionShape = Shapes.or(directionShape, SOUTH);
                }
                if(pState.getValue(EAST_ADJ)) {
                    directionShape = Shapes.or(directionShape, EAST);
                }
                if(pState.getValue(PLACED_TOP)) {
                    directionShape = WEST_TOP;
                    if(pState.getValue(NORTH_ADJ)) {
                        directionShape = Shapes.or(directionShape, NORTH_TOP);
                    }
                    if(pState.getValue(SOUTH_ADJ)) {
                        directionShape = Shapes.or(directionShape, SOUTH_TOP);
                    }
                    if(pState.getValue(EAST_ADJ)) {
                        directionShape = Shapes.or(directionShape, EAST_TOP);
                    }
                }
                break;
            case 2:
                directionShape = NORTH;
                if(pState.getValue(SOUTH_ADJ)) {
                    directionShape = Shapes.or(directionShape, SOUTH);
                }
                if(pState.getValue(EAST_ADJ)) {
                    directionShape = Shapes.or(directionShape, EAST);
                }
                if(pState.getValue(WEST_ADJ)) {
                    directionShape = Shapes.or(directionShape, WEST);
                }
                if(pState.getValue(PLACED_TOP)) {
                    directionShape = NORTH_TOP;
                    if(pState.getValue(SOUTH_ADJ)) {
                        directionShape = Shapes.or(directionShape, SOUTH_TOP);
                    }
                    if(pState.getValue(EAST_ADJ)) {
                        directionShape = Shapes.or(directionShape, EAST_TOP);
                    }
                    if(pState.getValue(WEST_ADJ)) {
                        directionShape = Shapes.or(directionShape, WEST_TOP);
                    }
                }
                break;
            case 3:
                directionShape = EAST;
                if(pState.getValue(NORTH_ADJ)) {
                    directionShape = Shapes.or(directionShape, NORTH);
                }
                if(pState.getValue(SOUTH_ADJ)) {
                    directionShape = Shapes.or(directionShape, SOUTH);
                }
                if(pState.getValue(WEST_ADJ)) {
                    directionShape = Shapes.or(directionShape, WEST);
                }
                if(pState.getValue(PLACED_TOP)) {
                    directionShape = EAST_TOP;
                    if(pState.getValue(NORTH_ADJ)) {
                        directionShape = Shapes.or(directionShape, NORTH_TOP);
                    }
                    if(pState.getValue(SOUTH_ADJ)) {
                        directionShape = Shapes.or(directionShape, SOUTH_TOP);
                    }
                    if(pState.getValue(WEST_ADJ)) {
                        directionShape = Shapes.or(directionShape, WEST_TOP);
                    }
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
