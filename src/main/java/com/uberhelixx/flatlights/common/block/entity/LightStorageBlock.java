package com.uberhelixx.flatlights.common.block.entity;

import com.uberhelixx.flatlights.common.blockentity.LightStorageBE;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class LightStorageBlock extends BaseEntityBlock {
    public static final float DESTROY_TIME = 1.0f;
    public static final float EXPLOSION_RESISTANCE = 100000000f;
    public LightStorageBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(DESTROY_TIME, EXPLOSION_RESISTANCE)
                .noOcclusion()
                .sound(SoundType.STONE));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof LightStorageBE) {
                ((LightStorageBE) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
    
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof LightStorageBE && !pPlayer.isCrouching()) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (LightStorageBE)entity, pPos);
                ((LightStorageBE) entity).playSound(SoundEvents.ENDER_CHEST_OPEN);
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
        return new LightStorageBE(blockPos, blockState);
    }
}
