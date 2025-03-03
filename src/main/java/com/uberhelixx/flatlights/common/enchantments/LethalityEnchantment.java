package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.effect.ModEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.effect.MobEffectInstance;
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
