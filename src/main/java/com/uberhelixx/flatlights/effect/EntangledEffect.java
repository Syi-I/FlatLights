package com.uberhelixx.flatlights.effect;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EntangledEffect extends Effect {
    private static final String ENTANGLED_TEAM = "entangledMobs";

    protected EntangledEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.hurtResistantTime = 0;
        int dmgMultiplier = 1;
        if(amplifier > 0) {
            dmgMultiplier += amplifier;
        }
        entityLivingBaseIn.attackEntityFrom(ModDamageTypes.ENTANGLED, entityLivingBaseIn.getMaxHealth() * (0.1F * dmgMultiplier) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.entangledEndDmg.get()));
        if(entityLivingBaseIn.getTeam() == entityLivingBaseIn.getEntityWorld().getScoreboard().getTeam(ENTANGLED_TEAM)) {
            entityLivingBaseIn.getEntityWorld().getScoreboard().removePlayerFromTeam(entityLivingBaseIn.getCachedUniqueIdString(), entityLivingBaseIn.getEntityWorld().getScoreboard().getTeam(ENTANGLED_TEAM));
        }
        super.performEffect(entityLivingBaseIn, amplifier);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration <= 1;
    }

    public static String getEntangledTeam() {
        return ENTANGLED_TEAM;
    }

}
