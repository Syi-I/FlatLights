package com.uberhelixx.flatlights.common.effect;

import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class BleedEffect extends MobEffect {
    protected BleedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Entity trueSource = pLivingEntity.getLastDamageSource() != null ? pLivingEntity.getLastDamageSource().getEntity() : pLivingEntity;
        
        int dmgMultiplier = 1;
        if(pAmplifier > 0) {
            dmgMultiplier += pAmplifier;
        }
        pLivingEntity.hurt(ModDamageTypes.causeBleedDamage(trueSource), pLivingEntity.getMaxHealth() * (0.03F * dmgMultiplier));
        
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }
    
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 40 == 0;
    }
}
