package com.uberhelixx.flatlights.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRisingHeat extends INBTSerializable<CompoundTag> {
    public static final String RISING_HEAT_STATE = "flatlights.rising_heat";
    
    default boolean isHeated() {
        return false;
    }
    
    default void readHeatState(CompoundTag nbt) {
    }
    
    default void setHeatState(boolean state) {
    }
}
