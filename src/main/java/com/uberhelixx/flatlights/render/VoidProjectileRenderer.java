package com.uberhelixx.flatlights.render;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.entity.VoidProjectileEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class VoidProjectileRenderer extends ArrowRenderer<VoidProjectileEntity> {
    public static final ResourceLocation projectile = new ResourceLocation(FlatLights.MOD_ID, "textures/entity/void_projectile.png");

    public VoidProjectileRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(VoidProjectileEntity entity) {
        return projectile;
    }
}
