package com.uberhelixx.flatlights.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class BleedEffect extends Effect {
    protected BleedEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.hurtResistantTime = 0;
        int dmgMultiplier = 1;
        if(amplifier > 0) {
            dmgMultiplier += amplifier;
        }
        entityLivingBaseIn.attackEntityFrom(DamageSource.GENERIC, entityLivingBaseIn.getMaxHealth() * (0.03F * dmgMultiplier));

        super.performEffect(entityLivingBaseIn, amplifier);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 3 == 0;
    }

}
