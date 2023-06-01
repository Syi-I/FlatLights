package com.uberhelixx.flatlights.effect;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
    public static final DeferredRegister<Effect> POTIONS
            = DeferredRegister.create(ForgeRegistries.POTIONS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}
