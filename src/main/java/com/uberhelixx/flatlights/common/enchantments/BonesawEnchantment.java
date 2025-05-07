package com.uberhelixx.flatlights.common.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.effect.ModEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Objects;

public class BonesawEnchantment extends Enchantment {
    protected BonesawEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        Style color = Style.EMPTY.withColor(4478069);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
    
    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        int stackCap = FlatLightsCommonConfig.bonesawStacks.get() - 1;
        LivingEntity target = null;
        if(pTarget instanceof LivingEntity) {
            target = (LivingEntity) pTarget;
        }
        int level = 0;
        //grab bonesaw enchantment level if any is applied
        if(pAttacker instanceof Player) {
            level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BONESAW.get(), pAttacker);
        }
        
        //check if target exists, enchant is applied, if damage is directly from player action
        if(target != null && level > 0) {
            if(target.hasEffect(ModEffects.ARMOR_SHRED.get())) {
                //if potion effect is active then get amplifier level (0-4 stacks of armor shred)
                int amplifier = Objects.requireNonNull(target.getEffect(ModEffects.ARMOR_SHRED.get())).getAmplifier();
                //Component message = Component.literal("[Armor Shred] Amplifier Level: " + amplifier);
                //((Player) pAttacker).displayClientMessage(message, true);
                //allow for reapplication if duration is low as otherwise bleed damage may never trigger from constantly being reapplied
                //allow for reapplication if next level of amplifier (aka bleed stacks present) is lower than the config stack cap
                target.addEffect(new MobEffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(amplifier + 1, stackCap)));
            }
            //base application if no armor shred effect is active, higher level increases starting stacks
            else {
                //Component message = Component.literal("[Armor Shred] Enchant Level: " + level);
                //((Player) pAttacker).displayClientMessage(message, true);
                target.addEffect(new MobEffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(level - 1, stackCap)));
            }
        }
    }
}
