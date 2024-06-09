package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.awt.*;

public class DragonSphereRenderer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public static final ResourceLocation INNER_SPHERE = new ResourceLocation(FlatLights.MOD_ID, "textures/models/inner_sphere/inner_sphere.png");
    public static final ResourceLocation INNER_SPHERE_MODEL = new ResourceLocation(FlatLights.MOD_ID, "entity/inner_sphere_wrapper");

    public static final ResourceLocation OUTER_SPHERE = new ResourceLocation(FlatLights.MOD_ID, "textures/models/outer_sphere/outer_sphere.png");
    public static final ResourceLocation OUTER_SPHERE_MODEL = new ResourceLocation(FlatLights.MOD_ID, "entity/outer_sphere_wrapper");

    public DragonSphereRenderer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> playerModel) {
        super(playerModel);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity clientPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        //render effect on player if not invisible
        if(clientPlayer.hasPlayerInfo() && !clientPlayer.isInvisible()) {
            //get cube curio because it's the one with the set effect toggle
            ItemStack curio = CurioUtils.getCurioFromSlot(clientPlayer, CurioUtils.CUBE_SLOT_ID);

            //make sure the player is wearing the cube curio
            if(curio != null) {
                //get effect toggle status to determine whether to render the visual effects
                CompoundNBT tag = curio.getTag();
                boolean toggled = false;
                if(tag != null && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    toggled = tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE);
                }
                //toggled on
                if(CurioUtils.correctSetEffect(clientPlayer, CurioSetNames.DRAGONSFINAL) && toggled) {
                    IBakedModel innerSphereModel = Minecraft.getInstance().getModelManager().getModel(INNER_SPHERE_MODEL);

                    matrixStackIn.push();
                    MatrixStack.Entry currentMatrix = matrixStackIn.getLast();

                    final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float TARGET_SIZE_WHEN_RENDERED = 0.1F;  // desired size when rendered (in metres)

                    final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    matrixStackIn.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
                    matrixStackIn.translate(-5, -3.15 + 0.1 * Math.sin(ageInTicks * 0.1), 5);
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-1 * ageInTicks));
                    float flicker = (float) (0.1 * Math.sin(ageInTicks * 2));
                    matrixStackIn.scale(1 - flicker, 1 - flicker, 1 - flicker);

                    Color blendColour = Color.WHITE;
                    float red = blendColour.getRed() / 255.0F;
                    float green = blendColour.getGreen() / 255.0F;
                    float blue = blendColour.getBlue() / 255.0F;

                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

                    IVertexBuilder vertexBuffer = bufferIn.getBuffer(RenderType.getTranslucent());
                    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, innerSphereModel,
                            red, green, blue, packedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

                    matrixStackIn.pop(); // restore the original transformation matrix + normals matrix

                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++e++++++++++++++++++++

                    IBakedModel outerSphereModel = Minecraft.getInstance().getModelManager().getModel(OUTER_SPHERE_MODEL);

                    matrixStackIn.push();

                    MatrixStack.Entry outCurrentMatrix = matrixStackIn.getLast();

                    final float OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float OUT_TARGET_SIZE_WHEN_RENDERED = 0.25F;  // desired size when rendered (in metres)

                    final float OUT_SCALE_FACTOR = OUT_TARGET_SIZE_WHEN_RENDERED / OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    matrixStackIn.scale(OUT_SCALE_FACTOR, OUT_SCALE_FACTOR, OUT_SCALE_FACTOR);
                    matrixStackIn.translate(-2, -1.25 + 0.1 * Math.sin(ageInTicks * 0.1), 2);
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-1.5f * ageInTicks));

                    Color outBlendColour = Color.MAGENTA;
                    float outRed = (float) (outBlendColour.getRed() / 255.0F * Math.cos(ageInTicks * 0.1));
                    float outGreen = outBlendColour.getGreen() / 255.0F;
                    float outBlue = outBlendColour.getBlue() / 255.0F;

                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRendererDispatcher outDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

                    IVertexBuilder outVertexBuffer = bufferIn.getBuffer(RenderType.getTranslucent());
                    outDispatcher.getBlockModelRenderer().renderModel(outCurrentMatrix, outVertexBuffer, null, outerSphereModel,
                            outRed, outGreen, outBlue, packedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

                    matrixStackIn.pop(); // restore the original transformation matrix + normals matrix
                }
                //toggled off
                else if(CurioUtils.correctSetEffect(clientPlayer, CurioSetNames.DRAGONSFINAL) && !toggled) {
                    IBakedModel innerSphereModel = Minecraft.getInstance().getModelManager().getModel(INNER_SPHERE_MODEL);

                    matrixStackIn.push();
                    MatrixStack.Entry currentMatrix = matrixStackIn.getLast();

                    final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float TARGET_SIZE_WHEN_RENDERED = 0.1F;  // desired size when rendered (in metres)

                    final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    matrixStackIn.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
                    matrixStackIn.translate(-5, -3.15 + 0.1 * Math.sin(ageInTicks * 0.1), 5);
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-1 * ageInTicks));
                    float flicker = (float) (0.05 * Math.sin(ageInTicks * 0.1));
                    matrixStackIn.scale(1 - flicker, 1 - flicker, 1 - flicker);

                    Color blendColour = Color.WHITE;
                    float red = blendColour.getRed() / 255.0F;
                    float green = blendColour.getGreen() / 255.0F;
                    float blue = blendColour.getBlue() / 255.0F;

                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

                    IVertexBuilder vertexBuffer = bufferIn.getBuffer(RenderType.getSolid());
                    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, innerSphereModel,
                            red, green, blue, packedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

                    matrixStackIn.pop(); // restore the original transformation matrix + normals matrix

                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++e++++++++++++++++++++

                    IBakedModel outerSphereModel = Minecraft.getInstance().getModelManager().getModel(OUTER_SPHERE_MODEL);

                    matrixStackIn.push();

                    MatrixStack.Entry outCurrentMatrix = matrixStackIn.getLast();

                    final float OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float OUT_TARGET_SIZE_WHEN_RENDERED = 0.25F;  // desired size when rendered (in metres)

                    final float OUT_SCALE_FACTOR = OUT_TARGET_SIZE_WHEN_RENDERED / OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    matrixStackIn.scale(OUT_SCALE_FACTOR, OUT_SCALE_FACTOR, OUT_SCALE_FACTOR);
                    matrixStackIn.translate(-2, -1.25 + 0.1 * Math.sin(ageInTicks * 0.1), 2);
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-1.5f * ageInTicks));

                    Color outBlendColour = Color.WHITE;
                    float outRed = outBlendColour.getRed() / 255.0F;
                    float outGreen = outBlendColour.getGreen() / 255.0F;
                    float outBlue = (float) (outBlendColour.getBlue() / 255.0F + 0.5 * Math.cos(ageInTicks * 0.05));

                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRendererDispatcher outDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

                    IVertexBuilder outVertexBuffer = bufferIn.getBuffer(RenderType.getTranslucent());
                    outDispatcher.getBlockModelRenderer().renderModel(outCurrentMatrix, outVertexBuffer, null, outerSphereModel,
                            outRed, outGreen, outBlue, packedLightIn, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

                    matrixStackIn.pop(); // restore the original transformation matrix + normals matrix
                }
            }
        }
    }
}
