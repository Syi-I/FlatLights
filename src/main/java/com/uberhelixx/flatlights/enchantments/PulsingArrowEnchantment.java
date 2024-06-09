package com.uberhelixx.flatlights.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class PulsingArrowEnchantment extends Enchantment {
    public PulsingArrowEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.BOW, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 5; }

}
