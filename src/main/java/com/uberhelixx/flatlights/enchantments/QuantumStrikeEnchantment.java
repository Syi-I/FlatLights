package com.uberhelixx.flatlights.enchantments;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.EntangledEffect;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;

public class QuantumStrikeEnchantment extends Enchantment {
    public QuantumStrikeEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 5; }

    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            ItemStack weapon = user.getHeldItemMainhand();

            //initial fist damage is just a flat 1 dmg
            double weaponDamage = 1;

            //get attribute map of the currently held mainhand item
            Multimap<Attribute, AttributeModifier> attributes = weapon.getAttributeModifiers(EquipmentSlotType.MAINHAND);
            MiscHelpers.debugLogger("[Quantum Strike] Damage attribute from weapon: " + getDamageAttribute(weapon, attributes, Attributes.ATTACK_DAMAGE));
            MiscHelpers.debugLogger("[Quantum Strike] Damage addition from sharpness: " + getSharpnessDmg(weapon));

            //total up additional damage modifiers from the weapon and any present sharpness levels
            double newWeaponDmg = getDamageAttribute(weapon, attributes, Attributes.ATTACK_DAMAGE) + getSharpnessDmg(weapon);
            MiscHelpers.debugLogger("[Quantum Strike] Total Dmg: " + newWeaponDmg);

            //add damage modifiers to initial fist damage for total weapon damage
            weaponDamage += newWeaponDmg;

            //reset iframes so that the instance of quantum damage hits and actually deals damage
            target.hurtResistantTime = 0;
            target.attackEntityFrom(ModDamageTypes.causeIndirectQuantum(user, null), (float) (weaponDamage * (1 + (0.1F * level))) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.quantumPercent.get()));

            //apply entangled for shared damage effect, glowing and set team for colored outline indicator of being entangled
            ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.ENTANGLED.get(), 600, level));
            ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.GLOWING, 600));
            MiscHelpers.addToTeam((LivingEntity) target, EntangledEffect.getEntangledTeam(), TextFormatting.BLUE);
            //target.hurtResistantTime = 20;
        }
    }

    private double getDamageAttribute(ItemStack itemIn, Multimap<Attribute, AttributeModifier> mapIn, Attribute attributeIn) {
        Collection<AttributeModifier> collector;
        double amount = 0;

        //get the value of the input attribute
        collector = mapIn.get(attributeIn);

        //make sure that collection actually has some entry in it to use
        if(!collector.isEmpty()) {
            //total up all attribute values of the input type
            for(AttributeModifier entry : collector) {
                double entryAmount = entry.getAmount();
                amount += entryAmount;
                MiscHelpers.debugLogger("[Quantum Strike] Attack Dmg Attribute Name: " + Attributes.ATTACK_DAMAGE.getAttributeName());
                MiscHelpers.debugLogger("[Quantum Strike] Collection Entry Name: " + entry);
            }
        }

        return amount;
    }

    private double getSharpnessDmg(ItemStack itemIn) {
        //base sharpness damage addition
        double amount = 0;

        //check if sharpness is on the held item
        if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemIn) > 0) {
            //sharpness is 0.5 dmg + 0.5 * lvl for additional damage calcs
            amount = 0.5;
            amount = amount + (0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemIn));
        }
        return amount;
    }

    /*public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        return 1 + (0.75f * level);
    }*/
}
