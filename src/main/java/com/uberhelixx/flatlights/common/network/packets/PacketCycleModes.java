package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.common.item.IMultiModeItem;
import com.uberhelixx.flatlights.util.lib.LibTagKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCycleModes {
    public PacketCycleModes() {
    }
    
    public static void encode(PacketCycleModes msg, FriendlyByteBuf buf) {
    }
    
    public static PacketCycleModes decode(FriendlyByteBuf buf) {
        return new PacketCycleModes();
    }
    
    public static void handle(PacketCycleModes msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if(player != null) {
                    ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if(item.getItem() instanceof IMultiModeItem modeItem) {
                        CompoundTag tag = item.getTag();
                        if(tag != null) {
                            int mode = tag.getInt(LibTagKeys.MODE_TAG);
                            modeItem.incrMode(item);
                            modeItem.onModeChange(player, item);
                        }
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
