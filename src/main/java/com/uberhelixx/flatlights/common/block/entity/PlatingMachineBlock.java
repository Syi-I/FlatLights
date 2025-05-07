package com.uberhelixx.flatlights.common.block.entity;

import com.uberhelixx.flatlights.common.blockentity.ModBlockEntities;
import com.uberhelixx.flatlights.common.blockentity.PlatingMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class PlatingMachineBlock extends RotatableEntityBlock {
    public static final float DESTROY_TIME = 1.0f;
    public static final float EXPLOSION_RESISTANCE = 100000000f;
    public PlatingMachineBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                .strength(DESTROY_TIME, EXPLOSION_RESISTANCE)
                .noOcclusion()
                .sound(SoundType.NETHERITE_BLOCK));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof PlatingMachineBE) {
                ((PlatingMachineBE) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
    
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof PlatingMachineBE && !pPlayer.isCrouching()) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (PlatingMachineBE)entity, pPos);
            }
            else {
                throw new IllegalStateException("Container provider missing :skull:");
            }
        }
        
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PlatingMachineBE(blockPos, blockState);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }
        
        return createTickerHelper(pBlockEntityType, ModBlockEntities.PLATING_MACHINE_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}
