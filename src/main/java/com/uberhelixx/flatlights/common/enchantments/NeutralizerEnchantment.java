package com.uberhelixx.flatlights.common.enchantments;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NeutralizerEnchantment extends Enchantment {
    protected NeutralizerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[] {EquipmentSlot.CHEST});
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        Style color = Style.EMPTY.withColor(5992051);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
}
