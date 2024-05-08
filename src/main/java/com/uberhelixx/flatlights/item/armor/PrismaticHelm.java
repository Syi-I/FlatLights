package com.uberhelixx.flatlights.item.armor;

import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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
