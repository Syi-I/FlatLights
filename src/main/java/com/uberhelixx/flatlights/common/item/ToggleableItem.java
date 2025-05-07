package com.uberhelixx.flatlights.common.item;

import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.lib.LibTagKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ToggleableItem {
    /**
     * Checks if the input item is enabled or not
     * @param stack The {@link ItemStack} that the {@link CompoundTag} is being taken from to check the toggle state
     * @return A {@code boolean} value for the toggled state of the item ({@code true} for enabled/{@code false} for disabled)
     */
    default boolean isToggledOn(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        //check if MODE tag exists, add if it doesn't
        if(!tag.contains(LibTagKeys.MODE_TAG)) {
            tag.putBoolean(LibTagKeys.MODE_TAG, true);
        }
        return tag.getBoolean(LibTagKeys.MODE_TAG);
    }
    
    /**
     * Changes the toggle state of the input item and plays a notification to the player indicating the changed toggle state
     * @param stack The {@link ItemStack} that is being toggled
     * @param player The {@link Player} that is toggling the item
     */
    static void toggleEnabled(ItemStack stack, Player player) {
        //get mode boolean tag and invert its value to change the toggle state of the item
        CompoundTag tag = stack.getOrCreateTag();
        boolean enabled = tag.contains(LibTagKeys.MODE_TAG) ? !(tag.getBoolean(LibTagKeys.MODE_TAG)) : true;
        //update the toggle state of the item
        tag.putBoolean(LibTagKeys.MODE_TAG, enabled);
        stack.setTag(tag);
        //send packet to update the tag on serverside
        if(player.level().isClientSide()) {
            PacketHandler.sendToServer(new PacketWriteNbt(tag, stack));
        }
        //send player notification to indicate that the item toggle has changed
        player.displayClientMessage(enabled ? Component.translatable("flatlights.enabled") : Component.translatable("flatlights.disabled"), true);
        MiscUtils.modeSwitchSound(player, enabled);
    }
    
    static ItemStack getToggleableItem(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if(mainHand.getItem() instanceof ToggleableItem) {
            return mainHand;
        }
        ItemStack offHand = player.getOffhandItem();
        if(offHand.getItem() instanceof ToggleableItem) {
            return offHand;
        }
        return ItemStack.EMPTY;
    }
}
