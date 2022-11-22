package com.uberhelixx.flatlights.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public abstract class ArmorBaseModel extends BipedModel<LivingEntity> {
    protected final ModelRenderer armorHead;
    private final ModelRenderer colorPane3_r1;
    private final ModelRenderer squarePane3_r1;
    private final ModelRenderer squarePane3_r2;
    private final ModelRenderer colorPane2_r1;
    private final ModelRenderer squarePane2_r1;
    private final ModelRenderer squarePane2_r2;
    private final ModelRenderer colorPane1_r1;
    private final ModelRenderer squarePane1_r1;
    private final ModelRenderer squarePane1_r2;
    private final ModelRenderer sideTriangle2_r1;
    private final ModelRenderer finTop_r1;
    private final ModelRenderer frontSlantR_r1;
    private final ModelRenderer frontSlantL_r1;
    private final ModelRenderer sideTriangle3_r1;
    private final ModelRenderer topSlant2_r1;
    private final ModelRenderer topSlant1_r1;
    private final ModelRenderer sideTriangle1_r1;

    private String texture;

    public ArmorBaseModel(int textureWidth, int textureHeight, ResourceLocation texture){
        super(1F);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.texture = texture.toString();

        armorHead = new ModelRenderer(this);
        armorHead.setRotationPoint(-1.5F, -3.5F, -1.5F);
        bipedHead.addChild(armorHead);
        armorHead.setTextureOffset(0, 0).addBox(-3.0F, 1.5F, -1.5F, 9.0F, 2.0F, 7.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-3.0F, -5.0F, 3.0F, 9.0F, 6.0F, 3.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-2.725F, -4.75F, -2.75F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(0.325F, -5.0F, 1.75F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(3.0F, 2.5F, -3.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-3.0F, 2.5F, -3.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -3.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(3.25F, 3.4F, -3.25F, 3.0F, 0.0F, 8.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(3.25F, 2.9F, -3.25F, 3.0F, 0.0F, 8.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-3.25F, 2.9F, -3.25F, 3.0F, 0.0F, 8.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-3.25F, 3.4F, -3.25F, 3.0F, 0.0F, 8.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(3.25F, 3.15F, -3.25F, 0.0F, 0.0F, 0.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-0.5F, 3.15F, -3.25F, 0.0F, 0.0F, 0.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-1.0F, -5.25F, 3.25F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-1.5F, -5.25F, 3.25F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(-2.0F, -5.25F, 3.25F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(3.75F, -5.25F, 3.25F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(4.25F, -5.25F, 3.25F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        armorHead.setTextureOffset(0, 0).addBox(4.75F, -5.25F, 3.25F, 0.0F, 1.0F, 3.0F, 0.0F, false);

        colorPane3_r1 = new ModelRenderer(this);
        colorPane3_r1.setRotationPoint(-0.1F, 3.0F, 8.0F);
        armorHead.addChild(colorPane3_r1);
        setRotationAngle(colorPane3_r1, 0.0F, 0.0F, -0.7854F);
        colorPane3_r1.setTextureOffset(0, 0).addBox(3.825F, -3.0F, -2.4F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        squarePane3_r1 = new ModelRenderer(this);
        squarePane3_r1.setRotationPoint(-0.6F, -2.25F, 6.25F);
        armorHead.addChild(squarePane3_r1);
        setRotationAngle(squarePane3_r1, 0.0F, 0.0F, -0.7854F);
        squarePane3_r1.setTextureOffset(0, 0).addBox(0.25F, 0.75F, -0.25F, 0.0F, 2.0F, 0.0F, 0.0F, false);
        squarePane3_r1.setTextureOffset(0, 0).addBox(1.75F, 0.75F, -0.25F, 0.0F, 2.0F, 0.0F, 0.0F, false);

        squarePane3_r2 = new ModelRenderer(this);
        squarePane3_r2.setRotationPoint(-0.6F, -2.25F, 6.25F);
        armorHead.addChild(squarePane3_r2);
        setRotationAngle(squarePane3_r2, 0.0F, 0.0F, 0.7854F);
        squarePane3_r2.setTextureOffset(0, 0).addBox(2.25F, -1.75F, -0.25F, 0.0F, 1.0F, 0.0F, 0.0F, false);
        squarePane3_r2.setTextureOffset(0, 0).addBox(0.75F, -1.75F, -0.25F, 0.0F, 1.0F, 0.0F, 0.0F, false);

        colorPane2_r1 = new ModelRenderer(this);
        colorPane2_r1.setRotationPoint(-0.7F, 2.0F, 8.0F);
        armorHead.addChild(colorPane2_r1);
        setRotationAngle(colorPane2_r1, 0.0F, 0.0F, -0.7854F);
        colorPane2_r1.setTextureOffset(0, 0).addBox(2.925F, 2.5F, -2.4F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        squarePane2_r1 = new ModelRenderer(this);
        squarePane2_r1.setRotationPoint(2.05F, 1.25F, 6.25F);
        armorHead.addChild(squarePane2_r1);
        setRotationAngle(squarePane2_r1, 0.0F, 0.0F, 0.7854F);
        squarePane2_r1.setTextureOffset(0, 0).addBox(0.75F, -1.75F, -0.25F, 0.0F, 1.0F, 0.0F, 0.0F, false);
        squarePane2_r1.setTextureOffset(0, 0).addBox(2.25F, -1.75F, -0.25F, 0.0F, 1.0F, 0.0F, 0.0F, false);

        squarePane2_r2 = new ModelRenderer(this);
        squarePane2_r2.setRotationPoint(2.05F, 1.25F, 6.25F);
        armorHead.addChild(squarePane2_r2);
        setRotationAngle(squarePane2_r2, 0.0F, 0.0F, -0.7854F);
        squarePane2_r2.setTextureOffset(0, 0).addBox(1.75F, 0.75F, -0.25F, 0.0F, 2.0F, 0.0F, 0.0F, false);
        squarePane2_r2.setTextureOffset(0, 0).addBox(0.25F, 0.75F, -0.25F, 0.0F, 2.0F, 0.0F, 0.0F, false);

        colorPane1_r1 = new ModelRenderer(this);
        colorPane1_r1.setRotationPoint(0.5F, 2.0F, 8.0F);
        armorHead.addChild(colorPane1_r1);
        setRotationAngle(colorPane1_r1, 0.0F, 0.0F, -0.7854F);
        colorPane1_r1.setTextureOffset(0, 0).addBox(-1.675F, -2.125F, -2.4F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        squarePane1_r1 = new ModelRenderer(this);
        squarePane1_r1.setRotationPoint(-3.25F, 1.25F, 6.25F);
        armorHead.addChild(squarePane1_r1);
        setRotationAngle(squarePane1_r1, 0.0F, 0.0F, -0.7854F);
        squarePane1_r1.setTextureOffset(0, 0).addBox(0.25F, 0.75F, -0.25F, 0.0F, 2.0F, 0.0F, 0.0F, false);
        squarePane1_r1.setTextureOffset(0, 0).addBox(1.75F, 0.75F, -0.25F, 0.0F, 2.0F, 0.0F, 0.0F, false);

        squarePane1_r2 = new ModelRenderer(this);
        squarePane1_r2.setRotationPoint(-3.25F, 1.25F, 6.25F);
        armorHead.addChild(squarePane1_r2);
        setRotationAngle(squarePane1_r2, 0.0F, 0.0F, 0.7854F);
        squarePane1_r2.setTextureOffset(0, 0).addBox(2.25F, -1.75F, -0.25F, 0.0F, 1.0F, 0.0F, 0.0F, false);
        squarePane1_r2.setTextureOffset(0, 0).addBox(0.75F, -1.75F, -0.25F, 0.0F, 1.0F, 0.0F, 0.0F, false);

        sideTriangle2_r1 = new ModelRenderer(this);
        sideTriangle2_r1.setRotationPoint(0.0F, 24.0F, 0.0F);
        armorHead.addChild(sideTriangle2_r1);
        setRotationAngle(sideTriangle2_r1, 0.3927F, 0.0F, 0.0F);
        sideTriangle2_r1.setTextureOffset(0, 0).addBox(-2.95F, -21.125F, 7.4F, 8.0F, 1.0F, 3.0F, 0.0F, false);

        finTop_r1 = new ModelRenderer(this);
        finTop_r1.setRotationPoint(-0.125F, 24.0F, 1.0F);
        armorHead.addChild(finTop_r1);
        setRotationAngle(finTop_r1, 0.3927F, 0.0F, 0.0F);
        finTop_r1.setTextureOffset(0, 0).addBox(0.95F, -26.75F, 12.5F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        finTop_r1.setTextureOffset(0, 0).addBox(0.7F, -26.5F, 12.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

        frontSlantR_r1 = new ModelRenderer(this);
        frontSlantR_r1.setRotationPoint(-2.8284F, 3.3878F, -4.5F);
        armorHead.addChild(frontSlantR_r1);
        setRotationAngle(frontSlantR_r1, 0.0F, 0.0F, -0.7854F);
        frontSlantR_r1.setTextureOffset(0, 0).addBox(1.625F, 1.375F, 1.525F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        frontSlantL_r1 = new ModelRenderer(this);
        frontSlantL_r1.setRotationPoint(2.8284F, 3.3878F, -4.5F);
        armorHead.addChild(frontSlantL_r1);
        setRotationAngle(frontSlantL_r1, 0.0F, 0.0F, -0.7854F);
        frontSlantL_r1.setTextureOffset(0, 0).addBox(-0.25F, -0.5F, 1.525F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        sideTriangle3_r1 = new ModelRenderer(this);
        sideTriangle3_r1.setRotationPoint(0.0F, 2.5617F, -3.8353F);
        armorHead.addChild(sideTriangle3_r1);
        setRotationAngle(sideTriangle3_r1, -0.7854F, 0.0F, 0.0F);
        sideTriangle3_r1.setTextureOffset(0, 0).addBox(-2.975F, -2.4F, 0.9F, 8.0F, 1.0F, 1.0F, 0.0F, false);

        topSlant2_r1 = new ModelRenderer(this);
        topSlant2_r1.setRotationPoint(-2.1534F, -4.7375F, 3.5533F);
        armorHead.addChild(topSlant2_r1);
        setRotationAngle(topSlant2_r1, 0.0F, 0.3927F, 0.0F);
        topSlant2_r1.setTextureOffset(0, 0).addBox(-0.25F, -0.2375F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

        topSlant1_r1 = new ModelRenderer(this);
        topSlant1_r1.setRotationPoint(3.9034F, -4.7375F, 3.5533F);
        armorHead.addChild(topSlant1_r1);
        setRotationAngle(topSlant1_r1, 0.0F, -0.3927F, 0.0F);
        topSlant1_r1.setTextureOffset(0, 0).addBox(-2.25F, -0.2375F, -1.0F, 3.0F, 0.0F, 1.0F, 0.0F, false);

        sideTriangle1_r1 = new ModelRenderer(this);
        sideTriangle1_r1.setRotationPoint(0.0F, -0.9462F, 2.2979F);
        armorHead.addChild(sideTriangle1_r1);
        setRotationAngle(sideTriangle1_r1, -0.3927F, 0.0F, 0.0F);
        sideTriangle1_r1.setTextureOffset(0, 0).addBox(-2.975F, -3.5F, -0.25F, 8.0F, 7.0F, 2.0F, 0.0F, false);

        setupArmorParts();
    }

    public abstract void setupArmorParts();

    public final String getTexture(){
        return this.texture;
    }

    /**
     * Feel free to override this method as needed.
     * It's just required to hide armor parts depending on the equipment slot
     */
    public BipedModel applySlot(EquipmentSlotType slot){
        armorHead.showModel = false;
        /*armorBody.showModel = false;
        armorRightArm.showModel = false;
        armorLeftArm.showModel = false;
        armorRightLeg.showModel = false;
        armorLeftLeg.showModel = false;
        armorRightBoot.showModel = false;
        armorLeftBoot.showModel = false;*/

        switch(slot){
            case HEAD:
                armorHead.showModel = true;
                break;
            /*case CHEST:
                armorBody.showModel = true;
                armorRightArm.showModel = true;
                armorLeftArm.showModel = true;
                break;
            case LEGS:
                armorRightLeg.showModel = true;
                armorLeftLeg.showModel = true;
                break;
            case FEET:
                armorRightBoot.showModel = true;
                armorLeftBoot.showModel = true;
                break;*/
            default:
                break;
        }

        return this;
    }

    public final ArmorBaseModel applyEntityStats(BipedModel defaultArmor){
        this.isChild = defaultArmor.isChild;
        this.isSneak = defaultArmor.isSneak;
        this.isSitting = defaultArmor.isSitting;
        this.rightArmPose = defaultArmor.rightArmPose;
        this.leftArmPose = defaultArmor.leftArmPose;

        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        copyModelAngles(this.bipedHead, this.armorHead);
        /*copyModelAngles(this.bipedBody, this.armorBody);
        copyModelAngles(this.bipedRightArm, this.armorRightArm);
        copyModelAngles(this.bipedLeftArm, this.armorLeftArm);
        copyModelAngles(this.bipedRightLeg, this.armorRightLeg);
        copyModelAngles(this.bipedLeftLeg, this.armorLeftLeg);
        copyModelAngles(this.bipedRightLeg, this.armorRightBoot);
        copyModelAngles(this.bipedLeftLeg, this.armorLeftBoot);*/

        matrixStack.push();
        if(isSneak) matrixStack.translate(0, 0.2, 0);

        renderChildrenOnly(this.bipedHead, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildrenOnly(this.bipedBody, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildrenOnly(this.bipedRightArm, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildrenOnly(this.bipedLeftArm, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildrenOnly(this.bipedRightLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildrenOnly(this.bipedLeftLeg, matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        matrixStack.pop();
    }

    public final void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    private final void copyModelAngles(ModelRenderer in, ModelRenderer out){
        out.rotateAngleX = in.rotateAngleX;
        out.rotateAngleY = in.rotateAngleY;
        out.rotateAngleZ = in.rotateAngleZ;
    }

    private final void renderChildrenOnly(ModelRenderer bodyPart, MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        if(!bodyPart.childModels.isEmpty()){
            matrixStack.push();
            bodyPart.translateRotate(matrixStack);
            for(ModelRenderer child : bodyPart.childModels)
                child.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            matrixStack.pop();
        }
    }
}
