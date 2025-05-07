package com.uberhelixx.flatlights.client.renderer.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.capability.ModCapabilities;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PlayerEntangledEffectRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerEntangledEffectRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, AbstractClientPlayer abstractClientPlayer, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        //cannot tell what active effects are on an entity clientside, need some sort of packet serverside to ask and track this first?
        //using capabilities, track a state of the mob updating the capability on both serverside and clientside synced together, then can read that state to determine if the effect should render
        //shows up on anything but slimes so far, ig it has another layer that renders above this one
        if(ModCapabilities.getEntangledState(abstractClientPlayer).isPresent()) {
            ModCapabilities.getEntangledState(abstractClientPlayer).ifPresent(entangled -> {
                if(entangled.isEntangled()) {
                    float tickTime = (float) abstractClientPlayer.tickCount + pPartialTicks;
                    EntityModel<AbstractClientPlayer> playerModel = this.getParentModel();
                    playerModel.prepareMobModel(abstractClientPlayer, pLimbSwing, pLimbSwingAmount, pPartialTicks);
                    this.getParentModel().copyPropertiesTo(playerModel);
                    VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(RenderType.energySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                    playerModel.setupAnim(abstractClientPlayer, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                    float rgbChannels = 0.75f;
                    playerModel.renderToBuffer(poseStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, rgbChannels, rgbChannels, rgbChannels, 1.0F);
                }
            });
        }
    }
    
    ResourceLocation ENTANGLED_LAYER = new ResourceLocation(FlatLights.MODID, "textures/models/power_layers/entangled_layer.png");
    
    private ResourceLocation layerTexture() {
        return ENTANGLED_LAYER;
    }
    
    private float layerPosition(float f) {
        //1.20.1 uses Mth instead of MathHelper now
        return Mth.cos(f * 0.02F) * 3.0F + Mth.sin(f * 0.02F) * 3.0F;
        //return f * 0.015f;
        //return MathHelper.cos(f * 0.02F) * 3.0F;
    }
}
