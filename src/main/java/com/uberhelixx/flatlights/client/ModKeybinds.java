package com.uberhelixx.flatlights.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {
    public static final ModKeybinds INSTANCE = new ModKeybinds();
    
    private ModKeybinds() {}
    
    private static final String CATEGORY = "key.categories." + FlatLights.MODID;
    
    public final KeyMapping CURIO_TOGGLE = new KeyMapping(
            "key." + FlatLights.MODID + ".curio_toggle",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V, -1),
            CATEGORY
    );
}
