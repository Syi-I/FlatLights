package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.common.item.tools.PrismaticSword;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLeftClick {
    public static void encode(PacketLeftClick msg, FriendlyByteBuf buf) {}
    
    public static PacketLeftClick decode(FriendlyByteBuf buf) {
        return new PacketLeftClick();
    }
    
    public static void handle(PacketLeftClick msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                Player player = ctx.get().getSender();
                assert player != null;
                ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
                Level level = player.level();
                
                PrismaticSword.throwBomb(player, heldItem);
                PrismaticBladeMk2.shootProjectile(level, player, player.getOnPos());
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
