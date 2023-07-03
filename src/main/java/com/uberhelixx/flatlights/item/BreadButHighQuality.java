package com.uberhelixx.flatlights.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BreadButHighQuality extends Item {
    public BreadButHighQuality(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void BreadEnchant(AnvilUpdateEvent event) {
        if (!Objects.requireNonNull(event.getPlayer()).isServerWorld()) {
            return;
        }

        //get bread from left anvil slot, enchanted book from right anvil slot
        ItemStack bread = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        //check that bread and enchanted book are in the anvil
        if (bread == null || bread.getItem() != ModItems.BIG_BREAD.get() || enchantedBook == null || enchantedBook.getItem() != Items.ENCHANTED_BOOK) {
            return;
        }

        //grab current enchantments from bread and book
        Map<Enchantment, Integer> breadMap = EnchantmentHelper.getEnchantments(bread);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);

        //check if book has enchants or not
        if (bookMap.isEmpty()) {
            return;
        }

        //output enchants for the bread
        Map<Enchantment, Integer> outputMap = new HashMap<>(breadMap);
        int costCounter = 0;

        //putting the enchantment into the output map and doubling enchants
        for (Map.Entry<Enchantment, Integer> entry : bookMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentValue = breadMap.get(entry.getKey());
            Integer addValue = entry.getValue();
            if (currentValue == null) {
                outputMap.put(entry.getKey(), addValue);
                costCounter += addValue;
            } else {
                int value = Math.min(currentValue + addValue, enchantment.getMaxLevel());
                outputMap.put(entry.getKey(), value);
                costCounter += (currentValue + addValue);
            }
        }

        //output new bread with enchants
        event.setCost(costCounter);
        ItemStack magicBread = bread.copy();
        EnchantmentHelper.setEnchantments(outputMap, magicBread);
        event.setOutput(magicBread);
    }
}


