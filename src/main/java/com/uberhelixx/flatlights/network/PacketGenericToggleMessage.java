package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.util.ClientUtils;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGenericToggleMessage {

    boolean toggleState;
    String toggleText;
    boolean cycleSound;

    /**
     * Display a generic message in the player's action bar
     * @param textIn The display text for the message
     * @param stateIn Basic toggle on/off boolean if there are only two modes, determines sound pitch played
     * @param cycleIn If multiple modes, set to true to play cycle sound instead of on/off pitch sound
     */
    public PacketGenericToggleMessage(String textIn, boolean stateIn, boolean cycleIn) {
        toggleState = stateIn;
        toggleText = textIn;
        cycleSound = cycleIn;
    }
    public static void encode(PacketGenericToggleMessage msg, PacketBuffer buf) {
        buf.writeString(msg.toggleText);
        buf.writeBoolean(msg.toggleState);
        buf.writeBoolean(msg.cycleSound);
    }

    public static PacketGenericToggleMessage decode(PacketBuffer buf) {
        return new PacketGenericToggleMessage(buf.readString(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(PacketGenericToggleMessage msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> {
                //should be only clientside stuff
                PlayerEntity player = ClientUtils.getPlayer();
                boolean state = msg.toggleState;
                boolean cycleModes = msg.cycleSound;
                if(player != null) {
                    //sends the input text
                    if(FlatLightsClientConfig.genericToggleMessage.get()) {
                        player.sendStatusMessage(ITextComponent.getTextComponentOrEmpty(msg.toggleText), true);
                    }
                    //on state, higher pitch sound plays
                    if(state && !cycleModes) {
                        if(FlatLightsClientConfig.genericToggleSound.get()) {
                            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.MODE_SWITCH.get(), SoundCategory.PLAYERS, 0.4f, 0.55f);
                        }
                    }
                    //off state, lower pitch sound plays
                    else if(!state && !cycleModes){
                        if(FlatLightsClientConfig.genericToggleSound.get()) {
                            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.MODE_SWITCH.get(), SoundCategory.PLAYERS, 0.4f, 0.01f);
                        }
                    }
                    else {
                        if(FlatLightsClientConfig.genericToggleSound.get()) {
                            player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.MODE_SWITCH.get(), SoundCategory.PLAYERS, 0.4f, 1.0f);
                        }
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
