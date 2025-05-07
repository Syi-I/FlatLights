package com.uberhelixx.flatlights.common.capability;

import net.minecraft.nbt.CompoundTag;

public class RisingHeatState implements IRisingHeat {
    private boolean heated;
    
    public RisingHeatState() {
        this(false);
    }
    
    public RisingHeatState(boolean state) {
        this.heated = state;
    }
    
    public boolean isHeated() {
        return heated;
    }
    
    public void readHeatState(CompoundTag nbt) {
        this.heated = nbt.getBoolean(RISING_HEAT_STATE);
    }
    
    public void setHeatState(boolean state) {
        this.heated = state;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(IRisingHeat.RISING_HEAT_STATE, this.isHeated());
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        setHeatState(tag.getBoolean(IRisingHeat.RISING_HEAT_STATE));
    }
}
