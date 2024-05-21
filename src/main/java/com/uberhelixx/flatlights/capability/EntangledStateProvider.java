package com.uberhelixx.flatlights.capability;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntangledStateProvider implements ICapabilitySerializable<INBT> {
    
    public static final ResourceLocation ID = new ResourceLocation(FlatLights.MOD_ID, "entangled_state");
    private final Direction NO_SPECIFIC_SIDE = null;
    private EntangledState entangledState = new EntangledState();
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (EntangledStateCapability.CAPABILITY_ENTANGLED_STATE == cap) {
            return (LazyOptional<T>)LazyOptional.of(()-> entangledState);
        }
        
        return LazyOptional.empty();
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        INBT entangled = EntangledStateCapability.CAPABILITY_ENTANGLED_STATE.writeNBT(entangledState, NO_SPECIFIC_SIDE);
        tag.put(IEntangled.ENTANGLED_KEY, entangled);
        return tag;
    }
    
    @Override
    public void deserializeNBT(INBT nbt) {
        if(nbt.getId() != new CompoundNBT().getId()) {
            return;
        }
        CompoundNBT tag = (CompoundNBT) nbt;
        INBT data = tag.get(IEntangled.ENTANGLED_KEY);
        EntangledStateCapability.CAPABILITY_ENTANGLED_STATE.readNBT(entangledState, NO_SPECIFIC_SIDE, data);
    }
    
    public static LazyOptional<EntangledState> getEntangledState(LivingEntity entityIn) {
        return entityIn.getCapability(EntangledStateCapability.CAPABILITY_ENTANGLED_STATE);
    }
    
    /**
     * Event handler for the {@link IEntangled} capability.
     */
    @Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
    public static class EntangledStateProviderEventHandler {
        
        /**
         * Attach the {@link IEntangled} capability to all living entities.
         * @param event The event
         */
        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            //make sure we only put entangled state on living entities
            if (event.getObject() instanceof LivingEntity) {
                MiscHelpers.debugLogger("added entangled state to mob");
                event.addCapability(ID, new EntangledStateProvider());
            }
        }
        
        @SubscribeEvent
        public static void addEntangledEffect(PotionEvent.PotionAddedEvent event) {
            //if mob has entangled effect now
            if(event.getEntityLiving().isPotionActive(ModEffects.ENTANGLED.get())) {
                LivingEntity entity = event.getEntityLiving();
                //set mob to entangled state true
                if(getEntangledState(entity).isPresent()) {
                    getEntangledState(entity).ifPresent(entangledState -> {
                        entangledState.setEntangledState(true);
                    });
                    MiscHelpers.debugLogger("changed entangled state to true");
                    
                }
            }
        }
        
        @SubscribeEvent
        public static void removeEntangledEffect(PotionEvent.PotionRemoveEvent event) {
            //check if the mob is removing entangled potion effect
            if(event.getPotion() != null && event.getPotion().equals(ModEffects.ENTANGLED.get())) {
                LivingEntity entity = event.getEntityLiving();
                //check entangled state capability and set to false
                if(getEntangledState(entity).isPresent()) {
                    getEntangledState(entity).ifPresent(entangledState -> {
                        entangledState.setEntangledState(false);
                    });
                    MiscHelpers.debugLogger("changed entangled state to false");
                    
                }
            }
        }
    }
}
