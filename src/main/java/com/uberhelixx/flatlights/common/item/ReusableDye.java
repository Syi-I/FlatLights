package com.uberhelixx.flatlights.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ReusableDye extends Item {
    
    public ReusableDye(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.copy();
    }
    
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
