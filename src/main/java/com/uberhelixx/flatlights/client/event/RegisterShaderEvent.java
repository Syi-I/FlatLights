package com.uberhelixx.flatlights.client.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterShaderEvent {
    public static ShaderInstance spaceShader;
    
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                        new ResourceLocation(FlatLights.MODID, "skybox"),
                        DefaultVertexFormat.POSITION),
                (e) -> spaceShader = e);
    }
}
