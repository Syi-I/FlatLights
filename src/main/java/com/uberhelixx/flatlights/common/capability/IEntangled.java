package com.uberhelixx.flatlights.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEntangled extends INBTSerializable<CompoundTag> {
    public static final String ENTANGLED_KEY = "flatlights.entangled";
    
    default boolean isEntangled() {
        return false;
    }
    
    default void readEntangledState(CompoundTag nbt) {
    }
    
    default void setEntangledState(boolean state) {
    }
}
