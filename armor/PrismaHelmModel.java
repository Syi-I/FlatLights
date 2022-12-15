package com.uberhelixx.flatlights.item.armor;// Made with Blockbench 4.5.0
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class PrismaHelmModel extends BipedModel<LivingEntity> {
	private final ModelRenderer armorHead;
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
	private final ModelRenderer armorBody;
	private final ModelRenderer armorRightArm;
	private final ModelRenderer armorLeftArm;
	private final ModelRenderer armorRightLeg;
	private final ModelRenderer armorRightBoot;
	private final ModelRenderer armorLeftLeg;
	private final ModelRenderer armorLeftBoot;

	public PrismaHelmModel() {
		super(1F);
		textureWidth = 64;
		textureHeight = 64;

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

		armorBody = new ModelRenderer(this);
		armorBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(armorBody);
		

		armorRightArm = new ModelRenderer(this);
		armorRightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(armorRightArm);
		

		armorLeftArm = new ModelRenderer(this);
		armorLeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(armorLeftArm);
		

		armorRightLeg = new ModelRenderer(this);
		armorRightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightLeg.addChild(armorRightLeg);
		

		armorRightBoot = new ModelRenderer(this);
		armorRightBoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightLeg.addChild(armorRightBoot);
		

		armorLeftLeg = new ModelRenderer(this);
		armorLeftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(armorLeftLeg);
		

		armorLeftBoot = new ModelRenderer(this);
		armorLeftBoot.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(armorLeftBoot);
		
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}