package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.network.PacketCurioToggle;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.util.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        onInput(mc, event.getKey(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action) {
        //this one is for toggling the curio set effect on or off
        //first checks curio slot, then main hand if nothing in the slot
        if (mc.currentScreen == null && ModKeybinds.CURIO_TOGGLE.isPressed()) {
            PacketHandler.sendToServer(new PacketCurioToggle());
        }
    }
}
