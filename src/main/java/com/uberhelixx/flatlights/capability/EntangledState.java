package com.uberhelixx.flatlights.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class EntangledState implements IEntangled{
    private boolean entangled;
    private final LivingEntity entity;
    
    public EntangledState(final @Nullable LivingEntity entityIn) {
        this.entity = entityIn;
    }
    
    public boolean isEntangled() {
        return entangled;
    }
    
    public void readEntangledState(CompoundNBT nbt) {
        this.entangled = nbt.getBoolean(ENTANGLED_KEY);
    }
    
    /*
    public void setEntangledState(CompoundNBT nbt) {
        nbt.putBoolean(ENTANGLED_KEY, entangled);
    }*/
    
    public void setEntangledState(boolean state) {
        this.entangled = state;
    }
}
