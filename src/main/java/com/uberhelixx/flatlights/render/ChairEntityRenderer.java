package com.uberhelixx.flatlights.render;

import com.uberhelixx.flatlights.entity.ChairEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class ChairEntityRenderer extends EntityRenderer<ChairEntity> {

    public ChairEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(ChairEntity entity) {
        return null;
    }

}
