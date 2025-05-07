package com.uberhelixx.flatlights.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.capability.ModCapabilities;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class RisingHeatEffectRenderer extends RenderLayer<LivingEntity, EntityModel<LivingEntity>> {
    public RisingHeatEffectRenderer(RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> pRenderer) {
        super(pRenderer);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, LivingEntity livingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        //cannot tell what active effects are on an entity clientside, need some sort of packet serverside to ask and track this first?
        //using capabilities, track a state of the mob updating the capability on both serverside and clientside synced together, then can read that state to determine if the effect should render
        //shows up on anything but slimes so far, ig it has another layer that renders above this one
        if(ModCapabilities.getHeatedState(livingEntity).isPresent()) {
            ModCapabilities.getHeatedState(livingEntity).ifPresent(heated -> {
                if(heated.isHeated()) {
                    float tickTime = (float) livingEntity.tickCount + pPartialTicks;
                    EntityModel<LivingEntity> entityModel = this.getParentModel();
                    entityModel.prepareMobModel(livingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
                    this.getParentModel().copyPropertiesTo(entityModel);
                    VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(RenderType.energySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                    entityModel.setupAnim(livingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                    float rgbChannels = 0.5f  + 0.35f * Mth.cos(tickTime / 5);
                    entityModel.renderToBuffer(poseStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, rgbChannels, rgbChannels, rgbChannels, 1.0F);
                }
            });
        }
    }
    
    ResourceLocation HEATED_LAYER = new ResourceLocation(FlatLights.MODID, "textures/models/power_layers/rising_heat_layer.png");
    
    private ResourceLocation layerTexture() {
        return HEATED_LAYER;
    }
    
    private float layerPosition(float f) {
        //1.20.1 uses Mth instead of MathHelper now
        return Mth.sin(f * 0.02F) * 3.0F;
        //return f * 0.015f;
        //return MathHelper.cos(f * 0.02F) * 3.0F;
    }
}
