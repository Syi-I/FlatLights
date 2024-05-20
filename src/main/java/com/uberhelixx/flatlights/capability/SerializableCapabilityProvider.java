package com.uberhelixx.flatlights.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class SerializableCapabilityProvider<HANDLER> extends SimpleCapabilityProvider<HANDLER> implements INBTSerializable<INBT> {
    
    /**
     * Create a provider for the default handler instance.
     * @param capability The Capability instance to provide the handler for
     * @param side     The Direction to provide the handler for
     */
    public SerializableCapabilityProvider(final Capability<HANDLER> capability, final @Nullable Direction side) {
        this(capability, side, capability.getDefaultInstance());
    }
    
    /**
     * Create a provider for the specified handler instance.
     * @param capability The Capability instance to provide the handler for
     * @param side The Direction to provide the handler for
     * @param instance The handler instance to provide
     */
    public SerializableCapabilityProvider(final Capability<HANDLER> capability, final @Nullable Direction side, final @Nullable HANDLER instance) {
        super(capability, side, instance);
    }
    
    @Override
    public INBT serializeNBT() {
        final HANDLER instance = getInstance();
        
        if(instance == null) {
            return null;
        }
        if(getCapability() == null) {
            return new CompoundNBT();
        }
        return getCapability().writeNBT(instance, getSide());
    }
    
    @Override
    public void deserializeNBT(final INBT nbt) {
        final HANDLER instance = getInstance();
        
        if (instance == null) {
            return;
        }
        getCapability().readNBT(instance, getSide(), nbt);
    }
}
