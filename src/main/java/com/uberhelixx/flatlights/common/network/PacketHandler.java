package com.uberhelixx.flatlights.common.network;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.network.packets.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String CHANNELS = "0";
    private static final String CHANNEL_NAME = "channel";
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(FlatLights.MODID, CHANNEL_NAME),
            () -> CHANNELS,
            CHANNELS::equals,
            CHANNELS::equals
    );
    
    public static void init() {
        int id = 0;
        HANDLER.registerMessage(id++, PacketWriteNbt.class, PacketWriteNbt::encode, PacketWriteNbt::decode, PacketWriteNbt::handle);
        HANDLER.registerMessage(id++, PacketLeftClick.class, PacketLeftClick::encode, PacketLeftClick::decode, PacketLeftClick::handle);
        HANDLER.registerMessage(id++, PacketEntangledUpdate.class, PacketEntangledUpdate::encode, PacketEntangledUpdate::decode, PacketEntangledUpdate::handle);
        HANDLER.registerMessage(id++, PacketRisingHeatUpdate.class, PacketRisingHeatUpdate::encode, PacketRisingHeatUpdate::decode, PacketRisingHeatUpdate::handle);
        HANDLER.registerMessage(id++, PacketCurioToggle.class, PacketCurioToggle::encode, PacketCurioToggle::decode, PacketCurioToggle::handle);
        HANDLER.registerMessage(id++, PacketSyncPlayerCap.class, PacketSyncPlayerCap::encode, PacketSyncPlayerCap::decode, PacketSyncPlayerCap::handle);
        HANDLER.registerMessage(id++, PacketAirstrafeMovement.class, PacketAirstrafeMovement::encode, PacketAirstrafeMovement::decode, PacketAirstrafeMovement::handle);
    }
    
    /**
     * Send a packet to the player client
     * @param msg The message packet to send
     * @param player The server player entity who is sending this message
     */
    public static void sendToPlayer(Object msg, ServerPlayer player) {
        if (!(player instanceof FakePlayer))
            HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    /**
     * Send a packet to all player clients (ignores fake players)
     * @param msg The message packet to send
     * @param level The world which is used to get all players
     */
    public static void sendToAll(Object msg, Level level) {
        for (Player player : level.players()) {
            if (!(player instanceof FakePlayer))
                HANDLER.sendTo(msg, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
    
    /**
     * Sends a vanilla packet to the given player
     *
     * @param player Player
     * @param packet Packet
     *               Stolen from Tinkers Construct :)
     */
    public static void sendVanillaPacket(Entity player, Packet<?> packet) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(packet);
        }
    }
    
    /**
     * Send a packet to the server from the client
     * @param msg The message packet to send
     */
    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }
    
    /**
     * Send a packet to a specific {@link PacketDistributor}
     * @param packetTarget The distributor that we are sending the packet to
     * @param toSend The message packet to send
     */
    public static void sendToDistributor(PacketDistributor.PacketTarget packetTarget, Object toSend) {
        HANDLER.send(packetTarget, toSend);
    }
}
