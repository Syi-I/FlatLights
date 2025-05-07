package com.uberhelixx.flatlights.common.item.armor;

import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PrismaticHelm extends BaseArmorItem {
    
    public PrismaticHelm(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_helm_shift");
        }
        else {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            TooltipHelper.shiftHint(pTooltipComponents);
        }
    }
    
    public static void onEquip(Player player, boolean hasNightVis, boolean hasWaterBreath) {
        if(!hasNightVis) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
        if(!hasWaterBreath) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }
    }
    
    public static void onUnequip(Player player, boolean hasNightVis, boolean hasWaterBreath) {
        if(hasNightVis) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
        if(hasWaterBreath) {
            player.removeEffect(MobEffects.WATER_BREATHING);
        }
    }
    
    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        //give potion effects again only if the player doesn't already have it applied, so we aren't spam reapplying
        if(!level.isClientSide() && player != null && wearingHelm(player)) {
            if(Objects.equals(player.getEffect(MobEffects.WATER_BREATHING), null)) {
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
            }
            if(Objects.equals(player.getEffect(MobEffects.NIGHT_VISION), null)) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
            }
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }
    
    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return true;
    }
}
