package com.uberhelixx.flatlights.common.enchantments;

import com.uberhelixx.flatlights.common.effect.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LethalityEnchantment extends Enchantment {
    protected LethalityEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        Style color = Style.EMPTY.withColor(6957095);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
    
    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if(pTarget instanceof LivingEntity hitEntity) {
            /*if(pAttacker instanceof Player) {
                Component message = Component.literal("[Lethality] Enchant Level: " + pLevel);
                ((Player) pAttacker).displayClientMessage(message, true);
            }*/
            hitEntity.addEffect(new MobEffectInstance(ModEffects.HEALTH_REDUCTION.get(), 600, pLevel - 1));
        }
    }
}
