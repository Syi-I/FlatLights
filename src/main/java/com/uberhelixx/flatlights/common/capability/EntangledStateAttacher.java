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

public class EntangledStateAttacher {
    public static class EntangledStateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation ID = new ResourceLocation(FlatLights.MODID, "entangled_state");
        private EntangledState entangledState = new EntangledState();
        private final LazyOptional<IEntangled> optionalData = LazyOptional.of(() -> entangledState);
        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            return ModCapabilities.ENTANGLED_CAPABILITY.orEmpty(capability, this.optionalData);
        }
        
        void invalidate() {
            this.optionalData.invalidate();
        }
        
        @Override
        public CompoundTag serializeNBT() {
            return this.entangledState.serializeNBT();
        }
        
        @Override
        public void deserializeNBT(CompoundTag tag) {
            this.entangledState.deserializeNBT(tag);
        }
    }
    
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        final EntangledStateProvider provider = new EntangledStateProvider();
        event.addCapability(EntangledStateProvider.ID, provider);
    }
}
