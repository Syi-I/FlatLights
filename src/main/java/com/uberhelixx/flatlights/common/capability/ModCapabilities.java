package com.uberhelixx.flatlights.common.capability;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.effect.ModEffects;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketEntangledUpdate;
import com.uberhelixx.flatlights.common.network.packets.PacketSyncPlayerCap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ModCapabilities {
    public static final Capability<IEntangled> ENTANGLED_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IRisingHeat> RISING_HEAT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IPlayerTracker> PLAYER_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    
    public static final Direction DEFAULT_FACING = null;
    
    /**
     * Get the {@link IEntangled} from the specified entity.
     * @param entity The entity
     * @return A lazy optional containing the IEntangled, if any
     */
    public static LazyOptional<IEntangled> getEntangledState(final LivingEntity entity) {
        if (entity == null) {
            return LazyOptional.empty();
        }
        return entity.getCapability(ENTANGLED_CAPABILITY);
    }
    
    /**
     * Get the {@link IRisingHeat} from the specified entity.
     * @param entity The entity
     * @return A lazy optional containing the IRisingHeat, if any
     */
    public static LazyOptional<IRisingHeat> getHeatedState(final LivingEntity entity) {
        if (entity == null) {
            return LazyOptional.empty();
        }
        return entity.getCapability(RISING_HEAT_CAPABILITY);
    }
    
    /**
     * Get the {@link IPlayerTracker} from the specified entity.
     * @param player The player
     * @return A lazy optional containing the IPlayerTracker, if any
     */
    public static LazyOptional<IPlayerTracker> getPlayerTracker(final Player player) {
        if (player == null) {
            return LazyOptional.empty();
        }
        return player.getCapability(PLAYER_TRACKER_CAPABILITY);
    }
    
    /**
     * Event handler for capabilities
     */
    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = FlatLights.MODID)
    public static class CapabilityEvents {
        /**
         * Attach capabilities to the appropriate things. Do attach filtering here, not in individual capability classes.
         * @param event The {@link AttachCapabilitiesEvent<Entity>} event
         */
        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            //FlatLights.LOGGER.info("[Attach Caps] Trying to attach all capabilities.");
            if(event.getObject() instanceof LivingEntity) {
                EntangledStateAttacher.attach(event);
                RisingHeatStateAttacher.attach(event);
            }
            //for some reason this ever only tries to attach to random UUIDs
            /*if(event.getObject() instanceof Player player) {
                FlatLights.LOGGER.info("[Attach Caps] Trying to add PlayerTrackerCap to " + player.getStringUUID());
                if(MiscUtils.uuidCheck(player.getUUID())) {
                    FlatLights.LOGGER.info("[Attach Caps] Added PlayerTrackerCap to " + player.getStringUUID());
                    PlayerTrackerCapAttacher.attach(event);
                }
            }*/
        }
        
        /**
         * Register the capabilities
         * @param event The {@link RegisterCapabilitiesEvent} event
         */
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(IEntangled.class);
            event.register(IRisingHeat.class);
            event.register(IPlayerTracker.class);
        }
        
        /**
         * Set the EntangledState of an entity to TRUE when the ENTANGLED effect is applied
         * @param event The event used for checking what {@link net.minecraft.world.effect.MobEffects} was applied
         */
        @SubscribeEvent
        public static void addEntangledEffect(MobEffectEvent.Added event) {
            //if mob has entangled effect now
            if(event.getEntity().hasEffect(ModEffects.ENTANGLED.get())) {
                LivingEntity entity = event.getEntity();
                //set mob to entangled state true
                if(getEntangledState(entity).isPresent()) {
                    getEntangledState(entity).ifPresent(entangledState -> {
                        entangledState.setEntangledState(true);
                        FlatLights.LOGGER.info("[Added potion effect] Changed entangled state to true");
                        if(!entity.level().isClientSide()) {
                            Supplier<Entity> supplier = () -> entity;
                            PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketEntangledUpdate(entity.getId(), true));
                        }
                    });
                }
            }
        }
        
        public static void syncPlayerCaps(Player playerIn) {
            IPlayerTracker cap = getPlayerTracker(playerIn).orElse(new PlayerTrackerCap());
            CompoundTag tag = cap.serializeNBT();
            if(playerIn instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendToPlayer(new PacketSyncPlayerCap(tag), serverPlayer);
            }
        }
        
        @SubscribeEvent
        public static void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
            if(event.getEntity() instanceof ServerPlayer serverPlayer) {
                syncPlayerCaps(serverPlayer);
            }
        }
        
        @SubscribeEvent
        public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
            if(event.getEntity() instanceof ServerPlayer serverPlayer) {
                syncPlayerCaps(serverPlayer);
            }
        }
        
        
        @SubscribeEvent
        public static void onPlayerStartTrackingEvent(PlayerEvent.StartTracking event) {
            if(event.getTarget() instanceof Player && event.getEntity() instanceof ServerPlayer serverPlayer) {
                syncPlayerCaps(serverPlayer);
            }
        }
        
        @SubscribeEvent
        public static void onPlayerDimChangedEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
            if(event.getEntity() instanceof ServerPlayer serverPlayer) {
                syncPlayerCaps(serverPlayer);
            }
        }
        
        /**
         * Copy the player's mana when they respawn after dying or returning from the end.
         * @param event The event
         */
        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            Player oldPlayer = event.getOriginal();
            oldPlayer.reviveCaps();
            
            //set our new tracker as the value of the old tracker
            getPlayerTracker(oldPlayer).ifPresent(oldPlayerTracker -> getPlayerTracker(event.getEntity()).ifPresent(newPlayerTracker -> {
                newPlayerTracker.setTracker(oldPlayerTracker.getTracker());
            }));
            //sync the capability
            getPlayerTracker(oldPlayer).ifPresent(oldPlayerTrackerCap -> {
                IPlayerTracker playerTrackerCap = getPlayerTracker(event.getEntity()).orElse(new PlayerTrackerCap());
                CompoundTag tag = oldPlayerTrackerCap.serializeNBT();
                playerTrackerCap.deserializeNBT(tag);
                syncPlayerCaps(event.getEntity());
            });
            event.getOriginal().invalidateCaps();
        }
        
        /**
         * Update the playerTracker capability on mob kills
         * @param event The event for when something is killed by the player with the tracker
         */
        /*@SubscribeEvent
        public static void coreSync(LivingDeathEvent event) {
            Entity mob = event.getEntity();
            Entity killer = event.getSource().getEntity();
            if(killer instanceof Player player && mob instanceof LivingEntity livingEntity) {
                //gained cores is equal to how many times more HP the mob had compared to the player's base 20 HP
                int gainedCores = Math.max((Math.round((livingEntity).getMaxHealth() / 20)), 1);
                
                //get the player tracker capability of this player if they have it
                getPlayerTracker(player).ifPresent(playerTracker -> {
                    IPlayerTracker playerTrackerCap = getPlayerTracker(player).orElse(new PlayerTrackerCap());
                    //increase the tracker by the gainedCores amount
                    playerTrackerCap.increaseTracker(gainedCores);
                });
            }
        }*/
        
    }
}
