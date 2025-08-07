package com.uberhelixx.flatlights.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.entity.Mk2ProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.model.data.ModelData;

import java.awt.*;

public class Mk2ProjectileRenderer extends EntityRenderer<Mk2ProjectileEntity> {
    public static final ResourceLocation MK2_PROJECTILE = new ResourceLocation(FlatLights.MODID, "textures/item/prismatic_blademk2/main_blade.png");
    public static final ResourceLocation MK2_PROJECTILE_MODEL = new ResourceLocation(FlatLights.MODID, "item/prismatic_blademk2");
    
    public Mk2ProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    
    @Override
    public void render(Mk2ProjectileEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel mk2ProjectileModel = mc.getModelManager().getModel(MK2_PROJECTILE_MODEL);
        
        pPoseStack.pushPose();
        PoseStack.Pose currentPose = pPoseStack.last();
        
        final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
        final float TARGET_SIZE_WHEN_RENDERED = 1.5F;  // desired size when rendered (in metres)
        
        final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
        pPoseStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        
        pPoseStack.mulPose(Axis.YP.rotationDegrees(90 + Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot())));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(90 - Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        
        Color blendColor = Color.WHITE;
        float red = blendColor.getRed() / 255.0F;
        float green = blendColor.getGreen() / 255.0F;
        float blue = blendColor.getBlue() / 255.0F;
        
        // we're going to use the block renderer to render our model, even though it's not a block, because we baked
        //   our entity model as if it were a block model.
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        
        //VertexConsumer shaderBuffer = pBuffer.getBuffer(ModRenderTypes.SPACE);
        VertexConsumer shaderBuffer = pBuffer.getBuffer(RenderType.endPortal());
        dispatcher.getModelRenderer().renderModel(currentPose, shaderBuffer, null, mk2ProjectileModel,
                red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.entityCutoutNoCull(MK2_PROJECTILE));
        
        pPoseStack.popPose(); // restore the original transformation matrix + normals matrix
        
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight); // renders labels
    }
    
    @Override
    public ResourceLocation getTextureLocation(Mk2ProjectileEntity mk2ProjectileEntity) {
        return MK2_PROJECTILE;
    }
}
