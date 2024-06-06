package com.uberhelixx.flatlights.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class NeutralizerEnchantment extends Enchantment {
    public NeutralizerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.ARMOR_CHEST, new EquipmentSlotType[] {EquipmentSlotType.CHEST});
    }

    @Override
    public int getMaxLevel() { return 1; }
}
