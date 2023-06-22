package com.uberhelixx.flatlights.enchantments;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.EntangledEffect;
import com.uberhelixx.flatlights.effect.ModEffects;
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
import java.util.List;

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
            ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.ENTANGLED.get(), 600, level));
            target.hurtResistantTime = 20;
        }
    }

    /*public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        return 1 + (0.75f * level);
    }*/

    public static void entangleDmg(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        double searchRadius = 16.0D;
        if(event.getSource() == ModDamageTypes.ENTANGLED) {
            return;
        }
        else {
            List<Entity> entities = target.world.getEntitiesWithinAABBExcludingEntity(target, target.getBoundingBox().expand(searchRadius, searchRadius, searchRadius));
            for (Entity instance : entities) {
                if (instance instanceof LivingEntity && ((LivingEntity) instance).isPotionActive(ModEffects.ENTANGLED.get())) {
                    FlatLights.LOGGER.info("found entity: " + instance.getName());
                    instance.hurtResistantTime = 0;
                    instance.attackEntityFrom(ModDamageTypes.ENTANGLED, event.getAmount());
                }
            }
        }
    }
}
