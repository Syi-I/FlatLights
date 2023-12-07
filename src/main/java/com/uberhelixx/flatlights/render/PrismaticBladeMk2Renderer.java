package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.awt.*;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.DAMAGE_MODE_TAG;
import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.PROJECTILE_MODE_TAG;

@OnlyIn(Dist.CLIENT)
public class PrismaticBladeMk2Renderer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public PrismaticBladeMk2Renderer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> playerModel) {
        super(playerModel);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        //is blade being used either in mainhand or offhand
        boolean notUsing = false;

        //get player inventory
        PlayerInventory inv = player.inventory;
        CompoundNBT bladeTag = new CompoundNBT();

        //check player inventory to see if blade is present
        for (ItemStack itemStack : inv.mainInventory) {
            Item item = itemStack.getItem();
            if (item == ModItems.PRISMATIC_BLADEMK2.get()) {
                notUsing = true;
                bladeTag = itemStack.getTag() != null ? itemStack.getTag() : new CompoundNBT();
            }
        }

        //check if blade is in mainhand
        if (inv.getCurrentItem().getItem() == ModItems.PRISMATIC_BLADEMK2.get()) {
            notUsing = false;
            bladeTag = inv.getCurrentItem().getTag() != null ? inv.getCurrentItem().getTag() : new CompoundNBT();
        }

        //check if blade is in offhand slot
        for (ItemStack itemStack : inv.offHandInventory) {
            Item item = itemStack.getItem();
            if (item == ModItems.PRISMATIC_BLADEMK2.get()) {
                notUsing = false;
                bladeTag = itemStack.getTag() != null ? itemStack.getTag() : new CompoundNBT();
            }
        }

        //render blade on player if not using it currently
        if (player.hasPlayerInfo() && !player.isInvisible() && notUsing) {
            matrixStack.push();

            //rotates with player
            getEntityModel().bipedBody.translateRotate(matrixStack);

            //sizing and angle of blade
            matrixStack.translate(0.275, 0.6, -0.1);
            matrixStack.scale(0.8f, -0.8f, -0.8f);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(240));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));

            //actually render the item
            Minecraft.getInstance().getItemRenderer().renderItem(player, ModItems.PRISMATIC_BLADEMK2.get().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, buffer, player.world, 0xF000F0, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        }

        if(bladeTag.contains(DAMAGE_MODE_TAG) || bladeTag.contains(PROJECTILE_MODE_TAG)) {
            if(bladeTag.getBoolean(DAMAGE_MODE_TAG)) {
                //yoinked from vanilla EnergyLayer since it requires being IChargeable mob which players are not
                float tickTime = (float) player.ticksExisted + partialTicks;
                EntityModel<AbstractClientPlayerEntity> playerModel = this.getModel();
                playerModel.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);
                this.getEntityModel().copyModelAttributesTo(playerModel);
                IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEnergySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                playerModel.setRotationAngles(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                float rgbChannels = 0.5f;
                playerModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, (float) (rgbChannels + 0.49 * MathHelper.cos(tickTime / 10)), rgbChannels, rgbChannels, 1.0F);
            }
            else if(bladeTag.getBoolean(PROJECTILE_MODE_TAG)) {
                IBakedModel sphereModel = Minecraft.getInstance().getModelManager().getModel(SPHERE_MODEL);
                final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F; //size of the wavefront model
                final float TARGET_SIZE_WHEN_RENDERED = 0.1F; //desired size when rendered (in metres)
                final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
                final float RADIUS = 5f;
                final float SPEED_MULTI = 5;
                final int BALLS = 3;

                for(int i = 0; i < BALLS; i++) {
                    matrixStack.push();
                    MatrixStack.Entry currentMatrix = matrixStack.getLast();
                    matrixStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
                    float vibe = (float) (1 + 0.15 * Math.sin(((ageInTicks + i * 10) / 5)));
                    matrixStack.translate(0, 3f, 0);
                    matrixStack.rotate(Vector3f.ZN.rotationDegrees((360f / BALLS) * i));
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(ageInTicks * SPEED_MULTI + (i * 75)));
                    matrixStack.translate(RADIUS, 0, RADIUS);
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(-1 * player.ticksExisted));
                    matrixStack.scale(vibe, vibe, vibe);

                    Color blendColour = Color.WHITE;
                    float red = blendColour.getRed() / 255.0F;
                    float green = blendColour.getGreen() / 255.0F;
                    float blue = blendColour.getBlue() / 255.0F;

                    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

                    IVertexBuilder vertexBuffer = buffer.getBuffer(RenderType.getSolid());
                    dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, sphereModel,
                            red, green, blue, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

                    matrixStack.pop();
                }
            }
        }
    }

    private float layerPosition(float f) {
        //return MathHelper.cos(f * 0.02F) * 3.0F + MathHelper.sin(f * 0.02F) * 3.0F;
        return f * 0.015f;
    }

    ResourceLocation DAMAGE_LAYER = new ResourceLocation(FlatLights.MOD_ID, "textures/models/power_layers/mk2_damage_mode_layer.png");

    private ResourceLocation layerTexture() {
        return DAMAGE_LAYER;
    }

    private EntityModel<AbstractClientPlayerEntity> getModel() {
        return this.getEntityModel();
    }

    public static final ResourceLocation SPHERE_MODEL = new ResourceLocation(FlatLights.MOD_ID, "entity/void_sphere_wrapper");
}
