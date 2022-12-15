package com.uberhelixx.flatlights.item.armor;// Made with Blockbench 4.4.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class prism_armor1 extends EntityModel<Entity> {
	private final ModelRenderer Head;
	private final ModelRenderer colorPane2_r1;
	private final ModelRenderer squarePane3_r1;
	private final ModelRenderer sideTriangle2_r1;
	private final ModelRenderer sideTriangle3_r1;
	private final ModelRenderer topSlant2_r1;
	private final ModelRenderer topSlant1_r1;
	private final ModelRenderer sideTriangle1_r1;
	private final ModelRenderer Body;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public prism_armor1() {
		textureWidth = 64;
		textureHeight = 64;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -4.5F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);
		Head.setTextureOffset(32, 0).addBox(-4.0F, -4.5F, -4.0F, 8.0F, 8.0F, 8.0F, 1.5F, false);
		Head.setTextureOffset(0, 0).addBox(-6.0F, 1.5F, -3.5F, 12.0F, 3.0F, 9.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-6.0F, -6.0F, 2.5F, 12.0F, 7.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-5.5F, -5.75F, -5.5F, 11.0F, 10.0F, 11.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-1.5F, -6.0F, 0.25F, 3.0F, 0.0F, 2.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(3.0F, 2.5F, -6.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-6.0F, 2.5F, -6.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-3.0F, 3.5F, -6.0F, 6.0F, 1.0F, 2.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(3.25F, 4.0F, -6.25F, 3.0F, 0.0F, 11.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(3.25F, 3.0F, -6.25F, 3.0F, 0.0F, 11.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-6.25F, 3.0F, -6.25F, 3.0F, 0.0F, 11.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-6.25F, 4.0F, -6.25F, 3.0F, 0.0F, 11.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(3.25F, 3.5F, -6.25F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-3.75F, 3.5F, -6.25F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-2.75F, -6.25F, 3.25F, 0.0F, 2.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-3.75F, -6.25F, 3.25F, 0.0F, 2.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(-4.75F, -6.25F, 3.25F, 0.0F, 2.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(2.25F, -6.25F, 3.25F, 0.0F, 2.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(3.25F, -6.25F, 3.25F, 0.0F, 2.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(0, 0).addBox(4.25F, -6.25F, 3.25F, 0.0F, 2.0F, 3.0F, 0.0F, false);

		colorPane2_r1 = new ModelRenderer(this);
		colorPane2_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(colorPane2_r1);
		setRotationAngle(colorPane2_r1, 0.0F, 0.0F, -0.7854F);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-1.1093F, 3.4201F, 4.1F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-0.5629F, -1.7264F, 4.1F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-5.7093F, -1.2049F, 4.1F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-0.9791F, -2.1422F, 6.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(1.5209F, -2.1422F, 6.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-6.1056F, -1.6119F, 6.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-3.6056F, -1.6119F, 6.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-1.5094F, 2.9843F, 6.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(0.9906F, 2.9843F, 6.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-5.655F, 0.405F, -5.975F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		colorPane2_r1.setTextureOffset(0, 0).addBox(-1.905F, 4.655F, -5.975F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		squarePane3_r1 = new ModelRenderer(this);
		squarePane3_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(squarePane3_r1);
		setRotationAngle(squarePane3_r1, 0.0F, 0.0F, 0.7854F);
		squarePane3_r1.setTextureOffset(0, 0).addBox(0.3578F, -1.5209F, 6.0F, 0.0F, 2.0F, 0.0F, 0.0F, false);
		squarePane3_r1.setTextureOffset(0, 0).addBox(-2.1422F, -1.5209F, 6.0F, 0.0F, 2.0F, 0.0F, 0.0F, false);
		squarePane3_r1.setTextureOffset(0, 0).addBox(0.8881F, 3.6056F, 6.0F, 0.0F, 2.0F, 0.0F, 0.0F, false);
		squarePane3_r1.setTextureOffset(0, 0).addBox(-1.6119F, 3.6056F, 6.0F, 0.0F, 2.0F, 0.0F, 0.0F, false);
		squarePane3_r1.setTextureOffset(0, 0).addBox(5.4843F, -0.9906F, 6.0F, 0.0F, 2.0F, 0.0F, 0.0F, false);
		squarePane3_r1.setTextureOffset(0, 0).addBox(2.9843F, -0.9906F, 6.0F, 0.0F, 2.0F, 0.0F, 0.0F, false);

		sideTriangle2_r1 = new ModelRenderer(this);
		sideTriangle2_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(sideTriangle2_r1);
		setRotationAngle(sideTriangle2_r1, 0.3927F, 0.0F, 0.0F);
		sideTriangle2_r1.setTextureOffset(0, 0).addBox(-5.95F, 1.5403F, -3.6954F, 11.0F, 1.0F, 4.0F, 0.0F, false);
		sideTriangle2_r1.setTextureOffset(0, 0).addBox(-0.75F, -4.5847F, 2.9046F, 1.0F, 1.0F, 3.0F, 0.0F, false);
		sideTriangle2_r1.setTextureOffset(0, 0).addBox(-1.0F, -4.3347F, 2.4046F, 2.0F, 2.0F, 4.0F, 0.0F, false);

		sideTriangle3_r1 = new ModelRenderer(this);
		sideTriangle3_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(sideTriangle3_r1);
		setRotationAngle(sideTriangle3_r1, -0.7854F, 0.0F, 0.0F);
		sideTriangle3_r1.setTextureOffset(0, 0).addBox(-5.975F, 4.2828F, -0.6411F, 11.0F, 2.0F, 1.0F, 0.0F, false);

		topSlant2_r1 = new ModelRenderer(this);
		topSlant2_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(topSlant2_r1);
		setRotationAngle(topSlant2_r1, 0.0F, 0.3927F, 0.0F);
		topSlant2_r1.setTextureOffset(0, 0).addBox(-6.025F, -4.901F, 0.2F, 4.0F, 0.0F, 2.0F, 0.0F, false);

		topSlant1_r1 = new ModelRenderer(this);
		topSlant1_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(topSlant1_r1);
		setRotationAngle(topSlant1_r1, 0.0F, -0.3927F, 0.0F);
		topSlant1_r1.setTextureOffset(0, 0).addBox(1.525F, -4.901F, 0.2F, 4.0F, 0.0F, 2.0F, 0.0F, false);

		sideTriangle1_r1 = new ModelRenderer(this);
		sideTriangle1_r1.setRotationPoint(0.0F, -1.074F, 0.0F);
		Head.addChild(sideTriangle1_r1);
		setRotationAngle(sideTriangle1_r1, -0.3927F, 0.0F, 0.0F);
		sideTriangle1_r1.setTextureOffset(0, 0).addBox(-5.975F, -4.2613F, 0.9218F, 11.0F, 7.0F, 2.0F, 0.0F, false);

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.setTextureOffset(16, 16).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.01F, false);
		Body.setTextureOffset(0, 15).addBox(-5.5F, 2.975F, -3.5F, 11.0F, 2.0F, 7.0F, 0.0F, false);
		Body.setTextureOffset(36, 33).addBox(-2.0F, 7.0F, -3.5F, 4.0F, 8.0F, 0.0F, 0.0F, false);
		Body.setTextureOffset(30, 0).addBox(3.5F, 6.0F, -3.5F, 2.0F, 11.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(30, 0).addBox(-5.5F, 6.0F, -3.5F, 2.0F, 11.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(29, 20).addBox(-3.0F, 5.5F, -3.5F, 6.0F, 1.0F, 0.0F, 0.0F, false);
		Body.setTextureOffset(0, 0).addBox(-5.5F, 5.5F, -0.5F, 11.0F, 11.0F, 4.0F, 0.0F, false);
		Body.setTextureOffset(24, 33).addBox(-3.5F, 7.0F, -3.5F, 1.0F, 10.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(24, 33).addBox(2.5F, 7.0F, -3.5F, 1.0F, 10.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(29, 14).addBox(-2.5F, 16.0F, -3.5F, 5.0F, 1.0F, 3.0F, 0.0F, false);
		Body.setTextureOffset(24, 0).addBox(-1.5F, 15.0F, -3.5F, 3.0F, 0.0F, 2.0F, 0.0F, false);
		Body.setTextureOffset(0, 0).addBox(-2.5F, 15.5F, -3.5F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		Body.setTextureOffset(0, 0).addBox(2.0F, 15.5F, -3.5F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		Body.setTextureOffset(0, 24).addBox(-5.025F, 4.0F, -3.025F, 10.0F, 12.0F, 2.0F, 0.0F, false);
		Body.setTextureOffset(32, 33).addBox(-1.0F, 4.0F, 3.5F, 2.0F, 12.0F, 0.0F, 0.0F, false);
		Body.setTextureOffset(29, 18).addBox(-2.5F, 5.5F, 2.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(36, 21).addBox(-2.25F, 8.0F, 2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(36, 21).addBox(-2.0F, 10.5F, 2.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(22, 24).addBox(-1.75F, 13.0F, 2.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		RightArm.setTextureOffset(40, 16).addBox(-3.0F, 2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
		RightArm.setTextureOffset(24, 24).addBox(-4.5F, 1.0F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);
		RightArm.setTextureOffset(24, 24).addBox(-4.5F, 4.0F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		LeftArm.setTextureOffset(40, 16).addBox(-1.0F, 2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		RightLeg.setTextureOffset(0, 16).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		LeftLeg.setTextureOffset(0, 16).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}