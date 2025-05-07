package com.uberhelixx.flatlights.client.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.client.ModKeybinds;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketCurioToggle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }
    
    private static void onInput(Minecraft mc, int key, int action) {
        //this one is for toggling the curio set effect on or off
        //first checks curio slot, then main hand if nothing in the slot
        if (mc.screen == null && ModKeybinds.INSTANCE.CURIO_TOGGLE.isDown()) {
            PacketHandler.sendToServer(new PacketCurioToggle());
        }
    }
}
