package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Objects;

public class BonesawEnchantment extends Enchantment {
    public BonesawEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 1; }


    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            if(((LivingEntity) target).isPotionActive(ModEffects.ARMOR_SHRED.get())) {
                int amplifier = Objects.requireNonNull(((LivingEntity) target).getActivePotionEffect(ModEffects.ARMOR_SHRED.get())).getAmplifier();
                ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(amplifier + 1, FlatLightsCommonConfig.bonesawStacks.get())));
            }
            else {
                ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(level, FlatLightsCommonConfig.bonesawStacks.get())));
            }
        }
    }
}
