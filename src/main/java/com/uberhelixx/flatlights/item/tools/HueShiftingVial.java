package com.uberhelixx.flatlights.item.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HueShiftingVial extends Item {

    public HueShiftingVial(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
