package com.uberhelixx.flatlights.item.armor;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void negateFallDamage(LivingHurtEvent event) {
        DamageSource dmgSrc = event.getSource();
        if(event.getEntityLiving() instanceof PlayerEntity && wearingBoots((PlayerEntity) event.getEntityLiving())) {
            if(dmgSrc == DamageSource.FALL) {
                event.setCanceled(true);
            }
        }
    }

}
