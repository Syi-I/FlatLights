package com.uberhelixx.flatlights.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.Nullable;

public class BasicColorParticleFactory implements ParticleProvider<BasicColorParticleOptions> {
    private final SpriteSet sprites;
    
    @Nullable
    @Override
    public Particle createParticle(BasicColorParticleOptions pOptions, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        BasicColorParticle particle = new BasicColorParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pOptions.getData());
        
        particle.pickSprite(sprites);
        
        return particle;
    }
    
    public BasicColorParticleFactory(SpriteSet sprite) {
        this.sprites = sprite;
    }
    
    public static class Type extends ParticleType<BasicColorParticleOptions> {
        public Type() {
            super(false, BasicColorParticleOptions.DESERIALIZER);
        }
        
        @Override
        public Codec<BasicColorParticleOptions> codec() {
            return BasicColorParticleOptions.CODEC;
        }
    }
}
