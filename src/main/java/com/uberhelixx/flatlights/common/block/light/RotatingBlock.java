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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.ToIntFunction;

public class RotatingBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
    
    static final float DESTROY_TIME = 0.4f;
    static final float EXPLOSION_RESISTANCE = 100000000f;
    
    protected RotatingBlock(ToIntFunction<BlockState> lightLevel) {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS)
                .noOcclusion()
                .emissiveRendering(RotatingBlock::isTrue)
                .isSuffocating(RotatingBlock::isFalse)
                .strength(DESTROY_TIME, EXPLOSION_RESISTANCE)
                .lightLevel(lightLevel)
                .sound(SoundType.GLASS));
        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
    }
    
    private static boolean isTrue(BlockState state, BlockGetter getter, BlockPos pos) {return true;}
    private static boolean isFalse(BlockState state, BlockGetter getter, BlockPos pos) {return false;}
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if(FlatLightsCommonConfig.entityDamageableBlocks.get()) {
            Component mobDamageableTooltip = Component.literal("Mob Destructible: ").append("TRUE").withStyle(ChatFormatting.RED);
            pTooltip.add(mobDamageableTooltip);
        }
        
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
    
    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return FlatLightsCommonConfig.entityDamageableBlocks.get();
    }
    
    public VoxelShape DOWN;
    public VoxelShape UP;
    public VoxelShape NORTH;
    public VoxelShape SOUTH;
    public VoxelShape WEST;
    public VoxelShape EAST;
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape directionShape;
        Direction facing = pState.getValue(BlockStateProperties.FACING);
        //get Index of this facing (order is D-U-N-S-W-E, 0-5)
        switch (facing.ordinal()) {
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
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, BlockStateProperties.WATERLOGGED);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(BlockStateProperties.WATERLOGGED, waterlogged);
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
