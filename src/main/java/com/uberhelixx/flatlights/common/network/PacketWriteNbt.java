package com.uberhelixx.flatlights.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketWriteNbt {
    public CompoundNBT tagToWrite;
    public ItemStack playerItem;
    public PacketWriteNbt() {

    }
    public PacketWriteNbt(CompoundNBT tag, ItemStack itemIn) {
        tagToWrite = tag;
        playerItem = itemIn;
    }

    public static void encode(PacketWriteNbt msg, PacketBuffer buf) {
        buf.writeCompoundTag(msg.tagToWrite);
        buf.writeItemStack(msg.playerItem);
    }

    public static PacketWriteNbt decode(PacketBuffer buf) {
        return new PacketWriteNbt(buf.readCompoundTag(), buf.readItemStack());
    }

    public static void handle(PacketWriteNbt msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
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
