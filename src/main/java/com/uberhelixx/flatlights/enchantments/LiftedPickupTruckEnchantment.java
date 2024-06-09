package com.uberhelixx.flatlights.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class LiftedPickupTruckEnchantment extends Enchantment {
    public LiftedPickupTruckEnchantment() {
        super(Rarity.RARE, EnchantmentType.ARMOR, new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET});
    }

    @Override
    public int getMaxLevel() { return 1; }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public boolean isCurse() { return true; }

    @Override
    public boolean canVillagerTrade() {
        return false;
    }

    @Override
    public boolean canGenerateInLoot() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) { return false; }
}
