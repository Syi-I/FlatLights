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
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

public class EntangledStateProvider {

    public static final Capability<IEntangled> ENTANGLED_STATE_CAPABILITY = null;
    public static final Direction DEFAULT_SIDE = null;
    public static final ResourceLocation ID = new ResourceLocation(FlatLights.MOD_ID, "entangled");
    
    public static void register() {
        //register instance of entangled state
        CapabilityManager.INSTANCE.register(IEntangled.class, new Capability.IStorage<IEntangled>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IEntangled> capability, IEntangled instance, Direction side) {
                CompoundNBT tag = new CompoundNBT();
                tag.putBoolean(IEntangled.ENTANGLED_KEY, instance.isEntangled());
                return tag;
            }
            
            @Override
            public void readNBT(Capability<IEntangled> capability, IEntangled instance, Direction side, INBT nbt) {
                //make sure we input CompoundNBT before trying to cast to CompoundNBT
                if(!(nbt instanceof CompoundNBT)) {
                    return;
                }
                CompoundNBT tag = (CompoundNBT) nbt;
                //read entangled state
                instance.readEntangledState(tag);
            }
        }, () -> new EntangledState(null));
        
    }
    
    /**
     * Get the {@link IEntangled} from the specified entity.
     * @param entity The entity
     * @return A lazy optional containing the IEntangled, if any
     */
    public static LazyOptional<IEntangled> getEntangledState(final LivingEntity entity){
        return entity.getCapability(ENTANGLED_STATE_CAPABILITY, DEFAULT_SIDE);
    }
    
    public static ICapabilityProvider createProvider(final IEntangled entangled) {
        return new SerializableCapabilityProvider<>(ENTANGLED_STATE_CAPABILITY, DEFAULT_SIDE, entangled);
    }
    
    /**
     * Event handler for the {@link IEntangled} capability.
     */
    @Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
    private static class EventHandler {
        
        /**
         * Attach the {@link IEntangled} capability to all living entities.
         * @param event The event
         */
        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            //make sure we only put entangled state on living entities
            if (event.getObject() instanceof LivingEntity) {
                MiscHelpers.debugLogger("added entangled state to mob");
                final EntangledState state = new EntangledState((LivingEntity) event.getObject());
                event.addCapability(ID, createProvider(state));
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
