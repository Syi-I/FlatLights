package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.capability.EntangledStateCapability;
import com.uberhelixx.flatlights.capability.RisingHeatStateCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRisingHeatUpdate {
    
    int entityID;
    boolean state;
    
    public PacketRisingHeatUpdate(int id, boolean stateIn) {
        entityID = id;
        state = stateIn;
    }
    public static void encode(PacketRisingHeatUpdate msg, PacketBuffer buf) {
        buf.writeInt(msg.entityID);
        buf.writeBoolean(msg.state);
    }

    public static PacketRisingHeatUpdate decode(PacketBuffer buf) {
        return new PacketRisingHeatUpdate(buf.readInt(), buf.readBoolean());
    }

    public static void handle(PacketRisingHeatUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                PlayerEntity player = Minecraft.getInstance().player;
                boolean state = msg.state;
                if(player != null) {
                    Entity entity = player.getEntityWorld().getEntityByID(msg.entityID);
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.getCapability(RisingHeatStateCapability.CAPABILITY_HEATED_STATE).ifPresent(heatedState -> {
                            heatedState.setHeatState(state);
                        });
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
