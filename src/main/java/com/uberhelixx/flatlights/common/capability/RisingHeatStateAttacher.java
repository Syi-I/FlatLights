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

public class RisingHeatStateAttacher {
    public static class RisingHeatStateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation ID = new ResourceLocation(FlatLights.MODID, "rising_heat_state");
        private RisingHeatState risingHeatState = new RisingHeatState();
        private final LazyOptional<IRisingHeat> optionalData = LazyOptional.of(() -> risingHeatState);
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            return ModCapabilities.RISING_HEAT_CAPABILITY.orEmpty(capability, this.optionalData);
        }
        
        void invalidate() {
            this.optionalData.invalidate();
        }
        
        @Override
        public CompoundTag serializeNBT() {
            return this.risingHeatState.serializeNBT();
        }
        
        @Override
        public void deserializeNBT(CompoundTag tag) {
            this.risingHeatState.deserializeNBT(tag);
        }
    }
    
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final RisingHeatStateProvider provider = new RisingHeatStateProvider();
        event.addCapability(RisingHeatStateProvider.ID, provider);
    }
}
