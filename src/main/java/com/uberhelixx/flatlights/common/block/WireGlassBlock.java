package com.uberhelixx.flatlights.common.block;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WireGlassBlock extends GlassBlock {
    static final float DESTROY_TIME = 0.4f;
    static final float EXPLOSION_RESISTANCE = 100000000f;
    public WireGlassBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS)
                .isSuffocating(WireGlassBlock::isFalse)
                .isViewBlocking(WireGlassBlock::isTrue)
                .noOcclusion()
                .strength(DESTROY_TIME, EXPLOSION_RESISTANCE)
                .sound(SoundType.GLASS));
    }
    
    private static boolean isTrue(BlockState state, BlockGetter getter, BlockPos pos) {return true;}
    private static boolean isFalse(BlockState state, BlockGetter getter, BlockPos pos) {return false;}
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if(FlatLightsCommonConfig.entityDamageableBlocks.get()) {
            String mobDamageable = "Mob Destructible: " + TextColor.fromLegacyFormat(ChatFormatting.RED) + "TRUE";
            Component mobDamageableTooltip = Component.literal(mobDamageable);
            pTooltip.add(mobDamageableTooltip);
        }
        
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
    
    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return FlatLightsCommonConfig.entityDamageableBlocks.get();
    }
}
