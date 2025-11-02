package com.uberhelixx.flatlights.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.client.event.RegisterShaderEvent;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModRenderTypes {
    protected static final RenderStateShard.OverlayStateShard OVERLAY = new RenderStateShard.OverlayStateShard(true);
    protected static final RenderStateShard.OverlayStateShard NO_OVERLAY = new RenderStateShard.OverlayStateShard(false);
    protected static final RenderStateShard.TransparencyStateShard TRANSPARENT =
            new RenderStateShard.TransparencyStateShard("translucent_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
            },
            RenderSystem::disableBlend);
    
    public static final ResourceLocation SPACE_TEX = new ResourceLocation(FlatLights.MODID, "textures/environment/space.png");
    
    public static boolean isOculusPresent() {
        return net.minecraftforge.fml.ModList.get().isLoaded("oculus");
    }
    
    public static final RenderType SPACE = RenderType.create(
            "space",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> RegisterShaderEvent.spaceShader))
                    .setTextureState(new RenderStateShard.TextureStateShard(SPACE_TEX, false, false))
                    .createCompositeState(true)
    );
    
    public static RenderType getSpace(ResourceLocation texture, ResourceLocation mask) {
        return RenderType.create(
                "space",
                DefaultVertexFormat.POSITION,
                VertexFormat.Mode.QUADS,
                100000,
                true,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(() -> RegisterShaderEvent.spaceShader))
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        //.setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(texture, false, false).add(mask, false, false).build())
                        //.setTextureState(new RenderStateShard.TextureStateShard(mask, true, true))
                        //.setOverlayState(OVERLAY)
                        //.setTransparencyState(TRANSPARENT)
                        //.setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                        //.setCullState(new RenderStateShard.CullStateShard(false))
                        .createCompositeState(true)
        );
    }
}
