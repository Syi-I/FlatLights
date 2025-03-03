package com.uberhelixx.flatlights.common.item.armor;

import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.item.IArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PrismaticBoots extends ModArmorItem {
    public PrismaticBoots(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_boots_shift"));
        }
        else {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add(TextHelpers.shiftTooltip("for details"));
        }
    }
}
