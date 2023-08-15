package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NeutralizerEnchantment extends Enchantment {
    public NeutralizerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.ARMOR_CHEST, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public int getMaxLevel() { return 1; }

    private static void doPhysDmg(LivingEntity target, float damageAmount) {
        MiscHelpers.debugLogger("[Neutralizer] Doing physical damage");
        target.hurtResistantTime = 0;
        target.attackEntityFrom(ModDamageTypes.PHYSICAL, damageAmount);
        target.hurtResistantTime = 20;
    }

    @SubscribeEvent
    public static void damageSourceConversion(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        float damageAmount = event.getAmount();

        if(event.getSource() != ModDamageTypes.PHYSICAL) {
            for (ItemStack instance : target.getArmorInventoryList()) {
                if(MiscHelpers.enchantCheck(instance, ModEnchantments.NEUTRALIZER.get())) {
                    MiscHelpers.debugLogger("[Neutralizer] Neutralizer enchantment triggered");
                    MiscHelpers.debugLogger("[Neutralizer] Initial un-neutralized damage: " + damageAmount);
                    event.setCanceled(true);
                    doPhysDmg(target, damageAmount);
                }
            }
        }
    }
}
