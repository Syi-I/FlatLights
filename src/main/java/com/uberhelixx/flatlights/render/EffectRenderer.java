package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.capability.EntangledStateProvider;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class EffectRenderer extends LayerRenderer<LivingEntity, EntityModel<LivingEntity>> {
    public EffectRenderer(IEntityRenderer<LivingEntity, EntityModel<LivingEntity>> entityModel) {
        super(entityModel);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, LivingEntity entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        //cannot tell what active effects are on an entity clientside, need some sort of packet serverside to ask and track this first?
        //shows up on anything but slimes so far, ig it has another layer that renders above this one
        if(!entityIn.getTags().isEmpty()) {
            MiscHelpers.debugLogger("entity tag is not empty");
            Set<String> tags = entityIn.getTags();
            boolean hasEntangled = false;
            if(EntangledStateProvider.getEntangledState(entityIn).isPresent()) {
                EntangledStateProvider.getEntangledState(entityIn).ifPresent(entangled -> {
                    if(entangled.isEntangled()) {
                        float tickTime = (float) entityIn.ticksExisted + partialTicks;
                        EntityModel<LivingEntity> entityModel = this.getEntityModel();
                        entityModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
                        this.getEntityModel().copyModelAttributesTo(entityModel);
                        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEnergySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                        entityModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                        float rgbChannels = 0.5f;
                        entityModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, (float) (rgbChannels + 0.49 * MathHelper.cos(tickTime / 10)), rgbChannels, rgbChannels, 1.0F);
                        MiscHelpers.debugLogger("should be rendering effect layer now");
                    }
                });
            }
            
            if(hasEntangled) {
                float tickTime = (float) entityIn.ticksExisted + partialTicks;
                EntityModel<LivingEntity> entityModel = this.getEntityModel();
                entityModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTicks);
                this.getEntityModel().copyModelAttributesTo(entityModel);
                IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEnergySwirl(this.layerTexture(), this.layerPosition(tickTime), tickTime * 0.01F));
                entityModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                float rgbChannels = 0.5f;
                entityModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, (float) (rgbChannels + 0.49 * MathHelper.cos(tickTime / 10)), rgbChannels, rgbChannels, 1.0F);
                MiscHelpers.debugLogger("should be rendering effect layer now");
            }
        }
    }

    ResourceLocation DAMAGE_LAYER = new ResourceLocation(FlatLights.MOD_ID, "textures/models/power_layers/mk2_damage_mode_layer.png");

    private ResourceLocation layerTexture() {
        return DAMAGE_LAYER;
    }

    private float layerPosition(float f) {
        //return MathHelper.cos(f * 0.02F) * 3.0F + MathHelper.sin(f * 0.02F) * 3.0F;
        return f * 0.015f;
    }
    
    /*public static final String ENTANGLED_MOB_TAG = "flatlights.entangled_mob";
    @SubscribeEvent
    public static void addEntangledEffect(PotionEvent.PotionAddedEvent event) {
        //if mob has entangled effect now
        if(event.getEntityLiving().isPotionActive(ModEffects.ENTANGLED.get())) {
            MiscHelpers.debugLogger("added entangled tag to mob");
            LivingEntity entity = event.getEntityLiving();
            //add entangled tag to the mob
            entity.addTag(ENTANGLED_MOB_TAG);
        }
    }
    
    @SubscribeEvent
    public static void removeEntangledEffect(PotionEvent.PotionRemoveEvent event) {
        //check if the mob does not have the entangled effect anymore
        if(!event.getEntityLiving().isPotionActive(ModEffects.ENTANGLED.get())) {
            LivingEntity entity = event.getEntityLiving();
            //get tags from the entity and iterate through each one
            Set<String> tags = entity.getTags();
            for(String tag : tags) {
                //if entangled before but no longer active, remove tag
                if(tag.equals(ENTANGLED_MOB_TAG)) {
                    MiscHelpers.debugLogger("removed entangled tag from mob");
                    entity.removeTag(ENTANGLED_MOB_TAG);
                }
            }
        }
    }*/
}

