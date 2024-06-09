package com.uberhelixx.flatlights.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class FlashOfBrillianceEnchantment extends Enchantment {
    public FlashOfBrillianceEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 5; }

    @Override
    public ITextComponent getDisplayName(int level) {
        return ((IFormattableTextComponent) super.getDisplayName(level)).mergeStyle(TextFormatting.DARK_GREEN);
    }
}
