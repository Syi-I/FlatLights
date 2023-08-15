package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.tools.PrismaticSword;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLeftClick {
    public static void encode(PacketLeftClick msg, PacketBuffer buf) {}

    public static PacketLeftClick decode(PacketBuffer buf) {
        return new PacketLeftClick();
    }

    public static void handle(PacketLeftClick msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> PrismaticSword.throwBomb(ctx.get().getSender(), ctx.get().getSender().getHeldItem(Hand.MAIN_HAND)));
        }
        ctx.get().setPacketHandled(true);
    }
}
