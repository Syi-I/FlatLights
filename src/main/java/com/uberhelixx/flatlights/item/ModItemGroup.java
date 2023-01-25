package com.uberhelixx.flatlights.item;

import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ModItemGroup {
    public static final ItemGroup FLATLIGHTS = new ItemGroup("Flat Lights") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.WHITE_BLOCK.get());
        }
        @Override
        public void fill(NonNullList<ItemStack> items)
        {
            super.fill(items);
        }
    };
}
