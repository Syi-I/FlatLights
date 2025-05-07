package com.uberhelixx.flatlights.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.entity.BombEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BombProjectileRenderer extends EntityRenderer<BombEntity> {
    //models that use block model renderer have to have textures in 'textures/block' specifically or else it ends up with missing textures
    public static final ResourceLocation BOMB = new ResourceLocation(FlatLights.MODID, "textures/block/bomb_projectile/bomb_base.png");
    //make sure to register the model in client setup via ModelEvent.RegisterAdditional event, otherwise missing model happens
    public static final ResourceLocation BOMB_MODEL = new ResourceLocation(FlatLights.MODID, "entity/bomb_projectile");
    
    public BombProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    
    @Override
    public void render(BombEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        BakedModel bombModel = Minecraft.getInstance().getModelManager().getModel(BOMB_MODEL);
        
        pPoseStack.pushPose();
        PoseStack.Pose currentPose = pPoseStack.last();
        
        final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
        final float TARGET_SIZE_WHEN_RENDERED = 1.0F;  // desired size when rendered (in metres)
        
        final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
        pPoseStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-15 * pEntity.tickCount));
        pPoseStack.translate(-0.5, 0.0, -0.5);
        
        Color blendColor = Color.WHITE;
        float red = blendColor.getRed() / 255.0F;
        float green = blendColor.getGreen() / 255.0F;
        float blue = blendColor.getBlue() / 255.0F;
        
        // we're going to use the block renderer to render our model, even though it's not a block, because we baked
        //   our entity model as if it were a block model.
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        
        VertexConsumer vertexBuffer = pBuffer.getBuffer(RenderType.solid());
        dispatcher.getModelRenderer().renderModel(currentPose, vertexBuffer, null, bombModel,
                red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY);
        
        pPoseStack.popPose(); // restore the original transformation matrix + normals matrix
        
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight); // renders labels
    }
    
    @Override
    @NotNull
    public ResourceLocation getTextureLocation(BombEntity bombEntity) {
        return BOMB;
    }
}
