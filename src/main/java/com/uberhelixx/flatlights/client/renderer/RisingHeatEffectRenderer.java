package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.capability.RisingHeatStateProvider;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RisingHeatEffectRenderer extends LayerRenderer<LivingEntity, EntityModel<LivingEntity>> {
    public RisingHeatEffectRenderer(IEntityRenderer<LivingEntity, EntityModel<LivingEntity>> entityModel) {
        super(entityModel);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, LivingEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        //cannot tell what active effects are on an entity clientside, need some sort of packet serverside to ask and track this first?
        //using capabilities, track a state of the mob updating the capability on both serverside and clientside synced together, then can read that state to determine if the effect should render
        //shows up on anything but slimes so far, ig it has another layer that renders above this one
        if(RisingHeatStateProvider.getHeatedState(entityIn).isPresent()) {
            RisingHeatStateProvider.getHeatedState(entityIn).ifPresent(heated -> {
                if(heated.isHeated()) {
                    float tickTime = (float) entityIn.ticksExisted + partialTicks;
                    EntityModel<LivingEntity> entityModel = this.getEntityModel();
                    entityModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
                    this.getEntityModel().copyModelAttributesTo(entityModel);
                    IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEnergySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                    entityModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    float rgbChannels = 0.5f  + 0.35f * MathHelper.cos(tickTime / 5);
                    entityModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, rgbChannels, rgbChannels, rgbChannels, 1.0F);
                }
            });
        }
    }

    ResourceLocation HEATED_LAYER = new ResourceLocation(FlatLights.MOD_ID, "textures/models/power_layers/rising_heat_layer.png");

    private ResourceLocation layerTexture() {
        return HEATED_LAYER;
    }

    private float layerPosition(float f) {
        return MathHelper.sin(f * 0.02F) * 3.0F;
        //return f * 0.015f;
        //return MathHelper.cos(f * 0.02F) * 3.0F;
    }
    
}

