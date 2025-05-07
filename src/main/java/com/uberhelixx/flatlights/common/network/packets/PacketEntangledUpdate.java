package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.common.capability.ModCapabilities;
import com.uberhelixx.flatlights.util.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEntangledUpdate {
    int entityID;
    boolean state;
    public PacketEntangledUpdate(int id, boolean stateIn) {
        entityID = id;
        state = stateIn;
    }
    
    public static void encode(PacketEntangledUpdate msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityID);
        buf.writeBoolean(msg.state);
    }
    
    public static PacketEntangledUpdate decode(FriendlyByteBuf buf) {
        return new PacketEntangledUpdate(buf.readInt(), buf.readBoolean());
    }
    
    public static void handle(PacketEntangledUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                Player player = ClientUtils.getPlayer();
                boolean state = msg.state;
                if(player != null) {
                    Entity entity = player.level().getEntity(msg.entityID);
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.getCapability(ModCapabilities.ENTANGLED_CAPABILITY).ifPresent(entangledState -> {
                            entangledState.setEntangledState(state);
                        });
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
