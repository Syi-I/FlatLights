package com.uberhelixx.flatlights.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.entity.PortableBlackHoleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class PortableBlackHoleRenderer extends EntityRenderer<PortableBlackHoleEntity> {
    public static final ResourceLocation SPHERE = new ResourceLocation(FlatLights.MODID, "textures/block/void_sphere/void_sphere.png");
    public static final ResourceLocation SPHERE_MODEL = new ResourceLocation(FlatLights.MODID, "entity/void_sphere_wrapper");
    
    public PortableBlackHoleRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    
    @Override
    public void render(PortableBlackHoleEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        BakedModel sphereModel = Minecraft.getInstance().getModelManager().getModel(SPHERE_MODEL);
        
        pPoseStack.pushPose();
        PoseStack.Pose currentPose = pPoseStack.last();
        
        final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
        final float TARGET_SIZE_WHEN_RENDERED = 0.75F;  // desired size when rendered (in metres)
        
        final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
        pPoseStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        pPoseStack.translate(0, 0.65, 0);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-1 * pEntity.tickCount));
        float flicker = pEntity.tickCount % 2 * 0.08f;
        pPoseStack.scale(1 - flicker, 1 - flicker, 1 - flicker);
        
        Color blendColor = Color.WHITE;
        float red = blendColor.getRed() / 255.0F;
        float green = blendColor.getGreen() / 255.0F;
        float blue = blendColor.getBlue() / 255.0F;
        
        // we're going to use the block renderer to render our model, even though it's not a block, because we baked
        //   our entity model as if it were a block model.
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        
        VertexConsumer vertexBuffer = pBuffer.getBuffer(RenderType.solid());
        dispatcher.getModelRenderer().renderModel(currentPose, vertexBuffer, null, sphereModel,
                red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY);
        
        pPoseStack.popPose(); // restore the original transformation matrix + normals matrix
        
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight); // renders labels
    }
    
    @Override
    public ResourceLocation getTextureLocation(PortableBlackHoleEntity portableBlackHoleEntity) {
        return SPHERE;
    }
}
