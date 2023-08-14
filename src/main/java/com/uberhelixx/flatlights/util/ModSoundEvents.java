package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(FlatLights.MOD_ID, name)));
    }

    public static final RegistryObject<SoundEvent> SQUEAK = registerSoundEvent("squeak");
    public static final RegistryObject<SoundEvent> DEV_BLADE_MODE_SWITCH = registerSoundEvent("dev_blade_mode_switch");
    public static final RegistryObject<SoundEvent> VOID_PROJECTILE_SHOT = registerSoundEvent("void_projectile_shot");
}