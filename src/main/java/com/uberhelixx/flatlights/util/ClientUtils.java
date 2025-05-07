package com.uberhelixx.flatlights.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientUtils {
    
    /**
     * Gets the Minecraft instance
     * @return The {@link Minecraft} instance
     */
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
    
    /**
     * Gets the player from the {@link Minecraft} instance
     * @return The {@link Player}
     */
    public static Player getPlayer() {
        return getMinecraft().player;
    }
    
    /**
     * Gets the level from the {@link Player}
     * @return The {@link Level}
     */
    public static Level getPlayerLevel() {
        return getPlayer().level();
    }
    
    /**
     * Gets the level from the {@link Minecraft} instance
     * @return The {@link Level}
     */
    public static Level getLevel() {
        return getMinecraft().level;
    }
}
