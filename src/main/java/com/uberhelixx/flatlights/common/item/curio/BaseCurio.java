package com.uberhelixx.flatlights.common.item.curio;

import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BaseCurio extends Item implements ICurioItem {
    public BaseCurio() {
        super(new Item.Properties().stacksTo(1).defaultDurability(0).fireResistant());
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        //basic info tooltip
        if(pStack.getTag() != null && !pStack.getTag().isEmpty()) {
            if(!Screen.hasShiftDown()) {
                CurioUtils.getSetTooltip(pStack, pTooltipComponents);
                if (pLevel != null && pLevel.isClientSide()) {
                    CurioUtils.getSetEffectTooltip(pStack, pTooltipComponents);
                }
                CurioUtils.getTierTooltip(pStack, pTooltipComponents);
                if (pStack.getTag().getFloat(CurioUtils.TIER) == CurioTier.getModel(CurioTier.GROWTH) && pStack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                    //hide tooltip if the cap is maxed out
                    CurioUtils.getGrowthTooltip(pStack, pStack.getTag().getInt(CurioUtils.GROWTH_CAP) != Integer.MAX_VALUE, pTooltipComponents);
                }
            }
            else {
                if (pLevel != null && pLevel.isClientSide) {
                    CurioUtils.getSetEffectTooltip(pStack, pTooltipComponents);
                }
                CurioUtils.getSetDescriptionTooltip(pStack, pTooltipComponents);
            }
        }
        //how to use curio when not rolled yet
        else {
            Style color = Style.EMPTY.withColor(ChatFormatting.GRAY);
            TooltipHelper.genericBrackets(pTooltipComponents, "Right-click to roll.", color);
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
    
    }
}
