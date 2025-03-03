package com.uberhelixx.flatlights.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientUtils {
    
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
    
    public static Player getPlayer() {
        return getMinecraft().player;
    }
    
    public static Level getWorld() {
        return getPlayer().level();
    }
}
