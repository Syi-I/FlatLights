package com.uberhelixx.flatlights.item.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.UUID;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PrismaticBlade extends SwordItem {

    private float attackDamage;
    public PrismaticBlade(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(0, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        if (attacker.getUniqueID().compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924b")) == 0) {
            target.setHealth(0);
        }
        else {
            target.addPotionEffect(new EffectInstance(Effects.GLOWING, 15));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 4));
            target.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 5, 2));
            //target.setHealth((float) min(target.getHealth() - this.getAttackDamage(), target.getHealth() - (target.getMaxHealth() * 0.1)));
        }
        return true;
    }



}
