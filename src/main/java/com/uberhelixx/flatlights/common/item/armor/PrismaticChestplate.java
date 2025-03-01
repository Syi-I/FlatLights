package com.uberhelixx.flatlights.item.armor;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PrismaticChestplate extends ModArmorItem {
    public PrismaticChestplate(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_chestplate_shift"));
            if(!FlatLightsCommonConfig.chestplateFlight.get()) {
                String disabled = "Flight: " + MiscHelpers.coloredText(TextFormatting.RED, "DISABLED");
                ITextComponent disabledTooltip = ITextComponent.getTextComponentOrEmpty(disabled);
                tooltip.add(disabledTooltip);
            }
        }
        else {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add(TextHelpers.shiftTooltip("for details"));
        }
    }

    public static void onEquip(PlayerEntity player) {
        if(!player.isCreative() && !player.isSpectator() && FlatLightsCommonConfig.chestplateFlight.get()) {
            player.abilities.allowFlying = true;
            player.sendPlayerAbilities();
        }
    }

    public static void onUnequip(PlayerEntity player) {
        if(!player.isCreative() && !player.isSpectator() && FlatLightsCommonConfig.chestplateFlight.get()) {
            player.abilities.allowFlying = false;
            player.abilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }
}
