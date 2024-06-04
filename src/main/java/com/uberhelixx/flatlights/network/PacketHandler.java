package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler {

    private static final String CHANNELS = "10";
    private static final String CHANNEL_NAME = "channel";
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(FlatLights.MOD_ID, CHANNEL_NAME),
            () -> CHANNELS,
            CHANNELS::equals,
            CHANNELS::equals
    );

    //can only have as many messages as CHANNELS is equal to
    public static void init() {
        int id = 0;
        HANDLER.registerMessage(id++, PacketLeftClick.class, PacketLeftClick::encode, PacketLeftClick::decode, PacketLeftClick::handle);
        HANDLER.registerMessage(id++, PacketWriteNbt.class, PacketWriteNbt::encode, PacketWriteNbt::decode, PacketWriteNbt::handle);
        HANDLER.registerMessage(id++, PacketCurioToggle.class, PacketCurioToggle::encode, PacketCurioToggle::decode, PacketCurioToggle::handle);
        HANDLER.registerMessage(id++, PacketCurioToggleMessage.class, PacketCurioToggleMessage::encode, PacketCurioToggleMessage::decode, PacketCurioToggleMessage::handle);
        HANDLER.registerMessage(id++, PacketGenericToggleMessage.class, PacketGenericToggleMessage::encode, PacketGenericToggleMessage::decode, PacketGenericToggleMessage::handle);
        HANDLER.registerMessage(id++, PacketGenericPlayerNotification.class, PacketGenericPlayerNotification::encode, PacketGenericPlayerNotification::decode, PacketGenericPlayerNotification::handle);
        HANDLER.registerMessage(id++, PacketReachHit.class, PacketReachHit::encode, PacketReachHit::decode, PacketReachHit::handle);
        HANDLER.registerMessage(id++, PacketEntangledUpdate.class, PacketEntangledUpdate::encode, PacketEntangledUpdate::decode, PacketEntangledUpdate::handle);
        HANDLER.registerMessage(id++, PacketRisingHeatUpdate.class, PacketRisingHeatUpdate::encode, PacketRisingHeatUpdate::decode, PacketRisingHeatUpdate::handle);
    }
    
    /**
     * Send a packet to the client player
     * @param playerMP The server player entity who is sending this message
     * @param toSend The message packet to send
     */
    public static void sendToPlayer(ServerPlayerEntity playerMP, Object toSend) {
        HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
    
    /**
     * Send a packet to a specific {@link PacketDistributor}
     * @param packetTarget The distributor that we are sending the packet to
     * @param toSend The message packet to send
     */
    public static void sendToDistributor(PacketDistributor.PacketTarget packetTarget, Object toSend) {
        HANDLER.send(packetTarget, toSend);
    }
    
    /**
     * Send a packet to any player that is not the server host
     * @param playerMP The player client that we are sending the message to
     * @param toSend The message packet to send
     */
    public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
        if (playerMP.server.isDedicatedServer() || !playerMP.getGameProfile().getName().equals(playerMP.server.getServerOwner())) {
            sendToPlayer(playerMP, toSend);
        }
    }
    
    /**
     * Send a packet to the server from the client
     * @param msg The message packet to send
     */
    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }

    private PacketHandler() {}
}
