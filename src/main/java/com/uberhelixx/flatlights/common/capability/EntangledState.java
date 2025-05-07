package com.uberhelixx.flatlights.common.capability;

import net.minecraft.nbt.CompoundTag;

public class EntangledState implements IEntangled {
    private boolean entangled;
    
    public EntangledState() {
        this(false);
    }
    
    public EntangledState(boolean state) {
        this.entangled = state;
    }
    
    public boolean isEntangled() {
        return entangled;
    }
    
    public void readEntangledState(CompoundTag nbt) {
        this.entangled = nbt.getBoolean(ENTANGLED_KEY);
    }
    
    public void setEntangledState(boolean state) {
        this.entangled = state;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(IEntangled.ENTANGLED_KEY, this.isEntangled());
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        setEntangledState(tag.getBoolean(IEntangled.ENTANGLED_KEY));
    }
}
