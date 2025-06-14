package com.uberhelixx.flatlights.client.particle;

import lombok.Builder;
import lombok.Data;

import java.awt.*;

@Data
@Builder
public class BasicColorParticleConstructor {
    @Builder.Default
    private Color color;
    
    @Builder.Default
    private float diameter = 1F;
    
    @Builder.Default
    private float roll = 0F;
    
    @Builder.Default
    private boolean physical = true;
    
    @Builder.Default
    private int lifetime = 20;
    
    @Builder.Default
    private float scaleModifier = 1F;
    
    public static class BasicColorParticleConstructorBuilder {
        private Color color = new Color(0xFFFFFFFF, true);
        
        public BasicColorParticleConstructorBuilder color(int color) {
            this.color = new Color(color, true);
            
            return this;
        }
        
        public BasicColorParticleConstructorBuilder color(float r, float g, float b, float a) {
            return this.color(new Color(r, g, b, a).getRGB());
        }
        
        public BasicColorParticleConstructorBuilder color(float r, float g, float b) {
            return this.color(r, g, b, 1F);
        }
        
        public BasicColorParticleConstructorBuilder color(int r, int g, int b, int a) {
            return this.color(r / 255F, g / 255F, b / 255F, a / 255F);
        }
        
        public BasicColorParticleConstructorBuilder color(int r, int g, int b) {
            return this.color(r, g, b, 0xFF);
        }
    }
}
