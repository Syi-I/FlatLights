package com.uberhelixx.flatlights.item.armor;

import com.google.common.collect.ImmutableMap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ModArmorItem extends ArmorItem {
    private static final Map<IArmorMaterial, Effect> MATERIAL_TO_EFFECT_MAP =
            new ImmutableMap.Builder<IArmorMaterial, Effect>()
                    .put(ModArmorMaterial.PRISMATIC, Effects.SATURATION)
                    .build();

    public ModArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties settings) {
        super(material, slot, settings);
    }

    /*@Override
    public boolean isDamageable() {
        return false;
    }*/

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String setEffect = TextHelpers.labelBrackets("Set Effect", null, "Saturation", TextFormatting.GOLD).getString();
        ITextComponent setEffectTooltip = ITextComponent.getTextComponentOrEmpty(setEffect);
        tooltip.add(setEffectTooltip);

        String dmgReduction = "Up to " + MiscHelpers.coloredText(TextFormatting.GREEN, FlatLightsCommonConfig.armorDamageReduction.get() + "%") + " bonus damage reduction when full set is equipped.";
        if(Minecraft.getInstance().player != null) {
            int armorTotal = Minecraft.getInstance().player.getTotalArmorValue();
            int armorVsDiamondTotal = armorTotal - 20;
            float reductionRatioCap = FlatLightsCommonConfig.armorDamageReduction.get() / 100f;
            float reductionRatio;

            //get reductionRatio, make sure percent doesn't go above reductionRatioCap %
            if(armorVsDiamondTotal > 0 && reductionRatioCap > 0) {
                reductionRatio = Math.min((armorVsDiamondTotal * 0.05f), reductionRatioCap);
            }
            //normal damage taken if at or below diamond level armor
            else {
                reductionRatio = 0;
            }

            DecimalFormat formatting = new DecimalFormat("#.##");
            formatting.setRoundingMode(RoundingMode.FLOOR);

            //capped damage reduction tooltip colors
            if(reductionRatio >= reductionRatioCap) {
                dmgReduction = TextHelpers.labelBrackets("Damage Reduction", null, MiscHelpers.coloredText(TextFormatting.GREEN, "" + formatting.format(reductionRatio * 100)) + "/" + MiscHelpers.coloredText(TextFormatting.GREEN, FlatLightsCommonConfig.armorDamageReduction.get() + "%"), null).getString();
            }
            //not full damage reduction tooltip colors
            else {
                dmgReduction = TextHelpers.labelBrackets("Damage Reduction", null, MiscHelpers.coloredText(TextFormatting.RED, "" + formatting.format(reductionRatio * 100)) + "/" + MiscHelpers.coloredText(TextFormatting.GREEN, FlatLightsCommonConfig.armorDamageReduction.get() + "%"), null).getString();
            }
        }
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
    
    //stops durability damage while allowing for `BREAKABLE` type enchantments to still apply to the armor, vs isDamageable preventing that if set to TRUE
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
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

    public boolean hasFullArmor(PlayerEntity player) {
        //use player.inventory.armorInventory.get(slot#) instead of player.inventory.armorItemInSlot(slot#)
        //armorItemInSlot is clientside only
        ItemStack boots = player.inventory.armorInventory.get(0);
        ItemStack leggings = player.inventory.armorInventory.get(1);
        ItemStack chestplate = player.inventory.armorInventory.get(2);
        ItemStack helmet = player.inventory.armorInventory.get(3);

        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
    }

    public static boolean hasCorrectArmorSet(IArmorMaterial material, PlayerEntity player) {
        ArmorItem boots = ((ArmorItem) player.inventory.armorInventory.get(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.inventory.armorInventory.get(1).getItem());
        ArmorItem chestplate = ((ArmorItem) player.inventory.armorInventory.get(2).getItem());
        ArmorItem helmet = ((ArmorItem) player.inventory.armorInventory.get(3).getItem());

        return helmet.getArmorMaterial() == material || chestplate.getArmorMaterial() == material ||
                leggings.getArmorMaterial() == material || boots.getArmorMaterial() == material;
    }

    //check if individual armor pieces are prismatic
    public static boolean wearingHelm(PlayerEntity player) {
        if (!player.inventory.armorInventory.get(3).isEmpty()) {
            ArmorItem helmet = ((ArmorItem) player.inventory.armorInventory.get(3).getItem());
            return helmet.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    public static boolean wearingChest(PlayerEntity player) {
        if (!player.inventory.armorInventory.get(2).isEmpty()) {
            ArmorItem chestplate = ((ArmorItem) player.inventory.armorInventory.get(2).getItem());
            return chestplate.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    public static boolean wearingLegs(PlayerEntity player) {
        if (!player.inventory.armorInventory.get(1).isEmpty()) {
            ArmorItem leggings = ((ArmorItem) player.inventory.armorInventory.get(1).getItem());
            return leggings.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    public static boolean wearingBoots(PlayerEntity player) {
        if (!player.inventory.armorInventory.get(0).isEmpty()) {
            ArmorItem boots = ((ArmorItem) player.inventory.armorInventory.get(0).getItem());
            return boots.getArmorMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
}
