package com.uberhelixx.flatlights.capability;

import net.minecraft.nbt.CompoundNBT;

public interface IEntangled {
    public static String ENTANGLED_KEY = "flatlights.entangled";
    
    default boolean isEntangled() {
        return false;
    }
    
    default void readEntangledState(CompoundNBT nbt) {
    }
    
    //default void setEntangledState(CompoundNBT nbt) {
    //}
    
    default void setEntangledState(boolean state) {
    }
}
