package com.uberhelixx.flatlights.common.enchantments;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FragmentationEnchantment extends Enchantment {
    protected FragmentationEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[] {EquipmentSlot.CHEST});
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        Style color = Style.EMPTY.withColor(15179310);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
}
