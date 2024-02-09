package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.effect.ModEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;

public class LethalityEnchantment extends Enchantment {
    public LethalityEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 1; }

    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            LivingEntity hitEntity = (LivingEntity) target;
            if(user instanceof PlayerEntity) {
                ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Lethality] Enchant level: " + level);
                user.sendMessage(message, user.getUniqueID());
            }
            hitEntity.addPotionEffect(new EffectInstance(ModEffects.HEALTH_REDUCTION.get(), 600, level -1));
        }
    }
}
