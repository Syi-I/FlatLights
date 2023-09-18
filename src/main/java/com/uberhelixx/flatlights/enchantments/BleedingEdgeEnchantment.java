package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.effect.ModEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;

import java.util.Objects;

public class BleedingEdgeEnchantment extends Enchantment {
    public BleedingEdgeEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 1; }

    //mnoved to EnchantmentEvents because apparently onEntityDamaged double fires, leading to double stack gain
    /*@Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        int stackCap = FlatLightsCommonConfig.bleedStacks.get() - 1;

        if(target instanceof LivingEntity) {
            LivingEntity hitEntity = (LivingEntity)target;
            if(hitEntity.isPotionActive(ModEffects.BLEED.get())) {
                int amplifier = Objects.requireNonNull(hitEntity.getActivePotionEffect(ModEffects.BLEED.get())).getAmplifier();
                int duration = Objects.requireNonNull(hitEntity.getActivePotionEffect(ModEffects.BLEED.get())).getDuration();
                if(user instanceof PlayerEntity) {
                    ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bleeding Edge] Amplifier level: " + amplifier);
                    user.sendMessage(message, user.getUniqueID());
                }
                if(duration <= 5 || amplifier + 1 <= stackCap) {
                    hitEntity.addPotionEffect(new EffectInstance(ModEffects.BLEED.get(), 600, Math.min(amplifier + 1, stackCap)));
                }
            }
            else {
                if(user instanceof PlayerEntity) {
                    ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bleeding Edge] Enchant level: " + level);
                    user.sendMessage(message, user.getUniqueID());
                }
                hitEntity.addPotionEffect(new EffectInstance(ModEffects.BLEED.get(), 600, Math.min(level - 1, stackCap)));
            }
        }
    }*/
}
