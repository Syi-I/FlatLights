package com.uberhelixx.flatlights.enchantments;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
            double weaponDamage = MiscHelpers.getItemDamage(weapon);
            target.hurtResistantTime = 0;
            target.attackEntityFrom(ModDamageTypes.QUANTUM, (float) (weaponDamage * (1 + (0.1f * level))));
            FlatLights.LOGGER.info("Weapon damage = " + weaponDamage);
            target.hurtResistantTime = 20;
        }
    }

    /*public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        return 1 + (0.75f * level);
    }*/

    @SubscribeEvent
    public static void quantumDmg(LivingHurtEvent event) {
        if(event.getSource() == ModDamageTypes.QUANTUM) {
            FlatLights.LOGGER.info("Dealt " + event.getAmount() + " quantum damage");
            LivingEntity target = event.getEntityLiving();
            target.addPotionEffect(new EffectInstance(Effects.GLOWING, 600));
        }
    }

    public static void entangleDmg(LivingHurtEvent event) {

    }
}
