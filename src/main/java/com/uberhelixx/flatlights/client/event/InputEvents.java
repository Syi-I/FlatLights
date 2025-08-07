package com.uberhelixx.flatlights.client.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.client.ModKeybinds;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketAirstrafeMovement;
import com.uberhelixx.flatlights.common.network.packets.PacketCurioToggle;
import com.uberhelixx.flatlights.common.network.packets.PacketCycleModes;
import com.uberhelixx.flatlights.common.network.packets.PacketWriteNbt;
import com.uberhelixx.flatlights.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
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
    
    //handles what happens for each keybind press
    private static void onInput(Minecraft mc, int key, int action) {
        //this one is for toggling the curio set effect on or off
        //first checks curio slot, then main hand if nothing in the slot
        if (mc.screen == null && ModKeybinds.INSTANCE.CURIO_TOGGLE.isDown()) {
            PacketHandler.sendToServer(new PacketCurioToggle());
        }
        
        if (mc.screen == null && ModKeybinds.INSTANCE.MODE_CYCLE.isDown()) {
            PacketHandler.sendToServer(new PacketCycleModes());
        }
    }
    
    //@SubscribeEvent
    public static void airstrafeMovement(TickEvent.PlayerTickEvent.ClientTickEvent event) {
        LocalPlayer localPlayer = (LocalPlayer) ClientUtils.getPlayer();
        Minecraft mc = ClientUtils.getMinecraft();
        if (localPlayer != null) {
            //raw keybinding checks
            /*boolean forwardDown  = mc.options.keyUp.isDown();
            boolean backDown     = mc.options.keyDown.isDown();
            boolean leftDown     = mc.options.keyLeft.isDown();
            boolean rightDown    = mc.options.keyRight.isDown();
            boolean jumpDown     = mc.options.keyJump.isDown();
            boolean sprintDown   = mc.options.keySprint.isDown();
            boolean sneakDown    = mc.options.keyShift.isDown();
            */
            
            //movement vector from the MovementInput object
            float forward    = localPlayer.input.forwardImpulse;    // positive = forward, negative = back
            float strafe     = localPlayer.input.leftImpulse;  // positive = left, negative = right
            boolean jumping  = localPlayer.input.jumping;
            boolean sneaking = localPlayer.input.shiftKeyDown;
           PacketHandler.sendToServer(new PacketAirstrafeMovement(forward, strafe, jumping, sneaking));
           //localPlayer.addDeltaMovement(PacketAirstrafeMovement.processStrafes(forward, strafe));
           PacketAirstrafeMovement.processStrafes(forward, strafe, localPlayer);
        }
    }
}
