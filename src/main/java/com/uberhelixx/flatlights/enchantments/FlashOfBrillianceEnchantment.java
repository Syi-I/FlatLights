package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
}
