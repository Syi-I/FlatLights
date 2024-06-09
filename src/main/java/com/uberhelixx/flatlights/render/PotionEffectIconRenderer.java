package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PotionEffectIconRenderer extends LayerRenderer<LivingEntity, EntityModel<LivingEntity>> {
    public PotionEffectIconRenderer(IEntityRenderer<LivingEntity, EntityModel<LivingEntity>> entityModel) {
        super(entityModel);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Collection<EffectInstance> effects = entitylivingbaseIn.getActivePotionEffects();
        List<EffectInstance> effectList = new ArrayList<>(effects);
        ResourceLocation[] potions = new ResourceLocation[effectList.size()];
        for (int i = 0; i < effectList.size(); i++) {
            potions[i] = effectList.get(i).getPotion().getRegistryName();
        }

        //render blades on player if not using it currently
        if (!entitylivingbaseIn.isInvisible() && potions != null) {
            //total amount of floating potion icons
            int totalIcons = potions.length;

            //values for orbit rotation
            float radius = 0.7f;
            float speedMulti = 3f;

            //are blades floating horizontally or vertically
            boolean horizontal = false;

            //don't edit below this, all the variables are above ^^^^^^^^^^^^^^^^^^
            //calculating the angle for each sword position
            float[] angles = new float[totalIcons];
            float anglePer = 360F / totalIcons;
            float totalAngle = 0F;
            for (int i = 0; i < angles.length; i++) {
                angles[i] = totalAngle += anglePer;
            }

            for(int i = 0; i < angles.length; i++) {
                matrixStackIn.push();

                //used for just floating in front of the player at waist height
                //matrixStackIn.translate(0.0f, 0.6, -0.5f);

                //sets size scaling of each item
                float scale = 0.75f;
                matrixStackIn.scale(scale, -scale, -scale);

                //rotation of each blade
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(angles[i] + ageInTicks * speedMulti));
                matrixStackIn.translate(radius, -0.75f, radius);

                //slight vertical floating variance so it's not just hovering perfectly level all the time
                matrixStackIn.translate(0f, 0.075 * Math.sin((ageInTicks + i * 10) / 5), 0f);

                //set horizontal or vertical pose
                if(!horizontal) {
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(45));
                    matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(225));
                }
                else {
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
                }

                //actually render the item
                //Minecraft.getInstance().getItemRenderer().renderItem(player, ModItems.PRISMATIC_SWORD.get().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStackIn, buffer, player.world, 0xF000F0, OverlayTexture.NO_OVERLAY);
                Minecraft mc = Minecraft.getInstance();
                mc.getItemRenderer().renderItem(entitylivingbaseIn, ModItems.PRISMATIC_SWORD.get().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStackIn, bufferIn, entitylivingbaseIn.world, 0xF000F0, OverlayTexture.NO_OVERLAY);


                matrixStackIn.pop();
            }
        }
    }
}
