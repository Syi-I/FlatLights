package com.uberhelixx.flatlights.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BasicColorParticleOptions implements ParticleOptions {
    @Getter
    private final BasicColorParticleConstructor data;
    
    private BasicColorParticleOptions(int color, float diameter, int lifetime, float roll) {
        this.data = BasicColorParticleConstructor.builder()
                .color(color)
                .diameter(diameter)
                .lifetime(lifetime)
                .roll(roll)
                .build();
    }
    
    public BasicColorParticleOptions(BasicColorParticleConstructor data) {
        this.data = data;
    }
    
    @Nonnull
    @Override
    public ParticleType<BasicColorParticleOptions> getType() {
        return ModParticles.BASIC_COLOR.get();
    }
    
    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(data.getColor().getRGB());
        buf.writeFloat(data.getDiameter());
        buf.writeInt(data.getLifetime());
        buf.writeFloat(data.getRoll());
    }
    
    @Nonnull
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %.2f %d %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), data.getColor().getRGB(), data.getDiameter(), data.getLifetime(), data.getRoll());
    }
    
    public static final Codec<BasicColorParticleOptions> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(options -> options.getData().getColor().getRGB()),
                    Codec.FLOAT.fieldOf("diameter").forGetter(options -> options.getData().getDiameter()),
                    Codec.INT.fieldOf("lifetime").forGetter(options -> options.getData().getLifetime()),
                    Codec.FLOAT.fieldOf("roll").forGetter(options -> options.getData().getRoll())
            ).apply(instance, BasicColorParticleOptions::new));
    
    public static final ParticleOptions.Deserializer<BasicColorParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        @Nonnull
        @Override
        public BasicColorParticleOptions fromCommand(@Nonnull ParticleType<BasicColorParticleOptions> particleType, @Nonnull StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            int color = stringReader.readInt();
            
            stringReader.expect(' ');
            float diameter = stringReader.readFloat();
            
            stringReader.expect(' ');
            int lifetime = stringReader.readInt();
            
            stringReader.expect(' ');
            float roll = stringReader.readFloat();
            
            return new BasicColorParticleOptions(color, diameter, lifetime, roll);
        }
        
        
        @Override
        public BasicColorParticleOptions fromNetwork(@Nonnull ParticleType<BasicColorParticleOptions> type, FriendlyByteBuf buf) {
            int color = buf.readInt();
            float diameter = buf.readFloat();
            int lifetime = buf.readInt();
            float roll = buf.readFloat();
            
            return new BasicColorParticleOptions(color, diameter, lifetime, roll);
        }
    };
}
