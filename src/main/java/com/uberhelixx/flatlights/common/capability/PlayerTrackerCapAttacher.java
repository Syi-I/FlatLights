package com.uberhelixx.flatlights.common.capability;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTrackerCapAttacher {
    public static class PlayerTrackerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation ID = new ResourceLocation(FlatLights.MODID, "player_core_tracker");
        private PlayerTrackerCap playerTrackerCap = new PlayerTrackerCap();
        private final LazyOptional<IPlayerTracker> optionalData = LazyOptional.of(() -> playerTrackerCap);
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            return ModCapabilities.PLAYER_TRACKER_CAPABILITY.orEmpty(capability, this.optionalData);
        }
        
        void invalidate() {
            this.optionalData.invalidate();
        }
        
        @Override
        public CompoundTag serializeNBT() {
            return this.playerTrackerCap.serializeNBT();
        }
        
        @Override
        public void deserializeNBT(CompoundTag tag) {
            this.playerTrackerCap.deserializeNBT(tag);
        }
    }
    
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final PlayerTrackerProvider provider = new PlayerTrackerProvider();
        event.addCapability(PlayerTrackerProvider.ID, provider);
    }
}
