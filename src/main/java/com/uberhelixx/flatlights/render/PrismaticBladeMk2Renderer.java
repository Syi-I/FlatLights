package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

        //check player inventory to see if blade is present
        for (ItemStack itemStack : inv.mainInventory) {
            Item item = itemStack.getItem();
            if (item == ModItems.PRISMATIC_BLADEMK2.get()) {
                notUsing = true;
            }
        }

        //check if blade is in mainhand
        if (inv.getCurrentItem().getItem() == ModItems.PRISMATIC_BLADEMK2.get()) {
            notUsing = false;
        }

        //check if blade is in offhand slot
        for (ItemStack itemStack : inv.offHandInventory) {
            Item item = itemStack.getItem();
            if (item == ModItems.PRISMATIC_BLADEMK2.get()) {
                notUsing = false;
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
    }
}
