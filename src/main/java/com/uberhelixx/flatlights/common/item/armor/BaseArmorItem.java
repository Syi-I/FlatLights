package com.uberhelixx.flatlights.common.item.armor;

import com.google.common.collect.ImmutableMap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.ClientUtils;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class BaseArmorItem extends ArmorItem {
    private static final Map<ArmorMaterial, MobEffect> MATERIAL_TO_EFFECT_MAP =
            new ImmutableMap.Builder<ArmorMaterial, MobEffect>()
                    .put(ModArmorMaterial.PRISMATIC, MobEffects.SATURATION)
                    .build();
    
    public BaseArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    
    //use inventoryTick now instead of deprecated armorTick
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        Player player = pEntity instanceof Player ? (Player) pEntity : null;
        if(!pLevel.isClientSide() && player != null) {
            if(hasFullArmor(player)) {
                evaluateArmorEffects(player);
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        TooltipHelper.labelBrackets(pTooltipComponents, "Set Effect", null, "Saturation", Style.EMPTY.withColor(ChatFormatting.GOLD));
        
        Component dmgReduceComponent = Component.literal(FlatLightsCommonConfig.armorDamageReduction.get() + "%").withStyle(ChatFormatting.GREEN)
                .append(Component.literal(" bonus damage reduction when full set is equipped."));
        
        if(ClientUtils.getPlayer() != null) {
            int armorTotal = ClientUtils.getPlayer().getArmorValue();
            float totalReduction = (float) (armorTotal * (FlatLightsCommonConfig.reductionPerPoint.get() / 100f));
            float reductionRatioCap = FlatLightsCommonConfig.armorDamageReduction.get() / 100f;
            //get reductionRatio, make sure percent doesn't go above reductionRatioCap %
            float reductionRatio = Mth.clamp(totalReduction, 0, reductionRatioCap);
            
            DecimalFormat formatting = new DecimalFormat("#.##");
            formatting.setRoundingMode(RoundingMode.FLOOR);
            
            //capped damage reduction tooltip colors
            if(reductionRatio >= reductionRatioCap) {
                dmgReduceComponent = Component.literal(formatting.format(reductionRatio * 100)).withStyle(ChatFormatting.GREEN)
                        .append(Component.literal("/").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(FlatLightsCommonConfig.armorDamageReduction.get() + "%").withStyle(ChatFormatting.GREEN));
            }
            //not full damage reduction tooltip colors
            else {
                dmgReduceComponent = Component.literal(formatting.format(reductionRatio * 100)).withStyle(ChatFormatting.RED)
                        .append(Component.literal("/").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(FlatLightsCommonConfig.armorDamageReduction.get() + "%").withStyle(ChatFormatting.GREEN));
            }
        }
        TooltipHelper.labelBrackets(pTooltipComponents, "Damage Reduction", null, dmgReduceComponent);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }
    
    void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, MobEffect> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            MobEffect mapStatusEffect = entry.getValue();
            
            if(hasCorrectArmorSet(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            }
        }
    }
    
    private void addStatusEffectForMaterial(Player player, ArmorMaterial mapArmorMaterial, MobEffect mapStatusEffect) {
        boolean hasPlayerEffect = !Objects.equals(player.getEffect(mapStatusEffect), null);
        
        if(hasCorrectArmorSet(mapArmorMaterial, player) && !hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(mapStatusEffect, 400, 0, true, false));
        }
    }
    
    public boolean hasFullArmor(Player player) {
        //use player.getInventory().armor.get(slot#) instead of player.getInventory().getArmor(slot#)
        //getArmor is clientside only
        ItemStack boots = player.getInventory().armor.get(0);
        ItemStack leggings = player.getInventory().armor.get(1);
        ItemStack chestplate = player.getInventory().armor.get(2);
        ItemStack helmet = player.getInventory().armor.get(3);
        
        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
    }
    
    public static boolean hasCorrectArmorSet(ArmorMaterial material, Player player) {
        ArmorItem boots = ((ArmorItem) player.getInventory().armor.get(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.getInventory().armor.get(1).getItem());
        ArmorItem chestplate = ((ArmorItem) player.getInventory().armor.get(2).getItem());
        ArmorItem helmet = ((ArmorItem) player.getInventory().armor.get(3).getItem());
        
        return helmet.getMaterial() == material && chestplate.getMaterial() == material &&
                leggings.getMaterial() == material && boots.getMaterial() == material;
    }
    
    //check if individual armor pieces are prismatic
    public static boolean wearingHelm(Player player) {
        if (!player.getInventory().armor.get(3).isEmpty()) {
            ArmorItem helmet = ((ArmorItem) player.getInventory().armor.get(3).getItem());
            return helmet.getMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    public static boolean wearingChest(Player player) {
        if (!player.getInventory().armor.get(2).isEmpty()) {
            ArmorItem chestplate = ((ArmorItem) player.getInventory().armor.get(2).getItem());
            return chestplate.getMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    public static boolean wearingLegs(Player player) {
        if (!player.getInventory().armor.get(1).isEmpty()) {
            ArmorItem leggings = ((ArmorItem) player.getInventory().armor.get(1).getItem());
            return leggings.getMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    public static boolean wearingBoots(Player player) {
        if (!player.getInventory().armor.get(0).isEmpty()) {
            ArmorItem boots = ((ArmorItem) player.getInventory().armor.get(0).getItem());
            return boots.getMaterial() == ModArmorMaterial.PRISMATIC;
        }
        return false;
    }
    
    //stops durability damage while allowing for `BREAKABLE` type enchantments to still apply to the armor, vs isDamageable preventing that if set to TRUE
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, 0, entity, onBroken);
    }
}
