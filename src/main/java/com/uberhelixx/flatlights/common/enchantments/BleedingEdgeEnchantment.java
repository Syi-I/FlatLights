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

public class BleedingEdgeEnchantment extends Enchantment {
    protected BleedingEdgeEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public Component getFullname(int pLevel) {
        Style color = Style.EMPTY.withColor(12336436);
        return ((MutableComponent)super.getFullname(pLevel)).withStyle(color);
    }
    
    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        int stackCap = FlatLightsCommonConfig.bleedStacks.get() - 1;
        LivingEntity target = null;
        if(pTarget instanceof LivingEntity) {
            target = (LivingEntity) pTarget;
        }
        int level = 0;
        //grab bleeding edge enchantment level if any is applied
        if(pAttacker instanceof Player) {
            level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLEEDING_EDGE.get(), pAttacker);
        }
        
        //check if target exists, enchant is applied, if damage is directly from player action
        if(target != null && level > 0) {
            if(target.hasEffect(ModEffects.BLEED.get())) {
                //if potion effect is active then get amplifier level (0-4) and duration left (in ticks)
                int amplifier = Objects.requireNonNull(target.getEffect(ModEffects.BLEED.get())).getAmplifier();
                int duration = Objects.requireNonNull(target.getEffect(ModEffects.BLEED.get())).getDuration();
                //Component message = Component.literal("[Bleeding Edge] Amplifier Level: " + amplifier);
                //((Player) pAttacker).displayClientMessage(message, true);
                //allow for reapplication if duration is low as otherwise bleed damage may never trigger from constantly being reapplied
                //allow for reapplication if next level of amplifier (aka bleed stacks present) is lower than the config stack cap
                if(duration <= (5 * 20) || amplifier + 1 <= stackCap) {
                    target.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 600, Math.min(amplifier + 1, stackCap)));
                }
            }
            //base application if no bleed effect is active
            else {
                //Component message = Component.literal("[Bleeding Edge] Enchant Level: " + level);
                //((Player) pAttacker).displayClientMessage(message, true);
                target.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), 600, Math.min(level - 1, stackCap)));
            }
        }
    }
}
