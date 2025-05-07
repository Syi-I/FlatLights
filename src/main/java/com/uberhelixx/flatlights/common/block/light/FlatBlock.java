package com.uberhelixx.flatlights.common.block.light;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class FlatBlock extends Block {
    //constants for block hardness (time it takes to mine the block) and resistance (what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float DESTROY_TIME = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness?
    static final float EXPLOSION_RESISTANCE = 100000000f;
    
    public FlatBlock(ToIntFunction<BlockState> lightLevel) {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS)
                .noOcclusion()
                .emissiveRendering(FlatBlock::isEmissive)
                .strength(DESTROY_TIME, EXPLOSION_RESISTANCE)
                .lightLevel(lightLevel)
                .sound(SoundType.GLASS));
    }
    
    private static boolean isEmissive(BlockState state, BlockGetter getter, BlockPos pos) {return true;}
    
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
}
