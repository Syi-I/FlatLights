package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.common.capability.IPlayerTracker;
import com.uberhelixx.flatlights.common.capability.ModCapabilities;
import com.uberhelixx.flatlights.common.capability.PlayerTrackerCap;
import com.uberhelixx.flatlights.util.ClientUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPlayerCap {
    CompoundTag tag;
    public PacketSyncPlayerCap() {
    
    }
    
    public PacketSyncPlayerCap(CompoundTag tagIn) {
        this.tag = tagIn;
    }
    
    public static void encode(PacketSyncPlayerCap msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }
    
    public static PacketSyncPlayerCap decode(FriendlyByteBuf buf) {
        return new PacketSyncPlayerCap(buf.readNbt());
    }
    
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player playerEntity = ClientUtils.getPlayer();
            IPlayerTracker cap = ModCapabilities.getPlayerTracker(playerEntity).orElse(new PlayerTrackerCap());
            
            if (cap != null) {
                cap.deserializeNBT(tag);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
