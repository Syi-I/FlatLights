package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {
    public static KeyBinding CURIO_TOGGLE;
    public static void register(final FMLClientSetupEvent event) {
        CURIO_TOGGLE = create("curio_toggle", KeyEvent.VK_G);

        ClientRegistry.registerKeyBinding(CURIO_TOGGLE);
    }

    private static KeyBinding create(String name, int key) {
        return new KeyBinding("key." + FlatLights.MOD_ID + "." + name, key, "key.category." + FlatLights.MOD_ID);
    }
}
