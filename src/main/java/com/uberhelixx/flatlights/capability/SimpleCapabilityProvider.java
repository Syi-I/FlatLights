package com.uberhelixx.flatlights.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleCapabilityProvider<HANDLER> implements ICapabilityProvider {
    /**
     * The {@link Capability} instance to provide the handler for
     */
    protected final Capability<HANDLER> capability;
    
    /**
     * The {@link Direction} to provide the handler for
     */
    protected final Direction side;
    
    /**
     * The handler instance to provide
     */
    protected final HANDLER instance;
    
    /**
     * A lazy optional containing handler instance to provide
     */
    protected final LazyOptional<HANDLER> lazyOptional;
    
    public SimpleCapabilityProvider(final Capability<HANDLER> capability, final Direction side, final HANDLER instance) {
        this.capability = capability;
        this.side = side;
        this.instance = instance;

        if(this.instance != null) {
            lazyOptional = LazyOptional.of(() -> this.instance);
        }
        else {
            lazyOptional = LazyOptional.empty();
        }
    }
    
    /**
     * Retrieves the handler for the capability requested on the specific side.
     * The return value CAN be {@code null} if the object does not support the capability.
     * The return value CAN be the same for multiple faces.
     * @param capability The capability to check
     * @param side The Side to check from: Can be {@code null}. Null is defined to represent 'internal' or 'self'
     * @return A lazy optional containing the handler, if this object supports the capability.
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(final @Nonnull Capability<T> capability, final @Nullable Direction side) {
        if(getCapability() == null) {
            return LazyOptional.empty();
        }
        return getCapability().orEmpty(capability, lazyOptional);
    }
    
    /**
     * Get the {@link Capability} instance to provide the handler for
     * @return The Capability instance
     */
    public final Capability<HANDLER> getCapability() {
        return capability;
    }
    
    /**
     * Get the {@link Direction} to provide the handler for
     * @return The Direction to provide the handler for
     */
    @Nullable
    public Direction getSide() {
        return side;
    }
    
    /**
     * Get the handler instance
     * @return A lazy optional containing the handler instance
     */
    @Nullable
    public final HANDLER getInstance() {
        return instance;
    }
}
