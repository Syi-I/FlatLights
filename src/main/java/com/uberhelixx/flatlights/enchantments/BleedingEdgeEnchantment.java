package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.effect.ModEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;

import java.util.Objects;

public class BleedingEdgeEnchantment extends Enchantment {
    public BleedingEdgeEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 1; }


    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            if(((LivingEntity) target).isPotionActive(ModEffects.BLEED.get())) {
                int amplifier = Objects.requireNonNull(((LivingEntity) target).getActivePotionEffect(ModEffects.BLEED.get())).getAmplifier();
                int duration = Objects.requireNonNull(((LivingEntity) target).getActivePotionEffect(ModEffects.BLEED.get())).getDuration();
                if(duration <= 5 || amplifier + 1 <= FlatLightsCommonConfig.bleedStacks.get()) {
                    ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.BLEED.get(), 600, Math.min(amplifier + 1, FlatLightsCommonConfig.bleedStacks.get())));
                }
            }
            else {
                ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.BLEED.get(), 600, Math.min(level, FlatLightsCommonConfig.bleedStacks.get())));
            }
        }
    }
}
