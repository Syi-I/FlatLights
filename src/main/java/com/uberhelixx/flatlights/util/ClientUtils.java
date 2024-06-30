package com.uberhelixx.flatlights.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientUtils {
    
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
    
    public static PlayerEntity getPlayer() {
        return getMinecraft().player;
    }
}
