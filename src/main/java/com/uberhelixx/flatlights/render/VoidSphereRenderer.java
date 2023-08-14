package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.entity.VoidSphereEntity;
import com.uberhelixx.flatlights.util.MiscHelpers;
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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import java.awt.*;

public class VoidSphereRenderer extends EntityRenderer<VoidSphereEntity> {
    public static final ResourceLocation SPHERE = new ResourceLocation(FlatLights.MOD_ID, "textures/models/void_sphere.png");
    public static final ResourceLocation SPHERE_MODEL = new ResourceLocation(FlatLights.MOD_ID, "entity/void_sphere_wrapper");

    public VoidSphereRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(VoidSphereEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        IBakedModel sphereModel = Minecraft.getInstance().getModelManager().getModel(SPHERE_MODEL);

        matrixStackIn.push();
        MatrixStack.Entry currentMatrix = matrixStackIn.getLast();

        final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
        final float TARGET_SIZE_WHEN_RENDERED = 1.25F;  // desired size when rendered (in metres)

        final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
        matrixStackIn.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
        matrixStackIn.translate(0, 0.75, 0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-1 * entityIn.ticksExisted));

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
    public ResourceLocation getEntityTexture(VoidSphereEntity entity) {
        return SPHERE;
    }

    public static class VoidSphereRenderFactory implements IRenderFactory<VoidSphereEntity> {
        @Override
        public EntityRenderer<? super VoidSphereEntity> createRenderFor(EntityRendererManager manager) {
            return new VoidSphereRenderer(manager);
        }
    }

}