package com.uberhelixx.flatlights.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreadButHighQuality extends Item {
    public BreadButHighQuality(Properties properties) {
        super(properties);
    }

    public static void BreadEnchant(AnvilUpdateEvent event) {
        if (!event.getPlayer().isServerWorld()) {
            return;
        }

        //get prismatic blade from left anvil slot, enchanted book from right anvil slot
        ItemStack bread = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        //check that a prismatic blade and enchanted book are in the anvil
        if (bread == null || bread.getItem() != ModItems.PRISMATIC_BLADE.get() || enchantedBook == null || enchantedBook.getItem() != Items.ENCHANTED_BOOK) {
            return;
        }

        //grab current enchantments from blade and book
        Map<Enchantment, Integer> breadMap = EnchantmentHelper.getEnchantments(bread);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);

        //check if book has enchants or not
        if (bookMap.isEmpty()) {
            return;
        }

        //output enchants for the blade
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

        //output new blade with enchants
        event.setCost(costCounter);
        ItemStack magicBread = bread.copy();
        EnchantmentHelper.setEnchantments(outputMap, magicBread);
        event.setOutput(magicBread);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack bread = playerIn.getHeldItem(handIn);

        if(uuidCheck(playerIn.getUniqueID())) {

            Map<Enchantment, Integer> breadMap = EnchantmentHelper.getEnchantments(bread);
            Map<Enchantment, Integer> outputMap = new HashMap<>(breadMap);

            for (Map.Entry<Enchantment, Integer> entry : breadMap.entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment == null) {
                    continue;
                }
                Integer currentValue = breadMap.get(entry.getKey());
                if (currentValue == null) {
                    continue;
                } else {
                    outputMap.put(entry.getKey(), 32767);
                }
            }
            ItemStack powerBread = bread.copy();
            EnchantmentHelper.setEnchantments(outputMap, powerBread);
            return ActionResult.resultPass(powerBread);
        }

        return ActionResult.resultPass(bread);
    }

    private boolean uuidCheck(UUID targetUuid) {
        //380df991-f603-344c-a090-369bad2a924a is dev uuid
        if(0 == targetUuid.compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924b"))) {
            return true;
        }
        if(0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) {
            return true;
        }
        return false;
    }
}


