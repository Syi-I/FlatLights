package com.uberhelixx.flatlights.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerTracker extends INBTSerializable<CompoundTag> {
    //used for checking and remembering core count value
    public static final String PLAYER_CORETRACKER_KEY = "flatlights.core_tracker";
    
    /**
     * Gets the tracker value of the player
     * @return The tracker value of the player as an {@link Integer}
     */
    default Integer getTracker() {
        return 0;
    }
    
    /**
     * Set the tracker value of the player
     * @param value The updated tracker value of the player as an {@link Integer}
     */
    default void setTracker(Integer value) {
    
    }
    
    /**
     * Increase the tracker value of the player
     * @param amount The amount that the tracker should be increased by
     */
    default void increaseTracker(Integer amount) {
    
    }
}
