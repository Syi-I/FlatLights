package com.uberhelixx.flatlights.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.uberhelixx.flatlights.util.lib.LibTagKeys.MODE_TAG;

public interface IMultiModeItem {
    default CompoundTag getOrCreateModeTag(ItemStack stack) {
        return stack.getOrCreateTag();
    }
    
    /**
     * Get the current mode of an item.
     * @param stack ItemStack to get the mode of.
     * @return the int value read from the item's mode tag.
     */
    default int getMode(ItemStack stack) {
        return getOrCreateModeTag(stack).getInt(MODE_TAG);
    }
    
    /**
     * Attempt to set the state of the item.
     * @param stack ItemStack to set the mode on.
     * @param mode  Desired mode.
     * @return TRUE if the operation was successful, FALSE if it was not.
     */
    default boolean setMode(ItemStack stack, int mode) {
        if (getNumModes(stack) <= 1) {
            mode = 0;
        }
        if (mode < getNumModes(stack)) {
            getOrCreateModeTag(stack).putInt(MODE_TAG, mode);
            return true;
        }
        return false;
    }
    
    /**
     * Increment the current mode of an item.
     * @param stack The item whose mode is being changed.
     * @return TRUE if the mode got cycled, false if this item has no modes.
     */
    default boolean incrMode(ItemStack stack) {
        if (getNumModes(stack) <= 1) {
            return false;
        }
        int currMode = getMode(stack);
        ++currMode;
        if (currMode >= getNumModes(stack)) {
            currMode = 0;
        }
        getOrCreateModeTag(stack).putInt(MODE_TAG, currMode);
        return true;
    }
    
    /**
     * Decrement the current mode of an item.
     * @param stack The item whose mode is being changed.
     * @return TRUE if the mode got cycled, false if this item has no modes.
     */
    default boolean decrMode(ItemStack stack) {
        //only decrement if more than two modes, otherwise act as a toggle
        if (getNumModes(stack) <= 2) {
            return false;
        }
        int currMode = getMode(stack);
        --currMode;
        if (currMode < 0) {
            currMode = getNumModes(stack) - 1;
        }
        getOrCreateModeTag(stack).putInt(MODE_TAG, currMode);
        return true;
    }
    
    /**
     * Returns the number of possible modes.
     * @param stack The item whose number of modes is being checked.
     * @return The number of modes available
     */
    default int getNumModes(ItemStack stack) {
        return 2;
    }
    
    /**
     * Callback method for reacting to a state change. Useful in KeyBinding handlers.
     * @param player Player holding the item, if applicable.
     * @param stack  The item being held.
     */
    default void onModeChange(Player player, ItemStack stack) {
    
    }
}
