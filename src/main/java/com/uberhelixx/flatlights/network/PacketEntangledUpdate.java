package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.capability.EntangledState;
import com.uberhelixx.flatlights.capability.EntangledStateCapability;
import com.uberhelixx.flatlights.render.EffectRenderer;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import javafx.scene.effect.Effect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEntangledUpdate {
    
    int entityID;
    boolean state;
    
    public PacketEntangledUpdate(int id, boolean stateIn) {
        entityID = id;
        state = stateIn;
    }
    public static void encode(PacketEntangledUpdate msg, PacketBuffer buf) {
        buf.writeInt(msg.entityID);
        buf.writeBoolean(msg.state);
    }

    public static PacketEntangledUpdate decode(PacketBuffer buf) {
        return new PacketEntangledUpdate(buf.readInt(), buf.readBoolean());
    }

    public static void handle(PacketEntangledUpdate msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                PlayerEntity player = Minecraft.getInstance().player;
                boolean state = msg.state;
                if(player != null) {
                    Entity entity = player.getEntityWorld().getEntityByID(msg.entityID);
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.getCapability(EntangledStateCapability.CAPABILITY_ENTANGLED_STATE).ifPresent(entangledState -> {
                            entangledState.setEntangledState(state);
                        });
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
