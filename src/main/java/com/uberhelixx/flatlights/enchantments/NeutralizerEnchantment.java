package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class NeutralizerEnchantment extends Enchantment {
    public NeutralizerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.ARMOR_CHEST, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public int getMaxLevel() { return 1; }

    private static void doPhysDmg(LivingEntity target, float damageAmount) {
        FlatLights.LOGGER.debug("Doing physical damage");
        target.hurtResistantTime = 0;
        target.attackEntityFrom(ModDamageTypes.PHYSICAL, damageAmount);
        target.hurtResistantTime = 20;
    }

    @SubscribeEvent
    public static void damageSourceConversion(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        float damageAmount = event.getAmount();
        boolean neutralizerPresent = false;

        if(event.getSource() != ModDamageTypes.PHYSICAL) {
            for (ItemStack instance : target.getArmorInventoryList()) {
                if (instance.isEnchanted()) {
                    Map<Enchantment, Integer> instanceMap = EnchantmentHelper.getEnchantments(instance);
                    for (Map.Entry<Enchantment, Integer> entry : instanceMap.entrySet()) {
                        if (FlatLightsClientConfig.miscLogging.get()) {
                            FlatLights.LOGGER.debug("Neutralizer: Enchantment found was: " + entry.toString());
                        }
                        if (entry.getKey() == ModEnchantments.NEUTRALIZER.get()) {
                            neutralizerPresent = true;
                        }
                    }
                }
            }

            if (FlatLightsClientConfig.miscLogging.get()) {
                FlatLights.LOGGER.debug("Neutralizer: neutralizerPresent = " + neutralizerPresent);
            }

            if (neutralizerPresent) {
                if (FlatLightsClientConfig.miscLogging.get()) {
                    FlatLights.LOGGER.debug("Neutralizer: Neutralizer enchantment triggered");
                    FlatLights.LOGGER.debug("Neutralizer: Initial un-neutralized damage: " + damageAmount);
                }
                event.setCanceled(true);
                doPhysDmg(target, damageAmount);
            }
        }
    }
}
