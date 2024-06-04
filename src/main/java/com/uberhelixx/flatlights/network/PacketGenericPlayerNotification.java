package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGenericPlayerNotification {

    String messageText;

    /**
     * Displays a text notification to the player using the action bar, and plays an accompanying alert sound
     * @param textIn The text that is going to be displayed to the player
     */
    public PacketGenericPlayerNotification(String textIn) {
        messageText = textIn;
    }

    public static void encode(PacketGenericPlayerNotification msg, PacketBuffer buf) {
        buf.writeString(msg.messageText);
    }

    public static PacketGenericPlayerNotification decode(PacketBuffer buf) {
        return new PacketGenericPlayerNotification(buf.readString());
    }

    public static void handle(PacketGenericPlayerNotification msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                PlayerEntity player = Minecraft.getInstance().player;
                String notification = msg.messageText;
                if(player != null) {
                    if(FlatLightsClientConfig.genericNotificationText.get()) {
                        player.sendStatusMessage(ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + notification), true);
                    }
                    if(FlatLightsClientConfig.genericNotificationSound.get()) {
                        player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 10f);
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
