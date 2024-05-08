package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCurioToggleMessage {

    boolean toggleState;

    /**
     * Toggle notification for curios set effects only
     * @param stateIn True if being toggled on, false if being toggled off
     */
    public PacketCurioToggleMessage(boolean stateIn) {
        toggleState = stateIn;
    }

    public static void encode(PacketCurioToggleMessage msg, PacketBuffer buf) {
        buf.writeBoolean(msg.toggleState);
    }

    public static PacketCurioToggleMessage decode(PacketBuffer buf) {
        return new PacketCurioToggleMessage(buf.readBoolean());
    }

    public static void handle(PacketCurioToggleMessage msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                PlayerEntity player = Minecraft.getInstance().player;
                boolean state = msg.toggleState;
                if(player != null) {
                    if(state) {
                        if(FlatLightsClientConfig.curioToggleMessage.get()) {
                            player.sendStatusMessage(ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + "Toggled curio effect " + TextFormatting.GREEN + "on"), true);
                        }
                        if(FlatLightsClientConfig.curioToggleSound.get()) {
                            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.MODE_SWITCH.get(), SoundCategory.PLAYERS, 0.4f, 0.55f);
                        }
                    }
                    else {
                        if (FlatLightsClientConfig.curioToggleMessage.get()) {
                            player.sendStatusMessage(ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + "Toggled curio effect " + TextFormatting.RED + "off"), true);
                        }
                        if(FlatLightsClientConfig.curioToggleSound.get()) {
                            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.MODE_SWITCH.get(), SoundCategory.PLAYERS, 0.4f, 0.01f);
                        }
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
