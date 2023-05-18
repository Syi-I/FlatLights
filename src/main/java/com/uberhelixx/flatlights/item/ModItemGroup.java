package com.uberhelixx.flatlights.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModItemGroup {
    public static final ItemGroup FLATLIGHTS = new ItemGroup("flatlights") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.PRISMATIC_INGOT.get());
        }
        @Override
        public void fill(NonNullList<ItemStack> items)
        {
            super.fill(items);
        }
    };
}
