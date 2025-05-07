package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.common.capability.ModCapabilities;
import com.uberhelixx.flatlights.util.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRisingHeatUpdate {
    int entityID;
    boolean state;
    public PacketRisingHeatUpdate(int id, boolean stateIn) {
        entityID = id;
        state = stateIn;
    }
    
    public static void encode(PacketRisingHeatUpdate msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityID);
        buf.writeBoolean(msg.state);
    }
    
    public static PacketRisingHeatUpdate decode(FriendlyByteBuf buf) {
        return new PacketRisingHeatUpdate(buf.readInt(), buf.readBoolean());
    }
    
    public static void handle(PacketRisingHeatUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                Player player = ClientUtils.getPlayer();
                boolean state = msg.state;
                if(player != null) {
                    Entity entity = player.level().getEntity(msg.entityID);
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.getCapability(ModCapabilities.RISING_HEAT_CAPABILITY).ifPresent(heatedState -> {
                            heatedState.setHeatState(state);
                        });
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
