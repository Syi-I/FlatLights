package com.uberhelixx.flatlights.item.armor;

import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PrismaticLeggings extends ModArmorItem {
    public PrismaticLeggings(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_leggings_shift"));
        }
        else {
            super.addInformation(stack, worldIn, tooltip, flagIn);
            tooltip.add(TextHelpers.shiftTooltip("for details"));
        }
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

}
