package com.uberhelixx.flatlights.render.player;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BladeStanceRenderer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public BladeStanceRenderer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> playerModel) {
        super(playerModel);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        //render blades on player if not using it currently
        if(player.hasPlayerInfo() && !player.isInvisible()) {
            ItemStack curio = CurioUtils.getCurioFromSlot(player, CurioUtils.CUBE_SLOT_ID);
            if(curio != null) {
                CompoundNBT tag = curio.getTag();
                boolean toggled = false;
                if(tag != null && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    toggled = tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE);
                }
                if(CurioUtils.correctSetEffect(player, CurioSetNames.DRAGONSFINAL) && toggled) {
                    //total amount of floating swords
                    int totalSwords = 3;

                    //values for orbit rotation
                    float radius = 0.7f;
                    float speedMulti = 3f;

                    //are blades floating horizontally or vertically
                    boolean horizontal = false;

                    //don't edit below this, all the variables are above ^^^^^^^^^^^^^^^^^^
                    //calculating the angle for each sword position
                    float[] angles = new float[totalSwords];
                    float anglePer = 360F / totalSwords;
                    float totalAngle = 0F;
                    for (int i = 0; i < angles.length; i++) {
                        angles[i] = totalAngle += anglePer;
                    }

                    for (int i = 0; i < angles.length; i++) {
                        matrixStack.push();

                        //used for just floating in front of the player at waist height
                        //matrixStack.translate(0.0f, 0.6, -0.5f);

                        //sets size scaling of each item
                        float scale = 0.75f;
                        matrixStack.scale(scale, -scale, -scale);

                        //rotation of each blade
                        matrixStack.rotate(Vector3f.YP.rotationDegrees(angles[i] + ageInTicks * speedMulti));
                        matrixStack.translate(radius, -0.75f, radius);

                        //slight vertical floating variance so it's not just hovering perfectly level all the time
                        matrixStack.translate(0f, 0.075 * Math.sin((ageInTicks + i * 10) / 5), 0f);

                        //set horizontal or vertical pose
                        if (!horizontal) {
                            matrixStack.rotate(Vector3f.YP.rotationDegrees(45));
                            matrixStack.rotate(Vector3f.ZP.rotationDegrees(225));
                        } else {
                            matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
                        }

                        //actually render the item
                        Minecraft.getInstance().getItemRenderer().renderItem(player, ModItems.PRISMATIC_SWORD.get().getDefaultInstance(), ItemCameraTransforms.TransformType.NONE, false, matrixStack, buffer, player.world, 0xF000F0, OverlayTexture.NO_OVERLAY);
                        matrixStack.pop();
                    }
                }
            }
        }
    }
}
