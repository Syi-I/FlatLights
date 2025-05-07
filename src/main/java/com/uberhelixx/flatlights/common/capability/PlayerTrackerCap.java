package com.uberhelixx.flatlights.common.capability;

import net.minecraft.nbt.CompoundTag;

public class PlayerTrackerCap implements IPlayerTracker {
    private Integer tracker;
    
    //default instance
    public PlayerTrackerCap() {
        this(0);
    }
    
    public PlayerTrackerCap(Integer currentTrackerValue) {
        this.tracker = currentTrackerValue;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(IPlayerTracker.PLAYER_CORETRACKER_KEY, this.getTracker());
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        setTracker(tag.getInt(IPlayerTracker.PLAYER_CORETRACKER_KEY));
    }
    
    @Override
    public Integer getTracker() {
        return this.tracker;
    }
    
    @Override
    public void setTracker(Integer value) {
        this.tracker = value;
    }
    
    @Override
    public void increaseTracker(Integer amount) {
        this.setTracker(this.getTracker() + amount);
    }
}
