package com.uberhelixx.flatlights.common.item.armor;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrismaticChestplate extends BaseArmorItem {
    
    public PrismaticChestplate(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_chestplate_shift");
            if(!FlatLightsCommonConfig.chestplateFlight.get()) {
                Component disabled = Component.literal("Flight: ").append(Component.translatable("flatlights.disabled").withStyle(ChatFormatting.RED));
                pTooltipComponents.add(disabled);
            }
        }
        else {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            TooltipHelper.shiftHint(pTooltipComponents);
        }
    }
    
    public static void onEquip(Player player) {
        if(!player.isCreative() && !player.isSpectator() && FlatLightsCommonConfig.chestplateFlight.get()) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }
    
    public static void onUnequip(Player player) {
        if(!player.isCreative() && !player.isSpectator() && FlatLightsCommonConfig.chestplateFlight.get()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }
}
