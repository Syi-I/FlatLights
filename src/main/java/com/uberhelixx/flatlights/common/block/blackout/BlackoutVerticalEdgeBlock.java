package com.uberhelixx.flatlights.common.block.blackout;

import com.uberhelixx.flatlights.common.block.light.VerticalEdgeBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class BlackoutVerticalEdgeBlock extends VerticalEdgeBlock {
    public BlackoutVerticalEdgeBlock(ToIntFunction<BlockState> lightLevel, double thickness) {
        super(lightLevel, thickness);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("tooltip.flatlights.blackout").withStyle(ChatFormatting.GRAY));
    }
}
