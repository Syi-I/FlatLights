package com.uberhelixx.flatlights.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class EntangledState implements IEntangled{
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
    
    public void readEntangledState(CompoundNBT nbt) {
        this.entangled = nbt.getBoolean(ENTANGLED_KEY);
    }
    
    public void setEntangledState(boolean state) {
        this.entangled = state;
    }
    
    public static EntangledState createDefaultInstance() {
        return new EntangledState();
    }
    
    public static class EntangledStateNBTStorage implements Capability.IStorage<EntangledState> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<EntangledState> capability, EntangledState instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean(IEntangled.ENTANGLED_KEY, instance.isEntangled());
            return tag;
        }
        
        @Override
        public void readNBT(Capability<EntangledState> capability, EntangledState instance, Direction side, INBT nbt) {
            //make sure we input CompoundNBT before trying to cast to CompoundNBT
            if(!(nbt instanceof CompoundNBT)) {
                return;
            }
            CompoundNBT tag = (CompoundNBT) nbt;
            //read entangled state
            instance.readEntangledState(tag);
        }
    }
}
