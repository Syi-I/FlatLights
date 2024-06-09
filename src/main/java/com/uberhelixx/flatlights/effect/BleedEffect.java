package com.uberhelixx.flatlights.effect;

import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BleedEffect extends Effect {
    protected BleedEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        Entity trueSource = entityLivingBaseIn.getLastDamageSource() != null ? entityLivingBaseIn.getLastDamageSource().getTrueSource() : entityLivingBaseIn;

        int dmgMultiplier = 1;
        if(amplifier > 0) {
            dmgMultiplier += amplifier;
        }
        entityLivingBaseIn.attackEntityFrom(ModDamageTypes.causeIndirectBleed(trueSource, trueSource), entityLivingBaseIn.getMaxHealth() * (0.03F * dmgMultiplier));

        super.performEffect(entityLivingBaseIn, amplifier);
    }

    //potion effect should trigger every 2 seconds of duration (40 ticks = 2 seconds)
    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 40 == 0;
    }

}
