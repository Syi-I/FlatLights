package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.entity.BombSwingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.awt.*;

public class BombSwingProjectileRenderer extends EntityRenderer<BombSwingEntity> {
    public static final ResourceLocation BOMB = new ResourceLocation(FlatLights.MOD_ID, "textures/models/bomb_projectile/bomb_base.png");
    public static final ResourceLocation BOMB_MODEL = new ResourceLocation(FlatLights.MOD_ID, "entity/bomb_swing_projectile");

    public BombSwingProjectileRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(BombSwingEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        IBakedModel sphereModel = Minecraft.getInstance().getModelManager().getModel(BOMB_MODEL);

        matrixStackIn.push();
        MatrixStack.Entry currentMatrix = matrixStackIn.getLast();

        final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
        final float TARGET_SIZE_WHEN_RENDERED = 1.0F;  // desired size when rendered (in metres)

        final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
        matrixStackIn.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-15 * entityIn.ticksExisted));
        matrixStackIn.translate(-0.5, 0.0, -0.5);

        Color blendColour = Color.WHITE;
        float red = blendColour.getRed() / 255.0F;
        float green = blendColour.getGreen() / 255.0F;
        float blue = blendColour.getBlue() / 255.0F;

        // we're going to use the block renderer to render our model, even though it's not a block, because we baked
        //   our entity model as if it were a block model.
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        IVertexBuilder vertexBuffer = bufferIn.getBuffer(RenderType.getSolid());
        dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, sphereModel,
                red, green, blue, packedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

        matrixStackIn.pop(); // restore the original transformation matrix + normals matrix

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);  // renders labels
    }

    @Override
    public ResourceLocation getEntityTexture(BombSwingEntity entity) {
        return BOMB;
    }
}
