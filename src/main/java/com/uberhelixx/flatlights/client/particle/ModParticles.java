package com.uberhelixx.flatlights.client.particle;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticles {
    
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FlatLights.MODID);
    
    public static final RegistryObject<ParticleType<BasicColorParticleOptions>> BASIC_COLOR = PARTICLES.register("basic_color", BasicColorParticleFactory.Type::new);
    
    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
    
    @SubscribeEvent
    public static void onParticleRegistry(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BASIC_COLOR.get(), BasicColorParticleFactory::new);
    }
}
