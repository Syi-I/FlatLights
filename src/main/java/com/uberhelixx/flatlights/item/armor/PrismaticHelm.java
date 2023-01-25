package com.uberhelixx.flatlights.item.armor;

import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class PrismaticHelm extends ModArmorItem {
    public PrismaticHelm(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_helm"));
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_helm_shift"));
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private static void onEquip(PlayerEntity player, boolean hasNightVis, boolean hasWaterBreath) {
        if(!hasNightVis) {
            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
        if(!hasWaterBreath) {
            player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }
    }

    private static void onUnequip(PlayerEntity player, boolean hasNightVis, boolean hasWaterBreath) {
        if(hasNightVis) {
            player.removePotionEffect(Effects.NIGHT_VISION);
        }
        if(hasWaterBreath) {
            player.removePotionEffect(Effects.WATER_BREATHING);
        }
    }

    @SubscribeEvent
    public static void equipCheck(LivingEquipmentChangeEvent event) {
        PlayerEntity player;
        boolean hasNightVis;
        boolean hasWaterBreath;
        if(event.getEntityLiving() instanceof PlayerEntity) {
            player = (PlayerEntity) event.getEntityLiving();
            hasNightVis = !Objects.equals(player.getActivePotionEffect(Effects.NIGHT_VISION), null);
            hasWaterBreath = !Objects.equals(player.getActivePotionEffect(Effects.WATER_BREATHING), null);
        }
        else {
            return;
        }
        if(event.getSlot() == EquipmentSlotType.HEAD) {
            if(event.getFrom() == event.getTo() && event.getFrom().getItem() == ModItems.PRISMATIC_HELMET.get()) {
                return;
            }
            else if(event.getTo().getItem() == ModItems.PRISMATIC_HELMET.get()) {
                onEquip((PlayerEntity) event.getEntityLiving(), hasNightVis, hasWaterBreath);
            }
            else {
                onUnequip((PlayerEntity) event.getEntityLiving(), hasNightVis, hasWaterBreath);
            }
        }
    }

}
