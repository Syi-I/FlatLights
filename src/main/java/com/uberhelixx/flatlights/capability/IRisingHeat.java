package com.uberhelixx.flatlights.capability;

import net.minecraft.nbt.CompoundNBT;

public interface IRisingHeat {
    public static final String RISING_HEAT_KEY = "flatlights.rising_heat";
    
    default boolean isHeated() {
        return false;
    }
    
    default void readHeatState(CompoundNBT nbt) {
    }
    
    default void setHeatState(boolean state) {
    }
}
