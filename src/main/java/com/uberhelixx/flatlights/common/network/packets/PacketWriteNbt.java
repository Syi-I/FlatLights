package com.uberhelixx.flatlights.common.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketWriteNbt {
    public CompoundTag tagToWrite;
    public ItemStack playerItem;
    public PacketWriteNbt() {
    
    }
    public PacketWriteNbt(CompoundTag tag, ItemStack itemIn) {
        tagToWrite = tag;
        playerItem = itemIn;
    }
    
    public static void encode(PacketWriteNbt msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tagToWrite);
        //idk what the shareTag is vs the regular tag so I'm using the regular tag
        buf.writeItemStack(msg.playerItem, false);
    }
    
    public static PacketWriteNbt decode(FriendlyByteBuf buf) {
        return new PacketWriteNbt(buf.readNbt(), buf.readItem());
    }
    
    public static void handle(PacketWriteNbt msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if(player != null && msg.tagToWrite != null) {
                    //ItemStack item = player.getHeldItem(Hand.MAIN_HAND);
                    ItemStack item = msg.playerItem;
                    item.setTag(msg.tagToWrite);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
