package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.container.SpectrumAnvilContainer;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnvilEvents {

    //prismatic blade enchantment doubling/multiplying (based on config cap)
    @SubscribeEvent
    public static void EnchantDouble (AnvilUpdateEvent event) {
        if (!Objects.requireNonNull(event.getPlayer()).isServerWorld()) {
            return;
        }

        //get prismatic blade from left anvil slot, enchanted book from right anvil slot
        ItemStack prismaticBlade = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        //check that a prismatic blade and enchanted book are in the anvil
        if (prismaticBlade == null || prismaticBlade.getItem() != ModItems.PRISMATIC_BLADE.get() || enchantedBook == null || enchantedBook.getItem() != Items.ENCHANTED_BOOK) {
            return;
        }

        //grab current enchantments from blade and book
        Map<Enchantment, Integer> swordMap = EnchantmentHelper.getEnchantments(prismaticBlade);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);

        //check if book has enchants or not
        if (bookMap.isEmpty()) {
            return;
        }

        //output enchants for the blade
        Map<Enchantment, Integer> outputMap = new HashMap<>(swordMap);
        int costCounter = 0;

        //putting the enchantment into the output map and doubling enchants
        for (Map.Entry<Enchantment, Integer> entry : bookMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentValue = swordMap.get(entry.getKey());
            Integer addValue = entry.getValue();
            if (currentValue == null) {
                outputMap.put(entry.getKey(), addValue);
                costCounter += addValue;
            } else {
                int value = Math.min(currentValue + addValue, enchantment.getMaxLevel() * FlatLightsCommonConfig.enchantMultiplierCap.get());
                outputMap.put(entry.getKey(), value);
                costCounter += (currentValue + addValue) * 2;
            }
        }

        //output new blade with enchants
        event.setCost(costCounter);
        ItemStack enchantedBlade = prismaticBlade.copy();
        EnchantmentHelper.setEnchantments(outputMap, enchantedBlade);
        event.setOutput(enchantedBlade);
    }

    //letting bread but high quality take any enchantment
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

    //Spectrum Anvil level capping (NYI)
    @SubscribeEvent
    public static void LevelCapping(AnvilUpdateEvent event) {
        if(event.getPlayer() == null) {
            return;
        }
        PlayerEntity playerIn = event.getPlayer();
        Container containerIn = playerIn.openContainer;
        if(!(containerIn instanceof SpectrumAnvilContainer)) {
            return;
        }
        if(event.getCost() > 30) {
            event.setCost(30);
        }
    }
}
