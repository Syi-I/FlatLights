package com.uberhelixx.flatlights.common.effect;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.capability.ModCapabilities;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketEntangledUpdate;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.network.PacketDistributor;

import java.util.Objects;
import java.util.function.Supplier;

public class EntangledEffect extends MobEffect {
    private static final String ENTANGLED_TEAM = "flatlights.entangledMobs";
    
    protected EntangledEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Scoreboard entityScoreboard = pLivingEntity.getCommandSenderWorld().getScoreboard();
        //in theory the person who applied the potion effect, since this only comes from weapons enchanted with Quantum Strike normally
        Entity trueSource = pLivingEntity.getLastDamageSource() != null ? pLivingEntity.getLastDamageSource().getEntity() : pLivingEntity;
        //resets invulnerability timer so damage is guaranteed to hit
        pLivingEntity.invulnerableTime = 0;
        //final hit damage multiplied by amplifier level
        int dmgMulti = 1;
        if(pAmplifier > 0) {
            dmgMulti += pAmplifier;
        }
        //deal damage to entity and remove from the entangled team so the glowing effect color is reset
        pLivingEntity.hurt(ModDamageTypes.causeEntangledDamage(trueSource), pLivingEntity.getMaxHealth() * (0.1F * dmgMulti) * MiscUtils.damagePercentCalc(FlatLightsCommonConfig.entangledEndDmg.get()));
        //remove this entity from the entangled team once the effect expires
        if(pLivingEntity.getTeam() != null) {
            if (pLivingEntity.getTeam() == entityScoreboard.getPlayerTeam(ENTANGLED_TEAM)) {
                entityScoreboard.removePlayerFromTeam(pLivingEntity.getStringUUID(), Objects.requireNonNull(entityScoreboard.getPlayerTeam(ENTANGLED_TEAM)));
            }
        }
        //set entangled boolean capability to false since the effect has expired
        if(ModCapabilities.getEntangledState(pLivingEntity).isPresent()) {
            ModCapabilities.getEntangledState(pLivingEntity).ifPresent(entangledState -> {
                entangledState.setEntangledState(false);
                MiscUtils.infoLog("[Entangled Effect] Changed entangled state to false");
                if(!pLivingEntity.getCommandSenderWorld().isClientSide()) {
                    Supplier<Entity> supplier = () -> pLivingEntity;
                    PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketEntangledUpdate(pLivingEntity.getId(), false));
                }
            });
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }
    
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration <= 1;
    }
    
    public static String getEntangledTeam() {
        return ENTANGLED_TEAM;
    }
}
