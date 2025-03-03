package com.uberhelixx.flatlights.common.item.armor;

import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PrismaticHelm extends ModArmorItem {
    public PrismaticHelm(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_helm_shift"));
        }
        else {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add(TextHelpers.shiftTooltip("for details"));
        }
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        //give potion effects again only if the player doesn't already have it applied, so we aren't spam reapplying
        if(!world.isRemote()) {
            if (player != null && Objects.equals(player.getActivePotionEffect(Effects.WATER_BREATHING), null)) {
                player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
            }
            if (player != null && Objects.equals(player.getActivePotionEffect(Effects.NIGHT_VISION), null)) {
                player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
            }
        }
        super.onArmorTick(stack, world, player);
    }
    
    public static void onEquip(PlayerEntity player, boolean hasNightVis, boolean hasWaterBreath) {
        if(!hasNightVis) {
            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
        if(!hasWaterBreath) {
            player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }
    }

    public static void onUnequip(PlayerEntity player, boolean hasNightVis, boolean hasWaterBreath) {
        if(hasNightVis) {
            player.removePotionEffect(Effects.NIGHT_VISION);
        }
        if(hasWaterBreath) {
            player.removePotionEffect(Effects.WATER_BREATHING);
        }
    }
}
