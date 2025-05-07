package com.uberhelixx.flatlights.common.item.armor;

import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrismaticBoots extends BaseArmorItem {
    public PrismaticBoots(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_boots_shift");
        }
        else {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            TooltipHelper.shiftHint(pTooltipComponents);
        }
    }
}
