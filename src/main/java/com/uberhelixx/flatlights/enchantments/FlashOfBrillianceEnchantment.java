package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

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

    @SubscribeEvent
    public static void xpDropMultiplier(LivingExperienceDropEvent event) {
        LivingEntity user = event.getAttackingPlayer();
        int baseXpAmount = event.getDroppedExperience();

        ItemStack instance = user.getHeldItemMainhand();
        if (instance.isEnchanted()) {
            Map<Enchantment, Integer> instanceMap = EnchantmentHelper.getEnchantments(instance);
            for (Map.Entry<Enchantment, Integer> entry : instanceMap.entrySet()) {
                MiscHelpers.debugLogger("[Flash of Brilliance] Enchantment found was: " + entry.toString());
                if (entry.getKey() == ModEnchantments.FLASH_OF_BRILLIANCE.get()) {
                    int level = entry.getValue();
                    double chanceCap = FlatLightsCommonConfig.fobChanceCap.get();
                    double activeChance = level * 0.05;
                    if(Math.random() <= Math.min(activeChance, chanceCap)) {
                        event.setDroppedExperience(10 * baseXpAmount);
                        MiscHelpers.debugLogger("[Flash of Brilliance] Triggered XP multiplier.");
                        MiscHelpers.debugLogger("[Flash of Brilliance] Base XP value: " + baseXpAmount + " | New XP value: " + baseXpAmount * 10);
                    }
                }
            }
        }


    }
}
