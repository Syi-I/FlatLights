package com.uberhelixx.flatlights.client.renderer.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.client.renderer.ModRenderTypes;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;

import java.awt.*;

import static com.uberhelixx.flatlights.util.lib.LibTagKeys.MODE_TAG;
import static com.uberhelixx.flatlights.util.lib.LibTagKeys.TIER_TAG;

public class PrismaticBladeMk2Renderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PrismaticBladeMk2Renderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, AbstractClientPlayer abstractClientPlayer, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        //is blade being used either in mainhand or offhand
        boolean notUsing = false;
        
        //get player inventory
        Inventory inv = abstractClientPlayer.getInventory();
        CompoundTag bladeTag = new CompoundTag();
        
        //check player inventory to see if blade is present
        for (ItemStack itemStack : inv.items) {
            Item item = itemStack.getItem();
            if (item == ModItems.PRISMATIC_BLADEMK2.get()) {
                notUsing = true;
                bladeTag = itemStack.getTag() != null ? itemStack.getTag() : new CompoundTag();
            }
        }
        
        //check if blade is in mainhand
        if (inv.getSelected().getItem() == ModItems.PRISMATIC_BLADEMK2.get()) {
            notUsing = false;
            bladeTag = inv.getSelected().getTag() != null ? inv.getSelected().getTag() : new CompoundTag();
        }
        
        //check if blade is in offhand slot
        for (ItemStack itemStack : inv.offhand) {
            Item item = itemStack.getItem();
            if (item == ModItems.PRISMATIC_BLADEMK2.get()) {
                notUsing = false;
                bladeTag = itemStack.getTag() != null ? itemStack.getTag() : new CompoundTag();
            }
        }
        boolean playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(abstractClientPlayer.getUUID()) != null;
        
        //render blade on player if not using it currently
        if (playerInfo && !abstractClientPlayer.isInvisible() && notUsing) {
            poseStack.pushPose();
            
            //rotates with player
            getParentModel().body.translateAndRotate(poseStack);
            
            //sizing and angle of blade
            poseStack.translate(0.13, 1.15, 0.2);
            poseStack.scale(0.8f, -0.8f, -0.8f);
            poseStack.mulPose(Axis.XP.rotationDegrees(240));
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            
            //actually render the item
            Minecraft.getInstance().getItemRenderer().renderStatic(abstractClientPlayer, ModItems.PRISMATIC_BLADEMK2.get().getDefaultInstance(), ItemDisplayContext.NONE, false, poseStack, multiBufferSource, abstractClientPlayer.level(), 0xF000F0, OverlayTexture.NO_OVERLAY, abstractClientPlayer.getId());
            poseStack.popPose();
        }
        
        if(bladeTag.contains(MODE_TAG)) {
            int bladeMode = bladeTag.getInt(MODE_TAG);
            if(bladeMode == PrismaticBladeMk2.DMG_MODE) {
                //yoinked from vanilla EnergyLayer since it requires being IChargeable mob which players are not
                float tickTime = (float) abstractClientPlayer.tickCount + pPartialTicks;
                EntityModel<AbstractClientPlayer> playerModel = this.getParentModel();
                playerModel.prepareMobModel(abstractClientPlayer, pLimbSwing, pLimbSwingAmount, pPartialTicks);
                this.getParentModel().copyPropertiesTo(playerModel);
                VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(RenderType.energySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                playerModel.setupAnim(abstractClientPlayer, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                float rgbChannels = 0.5f;
                playerModel.renderToBuffer(poseStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, (float) (rgbChannels + 0.49 * Mth.cos(tickTime / 10)), rgbChannels, rgbChannels, 1.0F);
            }
            else if(bladeMode == PrismaticBladeMk2.AURA_MODE) {
                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                //renderSphereOrbit(poseStack, multiBufferSource, pPackedLight, abstractClientPlayer, pAgeInTicks, dispatcher);
                int swords = bladeTag.getInt(TIER_TAG);
                renderBladeOrbit(poseStack, multiBufferSource,pPackedLight, pAgeInTicks, dispatcher, swords);
            }
        }
    }
    
    ResourceLocation DAMAGE_LAYER = new ResourceLocation(FlatLights.MODID, "textures/models/power_layers/mk2_damage_mode_layer.png");
    ResourceLocation BLADE_LAYER = new ResourceLocation(FlatLights.MODID, "textures/models/power_layers/blade.png");
    
    private float layerPosition(float f) {
        //return Mth.cos(f * 0.02F) * 3.0F + Mth.sin(f * 0.02F) * 3.0F;
        return f * 0.015f;
    }
    
    private ResourceLocation layerTexture() {
        return DAMAGE_LAYER;
    }
    
    private ResourceLocation bladeLayerTexture() {
        return BLADE_LAYER;
    }
    
    public static final ResourceLocation SPHERE_MODEL = new ResourceLocation(FlatLights.MODID, "entity/void_sphere_wrapper");
    public static final ResourceLocation MK2_PROJECTILE_MODEL = new ResourceLocation(FlatLights.MODID, "item/prismatic_blademk2");
    public static final ResourceLocation MK2_PROJECTILE = new ResourceLocation(FlatLights.MODID, "textures/item/prismatic_blademk2/main_blade.png");
    
    private void renderBladeOrbit(PoseStack ms, MultiBufferSource buffers, int pPackedLight, float ageInTicks, BlockRenderDispatcher dispatcher, int swordCount) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel mk2ProjectileModel = mc.getModelManager().getModel(MK2_PROJECTILE_MODEL);
        
        //total amount of floating swords, defaults to 3
        int totalSwords = swordCount > 0 ? swordCount : 3;
        
        //values for orbit rotation
        float radius = 0.65f;
        float speedMulti = 3f;
        
        //are blades floating horizontally or vertically
        boolean horizontal = false;
        
        //sets size scaling of each item
        float scale = 0.65f;
        
        //don't edit below this, all the variables are above ^^^^^^^^^^^^^^^^^^
        //calculating the angle for each sword position
        float[] angles = new float[totalSwords];
        float anglePer = 360F / totalSwords;
        float totalAngle = 0F;
        for (int i = 0; i < angles.length; i++) {
            angles[i] = totalAngle += anglePer;
        }
        
        for (int i = 0; i < angles.length; i++) {
            ms.pushPose();
            
            //used for just floating in front of the player at waist height
            //matrixStack.translate(0.0f, 0.6, -0.5f);
            
            ms.scale(scale, -scale, -scale);
            
            //rotation of each blade
            ms.mulPose(Axis.YP.rotationDegrees(angles[i] + ageInTicks * speedMulti));
            ms.translate(radius, -0.75f, radius);
            
            //slight vertical floating variance so it's not just hovering perfectly level all the time
            ms.translate(0f, 0.075 * Math.sin((ageInTicks + i * 10) / 5), 0f);
            
            //set horizontal or vertical pose
            if (!horizontal) {
                ms.mulPose(Axis.YP.rotationDegrees(45));
                ms.mulPose(Axis.ZP.rotationDegrees(180));
            }
            else {
                ms.mulPose(Axis.XP.rotationDegrees(90));
            }
            
            PoseStack.Pose currentPose = ms.last();
            
            Color blendColor = Color.BLUE;
            float red = blendColor.getRed() / 255.0F;
            float green = blendColor.getGreen() / 255.0F;
            float blue = blendColor.getBlue() / 255.0F;
            
            //space shader for the rendered model
            //VertexConsumer shaderBuffer = buffers.getBuffer(ModRenderTypes.SPACE);
            VertexConsumer shaderBuffer;
            if (!ModRenderTypes.isOculusPresent()) {
                shaderBuffer = buffers.getBuffer(ModRenderTypes.SPACE);
            }
            else {
                shaderBuffer = buffers.getBuffer(RenderType.energySwirl(this.bladeLayerTexture(), this.layerPosition(ageInTicks), ageInTicks * 0.01F));
            }
            
            //actually render the item
            dispatcher.getModelRenderer().renderModel(currentPose, shaderBuffer, null, mk2ProjectileModel,
                    red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.entityCutoutNoCull(MK2_PROJECTILE));
            
            ms.popPose();
        }
    }
    
    private void renderSphereOrbit(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, AbstractClientPlayer abstractClientPlayer, float pAgeInTicks, BlockRenderDispatcher dispatcher) {
        BakedModel sphereModel = Minecraft.getInstance().getModelManager().getModel(SPHERE_MODEL);
        final float MODEL_SIZE_IN_ORIGINAL_COORDINATES = 1.0F; //size of the wavefront model
        final float TARGET_SIZE_WHEN_RENDERED = 0.1F; //desired size when rendered (in metres)
        final float SCALE_FACTOR = TARGET_SIZE_WHEN_RENDERED / MODEL_SIZE_IN_ORIGINAL_COORDINATES;
        final float RADIUS = 5f;
        final float SPEED_MULTI = 5;
        final int BALLS = 3;
        
        for(int i = 0; i < BALLS; i++) {
            poseStack.pushPose();
            PoseStack.Pose currentMatrix = poseStack.last();
            poseStack.scale(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
            float vibe = (float) (1 + 0.15 * Math.sin(((pAgeInTicks + i * 10) / 5)));
            poseStack.translate(0, 3f, 0);
            poseStack.mulPose(Axis.ZN.rotationDegrees((360f / BALLS) * i));
            poseStack.mulPose(Axis.YP.rotationDegrees(pAgeInTicks * SPEED_MULTI + (i * 75)));
            poseStack.translate(RADIUS, 0, RADIUS);
            poseStack.mulPose(Axis.YP.rotationDegrees(-1 * abstractClientPlayer.tickCount));
            poseStack.scale(vibe, vibe, vibe);
            
            Color blendColour = Color.WHITE;
            float red = blendColour.getRed() / 255.0F;
            float green = blendColour.getGreen() / 255.0F;
            float blue = blendColour.getBlue() / 255.0F;
            
            VertexConsumer vertexBuffer = multiBufferSource.getBuffer(RenderType.solid());
            dispatcher.getModelRenderer().renderModel(currentMatrix, vertexBuffer, null, sphereModel,
                    red, green, blue, pPackedLight, OverlayTexture.NO_OVERLAY);
            
            poseStack.popPose();
        }
    }
}
