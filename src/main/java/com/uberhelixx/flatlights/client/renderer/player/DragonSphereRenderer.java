package com.uberhelixx.flatlights.client.renderer.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class DragonSphereRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public static final ResourceLocation INNER_SPHERE = new ResourceLocation(FlatLights.MODID, "textures/block/inner_sphere/inner_sphere.png");
    public static final ResourceLocation INNER_SPHERE_MODEL = new ResourceLocation(FlatLights.MODID, "entity/inner_sphere_wrapper");
    
    public static final ResourceLocation OUTER_SPHERE = new ResourceLocation(FlatLights.MODID, "textures/block/outer_sphere/outer_sphere.png");
    public static final ResourceLocation OUTER_SPHERE_MODEL = new ResourceLocation(FlatLights.MODID, "entity/outer_sphere_wrapper");
    public DragonSphereRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, AbstractClientPlayer abstractClientPlayer, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        boolean playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(abstractClientPlayer.getUUID()) != null;
        //render effect on player if not invisible
        if(playerInfo && !abstractClientPlayer.isInvisible()) {
            //get cube curio because it's the one with the set effect toggle
            ItemStack curio = CurioUtils.getCurioFromSlot(abstractClientPlayer, CurioUtils.CUBE_SLOT_ID);
            
            //make sure the player is wearing the cube curio
            if(curio != null) {
                //get effect toggle status to determine whether to render the visual effects
                CompoundTag tag = curio.getTag();
                boolean toggled = false;
                if(tag != null && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    toggled = tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE);
                }
                //toggled on
                double pY_outer_sphere = -1.25 + 0.1 * Math.sin(pAgeInTicks * 0.1);
                
                double pY_inner_sphere = -3.15 + 0.1 * Math.sin(pAgeInTicks * 0.1);
                if(CurioUtils.correctSetEffect(abstractClientPlayer, CurioSetNames.DRAGON) && toggled) {
                    BakedModel innerSphereModel = Minecraft.getInstance().getModelManager().getModel(INNER_SPHERE_MODEL);
                    
                    poseStack.pushPose();
                    PoseStack.Pose currentMatrix = poseStack.last();
                    
                    final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float TARGET_SIZE_WHEN_RENDERED = 0.1F;  // desired size when rendered (in metres)
                    
                    final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    poseStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
                    poseStack.translate(-5, pY_inner_sphere, 5);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-1 * pAgeInTicks));
                    float flicker = (float) (0.1 * Math.sin(pAgeInTicks * 2));
                    poseStack.scale(1 - flicker, 1 - flicker, 1 - flicker);
                    
                    Color blendColour = Color.WHITE;
                    float red = blendColour.getRed() / 255.0F;
                    float green = blendColour.getGreen() / 255.0F;
                    float blue = blendColour.getBlue() / 255.0F;
                    
                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                    
                    VertexConsumer vertexBuffer = multiBufferSource.getBuffer(RenderType.translucent());
                    dispatcher.getModelRenderer().renderModel(currentMatrix, vertexBuffer, null, innerSphereModel,
                            red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY);
                    
                    poseStack.popPose(); // restore the original transformation matrix + normals matrix
                    
                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++e++++++++++++++++++++
                    
                    BakedModel outerSphereModel = Minecraft.getInstance().getModelManager().getModel(OUTER_SPHERE_MODEL);
                    
                    poseStack.pushPose();
                    
                    PoseStack.Pose outCurrentMatrix = poseStack.last();
                    
                    final float OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float OUT_TARGET_SIZE_WHEN_RENDERED = 0.25F;  // desired size when rendered (in metres)
                    
                    final float OUT_SCALE_FACTOR = OUT_TARGET_SIZE_WHEN_RENDERED / OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    poseStack.scale(OUT_SCALE_FACTOR, OUT_SCALE_FACTOR, OUT_SCALE_FACTOR);
                    poseStack.translate(-2, pY_outer_sphere, 2);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-1.5f * pAgeInTicks));
                    
                    Color outBlendColour = Color.MAGENTA;
                    float outRed = (float) (outBlendColour.getRed() / 255.0F * Math.cos(pAgeInTicks * 0.1));
                    float outGreen = outBlendColour.getGreen() / 255.0F;
                    float outBlue = outBlendColour.getBlue() / 255.0F;
                    
                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRenderDispatcher outDispatcher = Minecraft.getInstance().getBlockRenderer();
                    
                    VertexConsumer outVertexBuffer = multiBufferSource.getBuffer(RenderType.translucent());
                    outDispatcher.getModelRenderer().renderModel(outCurrentMatrix, outVertexBuffer, null, outerSphereModel,
                            outRed, outGreen, outBlue, pPackedLight, OverlayTexture.NO_OVERLAY);
                    
                    poseStack.popPose(); // restore the original transformation matrix + normals matrix
                }
                //toggled off
                else if(CurioUtils.correctSetEffect(abstractClientPlayer, CurioSetNames.DRAGON) && !toggled) {
                    BakedModel innerSphereModel = Minecraft.getInstance().getModelManager().getModel(INNER_SPHERE_MODEL);
                    
                    poseStack.pushPose();
                    PoseStack.Pose currentMatrix = poseStack.last();
                    
                    final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float TARGET_SIZE_WHEN_RENDERED = 0.1F;  // desired size when rendered (in metres)
                    
                    final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    poseStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
                    poseStack.translate(-5, pY_inner_sphere, 5);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-1 * pAgeInTicks));
                    float flicker = (float) (0.05 * Math.sin(pAgeInTicks * 0.1));
                    poseStack.scale(1 - flicker, 1 - flicker, 1 - flicker);
                    
                    Color blendColour = Color.WHITE;
                    float red = blendColour.getRed() / 255.0F;
                    float green = blendColour.getGreen() / 255.0F;
                    float blue = blendColour.getBlue() / 255.0F;
                    
                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                    
                    VertexConsumer vertexBuffer = multiBufferSource.getBuffer(RenderType.solid());
                    dispatcher.getModelRenderer().renderModel(currentMatrix, vertexBuffer, null, innerSphereModel,
                            red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY);
                    
                    poseStack.popPose(); // restore the original transformation matrix + normals matrix
                    
                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++e++++++++++++++++++++
                    
                    BakedModel outerSphereModel = Minecraft.getInstance().getModelManager().getModel(OUTER_SPHERE_MODEL);
                    
                    poseStack.pushPose();
                    
                    PoseStack.Pose outCurrentMatrix = poseStack.last();
                    
                    final float OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F;  // size of the wavefront model
                    final float OUT_TARGET_SIZE_WHEN_RENDERED = 0.25F;  // desired size when rendered (in metres)
                    
                    final float OUT_SCALE_FACTOR = OUT_TARGET_SIZE_WHEN_RENDERED / OUT_MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                    poseStack.scale(OUT_SCALE_FACTOR, OUT_SCALE_FACTOR, OUT_SCALE_FACTOR);
                    poseStack.translate(-2, pY_outer_sphere, 2);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-1.5f * pAgeInTicks));
                    
                    Color outBlendColour = Color.WHITE;
                    float outRed = outBlendColour.getRed() / 255.0F;
                    float outGreen = outBlendColour.getGreen() / 255.0F;
                    float outBlue = (float) (outBlendColour.getBlue() / 255.0F + 0.5 * Math.cos(pAgeInTicks * 0.05));
                    
                    // we're going to use the block renderer to render our model, even though it's not a block, because we baked
                    //   our entity model as if it were a block model.
                    BlockRenderDispatcher outDispatcher = Minecraft.getInstance().getBlockRenderer();
                    
                    VertexConsumer outVertexBuffer = multiBufferSource.getBuffer(RenderType.translucent());
                    outDispatcher.getModelRenderer().renderModel(outCurrentMatrix, outVertexBuffer, null, outerSphereModel,
                            outRed, outGreen, outBlue, pPackedLight, OverlayTexture.NO_OVERLAY);
                    
                    poseStack.popPose(); // restore the original transformation matrix + normals matrix
                }
            }
        }
    }
}
