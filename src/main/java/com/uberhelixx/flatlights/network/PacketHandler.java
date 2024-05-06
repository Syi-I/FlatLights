package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
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
    }

    public static void sendToPlayer(ServerPlayerEntity playerMP, Object toSend) {
        HANDLER.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
        if (playerMP.server.isDedicatedServer() || !playerMP.getGameProfile().getName().equals(playerMP.server.getServerOwner())) {
            sendToPlayer(playerMP, toSend);
        }
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }

    private PacketHandler() {}
}
