package com.uberhelixx.flatlights.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RisingHeatState implements IRisingHeat{
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
    
    public void readHeatState(CompoundNBT nbt) {
        this.heated = nbt.getBoolean(RISING_HEAT_KEY);
    }
    
    public void setHeatState(boolean state) {
        this.heated = state;
    }
    
    public static RisingHeatState createDefaultInstance() {
        return new RisingHeatState();
    }
    
    public static class HeatedStateNBTStorage implements Capability.IStorage<RisingHeatState> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<RisingHeatState> capability, RisingHeatState instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean(IRisingHeat.RISING_HEAT_KEY, instance.isHeated());
            return tag;
        }
        
        @Override
        public void readNBT(Capability<RisingHeatState> capability, RisingHeatState instance, Direction side, INBT nbt) {
            //make sure we input CompoundNBT before trying to cast to CompoundNBT
            if(!(nbt instanceof CompoundNBT)) {
                return;
            }
            CompoundNBT tag = (CompoundNBT) nbt;
            //read heat state
            instance.readHeatState(tag);
        }
    }
}
