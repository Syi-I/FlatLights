package com.uberhelixx.flatlights.item.armor;

import com.uberhelixx.flatlights.FlatLightsConfig;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
            if(!FlatLightsConfig.chestplateFlight.get()) {
                String disabled = "Flight: " + MiscHelpers.coloredText(TextFormatting.RED, "DISABLED");
                ITextComponent disabledTooltip = ITextComponent.getTextComponentOrEmpty(disabled);
                tooltip.add(disabledTooltip);
            }
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private static void onEquip(PlayerEntity player) {
        if(!player.isCreative() && !player.isSpectator() && FlatLightsConfig.chestplateFlight.get()) {
            player.abilities.allowFlying = true;
            player.sendPlayerAbilities();
        }
    }

    private static void onUnequip(PlayerEntity player) {
        if(!player.isCreative() && !player.isSpectator() && FlatLightsConfig.chestplateFlight.get()) {
            player.abilities.allowFlying = false;
            player.abilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @SubscribeEvent
    public static void equipCheck(LivingEquipmentChangeEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            if(event.getSlot() == EquipmentSlotType.CHEST) {
                if(event.getFrom() == event.getTo() && event.getFrom().getItem() == ModItems.PRISMATIC_CHESTPLATE.get()) {
                    return;
                }
                else if(event.getTo().getItem() == ModItems.PRISMATIC_CHESTPLATE.get()) {
                    onEquip((PlayerEntity) event.getEntityLiving());
                }
                else {
                    onUnequip((PlayerEntity) event.getEntityLiving());
                }
            }
        }
    }
}
