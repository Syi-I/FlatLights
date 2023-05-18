package com.uberhelixx.flatlights.item.armor;

import com.google.common.collect.ImmutableMap;
import com.uberhelixx.flatlights.FlatLightsConfig;
import com.uberhelixx.flatlights.item.armor.ModArmorMaterial;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModArmorItem extends ArmorItem {
    private static final Map<IArmorMaterial, Effect> MATERIAL_TO_EFFECT_MAP =
            new ImmutableMap.Builder<IArmorMaterial, Effect>()
                    .put(ModArmorMaterial.PRISMATIC, Effects.SATURATION)
                    .build();

    public ModArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String dmgReduction = "Up to " + MiscHelpers.coloredText(TextFormatting.GREEN, FlatLightsConfig.armorDamageReduction.get() + "%") + " damage reduction. (+5% reduction per armor point above 20 total points)";
        ITextComponent dmgReductionTooltip = ITextComponent.getTextComponentOrEmpty(dmgReduction);
        tooltip.add(dmgReductionTooltip);

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if(!world.isRemote()) {
            if(hasFullArmor(player)) {
                evaluateArmorEffects(player);
            }
        }

        super.onArmorTick(stack, world, player);
    }

    void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<IArmorMaterial, Effect> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            IArmorMaterial mapArmorMaterial = entry.getKey();
            Effect mapStatusEffect = entry.getValue();

            if(hasCorrectArmorSet(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            }
        }
    }

    private void addStatusEffectForMaterial(PlayerEntity player, IArmorMaterial mapArmorMaterial, Effect mapStatusEffect) {
        boolean hasPlayerEffect = !Objects.equals(player.getActivePotionEffect(mapStatusEffect), null);

        if(hasCorrectArmorSet(mapArmorMaterial, player) && !hasPlayerEffect) {
            player.addPotionEffect(new EffectInstance(mapStatusEffect, 400));

            // if(new Random().nextFloat() > 0.6f) { // 40% of damaging the armor! Possibly!
            // Uncomment if you wanna damage armor
            // player.inventory.func_234563_a_(DamageSource.MAGIC, 1f);
            // }
        }
    }

    boolean hasFullArmor(PlayerEntity player) {
        ItemStack boots = player.inventory.armorItemInSlot(0);
        ItemStack leggings = player.inventory.armorItemInSlot(1);
        ItemStack breastplate = player.inventory.armorItemInSlot(2);
        ItemStack helmet = player.inventory.armorItemInSlot(3);

        return !helmet.isEmpty() && !breastplate.isEmpty()
                && !leggings.isEmpty() && !boots.isEmpty();
    }

    private static boolean hasCorrectArmorSet(IArmorMaterial material, PlayerEntity player) {
        ArmorItem boots = ((ArmorItem)player.inventory.armorItemInSlot(0).getItem());
        ArmorItem leggings = ((ArmorItem)player.inventory.armorItemInSlot(1).getItem());
        ArmorItem chestplate = ((ArmorItem)player.inventory.armorItemInSlot(2).getItem());
        ArmorItem helmet = ((ArmorItem)player.inventory.armorItemInSlot(3).getItem());

        return helmet.getArmorMaterial() == material || chestplate.getArmorMaterial() == material ||
                leggings.getArmorMaterial() == material || boots.getArmorMaterial() == material;
    }

    //check if individual armor pieces are prismatic
    static boolean wearingHelm(PlayerEntity player) {
        if(!player.inventory.armorItemInSlot(3).isEmpty()) {
            ArmorItem helmet = ((ArmorItem)player.inventory.armorItemInSlot(3).getItem());
            return helmet.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    static boolean wearingChest(PlayerEntity player) {
        if(!player.inventory.armorItemInSlot(2).isEmpty()) {
            ArmorItem chestplate = ((ArmorItem)player.inventory.armorItemInSlot(2).getItem());
            return chestplate.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    static boolean wearingLegs(PlayerEntity player) {
        if(!player.inventory.armorItemInSlot(1).isEmpty()) {
            ArmorItem leggings = ((ArmorItem)player.inventory.armorItemInSlot(1).getItem());
            return leggings.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    static boolean wearingBoots(PlayerEntity player) {
        if(!player.inventory.armorItemInSlot(0).isEmpty()) {
            ArmorItem boots = ((ArmorItem)player.inventory.armorItemInSlot(0).getItem());
            return boots.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }

    //reduce incoming damage amount based on armor total, requires at least one piece of prisma armor on to work
    @SubscribeEvent
    public static void DamageReduction(LivingHurtEvent event) {
        if(!event.getSource().isUnblockable()) {
            if (event.getEntity() instanceof PlayerEntity) {
                //Minecraft.getInstance().player.sendChatMessage("Entity taking damage is a player");
                if (wearingBoots((PlayerEntity) event.getEntity()) || wearingLegs((PlayerEntity) event.getEntity()) ||
                        wearingChest((PlayerEntity) event.getEntity()) || wearingHelm((PlayerEntity) event.getEntity())) {
                    //Minecraft.getInstance().player.sendChatMessage("Wearing a prisma armor piece");
                    int armorTotal = ((PlayerEntity) event.getEntity()).getTotalArmorValue();
                    //Minecraft.getInstance().player.sendChatMessage("Total armor value is " + armorTotal);
                    int armorVsDiamondTotal = armorTotal - 20;
                    float reductionRatioCap = FlatLightsConfig.armorDamageReduction.get() / 100f;
                    float reductionRatio;
                    //get reductionRatio, make sure percent doesn't go above reductionRatioCap %
                    if(armorVsDiamondTotal > 0 && reductionRatioCap > 0) {
                        reductionRatio = Math.min((armorVsDiamondTotal * 0.05f), reductionRatioCap);
                    }
                    else {
                        //normal damage taken if at or below diamond level armor
                        reductionRatio = 0;
                    }
                    float reducedDamage = event.getAmount() * (1 - reductionRatio);
                    //Minecraft.getInstance().player.sendChatMessage("Reduced damage is now " + reducedDamage);
                    event.setAmount(reducedDamage);
                }
            }
        }
    }

}
