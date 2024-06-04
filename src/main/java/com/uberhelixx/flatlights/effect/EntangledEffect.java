package com.uberhelixx.flatlights.effect;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.capability.EntangledStateProvider;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.network.PacketEntangledUpdate;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

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
        //deal damage to entity and remove from the entangled team so the glowing effect color is reset
        entityLivingBaseIn.attackEntityFrom(ModDamageTypes.ENTANGLED, entityLivingBaseIn.getMaxHealth() * (0.1F * dmgMultiplier) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.entangledEndDmg.get()));
        if(entityLivingBaseIn.getTeam() != null) {
            if (entityLivingBaseIn.getTeam() == entityLivingBaseIn.getEntityWorld().getScoreboard().getTeam(ENTANGLED_TEAM)) {
                entityLivingBaseIn.getEntityWorld().getScoreboard().removePlayerFromTeam(entityLivingBaseIn.getCachedUniqueIdString(), entityLivingBaseIn.getEntityWorld().getScoreboard().getTeam(ENTANGLED_TEAM));
            }
        }
        if(EntangledStateProvider.getEntangledState(entityLivingBaseIn).isPresent()) {
            EntangledStateProvider.getEntangledState(entityLivingBaseIn).ifPresent(entangledState -> {
                entangledState.setEntangledState(false);
                MiscHelpers.debugLogger("[entangled effect] changed entangled state to false");
                
            });
            if(!entityLivingBaseIn.getEntityWorld().isRemote()) {
                Supplier<Entity> supplier = () -> entityLivingBaseIn;
                PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketEntangledUpdate(entityLivingBaseIn.getEntityId(), false));
            }
            
        }
        super.performEffect(entityLivingBaseIn, amplifier);
    }

    //trigger effect as the effect is expiring
    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration <= 1;
    }

    public static String getEntangledTeam() {
        return ENTANGLED_TEAM;
    }

}
