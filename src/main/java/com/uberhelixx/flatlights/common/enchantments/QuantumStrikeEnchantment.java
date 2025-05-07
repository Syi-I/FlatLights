package com.uberhelixx.flatlights.common.enchantments;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.effect.EntangledEffect;
import com.uberhelixx.flatlights.common.effect.ModEffects;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Collection;

public class QuantumStrikeEnchantment extends Enchantment {
    protected QuantumStrikeEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        Style color = Style.EMPTY.withColor(4984012);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
    
    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        super.doPostAttack(pAttacker, pTarget, pLevel);
        if(pTarget instanceof LivingEntity) {
            ItemStack weapon = pAttacker.getMainHandItem();
            
            //initial fist damage is just a flat 1 dmg
            double weaponDamage = 1;
            
            //get attribute map of the currently held mainhand item
            Multimap<Attribute, AttributeModifier> attributes = weapon.getAttributeModifiers(EquipmentSlot.MAINHAND);
            FlatLights.LOGGER.info("[Quantum Strike] Damage attribute from weapon: " + getDamageAttribute(attributes, Attributes.ATTACK_DAMAGE));
            FlatLights.LOGGER.info("[Quantum Strike] Damage addition from sharpness: " + getSharpnessDmg(pAttacker));
            
            //total up additional damage modifiers from the weapon and any present sharpness levels
            double newWeaponDmg = getDamageAttribute(attributes, Attributes.ATTACK_DAMAGE) + getSharpnessDmg(pAttacker);
            FlatLights.LOGGER.info("[Quantum Strike] Total Dmg: " + newWeaponDmg);
            
            //add damage modifiers to initial fist damage for total weapon damage
            weaponDamage += newWeaponDmg;
            
            //reset iframes so that the instance of quantum damage hits and actually deals damage
            pTarget.invulnerableTime = 0;
            pTarget.hurt(ModDamageTypes.causeQuantumDamage(pAttacker), (float) (weaponDamage * (1 + (0.1F * pLevel))) * MiscUtils.damagePercentCalc(FlatLightsCommonConfig.quantumPercent.get()));
            
            //apply entangled for shared damage effect, glowing and set team for colored outline indicator of being entangled
            ((LivingEntity) pTarget).addEffect(new MobEffectInstance(ModEffects.ENTANGLED.get(), 600, pLevel));
            ((LivingEntity) pTarget).addEffect(new MobEffectInstance(MobEffects.GLOWING, 600));
            MiscUtils.addToTeam((LivingEntity) pTarget, EntangledEffect.getEntangledTeam(), ChatFormatting.BLUE);
            //target.hurtResistantTime = 20;
        }
    }
    
    private double getDamageAttribute(Multimap<Attribute, AttributeModifier> mapIn, Attribute attributeIn) {
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
                FlatLights.LOGGER.info("[Quantum Strike] Attack Dmg Attribute Name: " + Attributes.ATTACK_DAMAGE.getDescriptionId());
                FlatLights.LOGGER.info("[Quantum Strike] Collection Entry Name: " + entry);
            }
        }
        
        return amount;
    }
    
    private double getSharpnessDmg(LivingEntity entityIn) {
        //base sharpness damage addition
        double amount = 0;
        
        //check if sharpness is on the held item
        if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, entityIn) > 0) {
            //sharpness is 0.5 dmg + 0.5 * lvl for additional damage calcs
            amount = 0.5;
            amount = amount + (0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, entityIn));
        }
        return amount;
    }
}
