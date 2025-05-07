package com.uberhelixx.flatlights.common.enchantments;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class BlackhandEnchantment extends Enchantment {
    protected BlackhandEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        return super.checkCompatibility(pOther) && pOther != Enchantments.KNOCKBACK;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        //dark 2105387
        //lighter 3356224
        //lighterer 4408139
        Style color = Style.EMPTY.withColor(5000275);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
}
