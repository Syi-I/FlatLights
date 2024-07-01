package com.uberhelixx.flatlights.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientUtils {
    
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
    
    public static PlayerEntity getPlayer() {
        return getMinecraft().player;
    }
    
    public static World getWorld() {
        return getPlayer().world;
    }
}
