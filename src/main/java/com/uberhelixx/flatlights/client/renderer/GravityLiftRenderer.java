package com.uberhelixx.flatlights.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.entity.GravityLiftEntity;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.awt.*;

public class GravityLiftRenderer extends EntityRenderer<GravityLiftEntity> {
    public static final ResourceLocation BEAM = new ResourceLocation(FlatLights.MOD_ID, "textures/models/gravity_lift/lift.png");
    public static final ResourceLocation LIFT_BASE_MODEL = new ResourceLocation(FlatLights.MOD_ID, "entity/gravity_lift");

    public GravityLiftRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(GravityLiftEntity entity) {
        return BEAM;
    }

    @Override
    public void render(GravityLiftEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        renderCubeUsingQuads(entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static final float BEAM_WIDTH = 0.125F / 2;
    public static void renderCubeUsingQuads(GravityLiftEntity gravityLiftBeam, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers, int combinedLight) {
        //model for the actual gravity lift part
        IBakedModel sphereModel = Minecraft.getInstance().getModelManager().getModel(LIFT_BASE_MODEL);

        matrixStack.push();
        MatrixStack.Entry currentMatrix = matrixStack.getLast();

        matrixStack.translate(-0.5, 0, -0.5);

        Color blendColour = Color.WHITE;
        float red = blendColour.getRed() / 255.0F;
        float green = blendColour.getGreen() / 255.0F;
        float blue = blendColour.getBlue() / 255.0F;

        // we're going to use the block renderer to render our model, even though it's not a block, because we baked
        //   our entity model as if it were a block model.
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        IVertexBuilder vertexBuffer = renderBuffers.getBuffer(RenderType.getSolid());
        dispatcher.getBlockModelRenderer().renderModel(currentMatrix, vertexBuffer, null, sphereModel,
                red, green, blue, combinedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.pop();

        // draw the object as a cube, using quads
        // When render method is called, the origin [0,0,0] is at the current [x,y,z] of the block.

        // The cube-drawing method draws the cube in the region from [0,0,0] to [1,1,1] but we want it
        //   to be in the block one above this, i.e. from [0,1,0] to [1,2,1],
        //   so we need to translate up by one block, i.e. by [0,1,0]
        //rendering for the beam from the lift
        final Vector3d TRANSLATION_OFFSET = new Vector3d(-BEAM_WIDTH / 2, 0, -BEAM_WIDTH / 2);

        matrixStack.push(); // push the current transformation matrix + normals matrix
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-10 * gravityLiftBeam.ticksExisted));
        matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
        Color BEAM_COLOR = Color.getHSBColor(0.75F, 1.0F, 0.8F);
        //flicker when lift is close to expiring
        if(gravityLiftBeam.ticksExisted > (GravityLiftEntity.SECONDS - 1.5) * 20 && gravityLiftBeam.ticksExisted % 5 == 0) {
            BEAM_COLOR = Color.WHITE;
        }

        drawCubeQuads(matrixStack, renderBuffers, BEAM_COLOR, combinedLight);
        matrixStack.pop(); // restore the original transformation matrix + normals matrix
    }

    /**
     * Draw a cube from [0,0,0] to [1,1,1], same texture on all sides, using a supplied texture
     */
    private static void drawCubeQuads(MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, Color color, int combinedLight) {
        IVertexBuilder vertexBuilderBlockQuads = renderBuffer.getBuffer(RenderType.getBeaconBeam(BEAM, true));
        // other typical RenderTypes used by TER are:
        // getEntityCutout, getBeaconBeam (which has translucency),

        Matrix4f matrixPos = matrixStack.getLast().getMatrix();     // retrieves the current transformation matrix
        Matrix3f matrixNormal = matrixStack.getLast().getNormal();  // retrieves the current transformation matrix for the normal vector

        // we use the whole texture
        Vector2f bottomLeftUV = new Vector2f(0.0F, 4.0F);
        float UVwidth = 1.0F;
        float UVheight = 4.0F;

        // all faces have the same height and width
        final float LIFT_HEIGHT = ModEntityTypes.GRAV_LIFT_HEIGHT;

        final Vector3d EAST_FACE_MIDPOINT = new Vector3d(BEAM_WIDTH, LIFT_HEIGHT / 2, BEAM_WIDTH * 0.5);
        final Vector3d WEST_FACE_MIDPOINT = new Vector3d(0.0, LIFT_HEIGHT / 2, BEAM_WIDTH * 0.5);
        final Vector3d NORTH_FACE_MIDPOINT = new Vector3d(BEAM_WIDTH * 0.5, LIFT_HEIGHT / 2, 0.0);
        final Vector3d SOUTH_FACE_MIDPOINT = new Vector3d(BEAM_WIDTH * 0.5, LIFT_HEIGHT / 2, BEAM_WIDTH);
        final Vector3d UP_FACE_MIDPOINT = new Vector3d(BEAM_WIDTH * 0.5, LIFT_HEIGHT, BEAM_WIDTH * 0.5);
        final Vector3d DOWN_FACE_MIDPOINT = new Vector3d(BEAM_WIDTH * 0.5, 0.0, BEAM_WIDTH * 0.5);

        addFace(Direction.EAST, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                color, EAST_FACE_MIDPOINT, BEAM_WIDTH, LIFT_HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);

        addFace(Direction.WEST, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                color, WEST_FACE_MIDPOINT, BEAM_WIDTH, LIFT_HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);

        addFace(Direction.NORTH, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                color, NORTH_FACE_MIDPOINT, BEAM_WIDTH, LIFT_HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);

        addFace(Direction.SOUTH, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                color, SOUTH_FACE_MIDPOINT, BEAM_WIDTH, LIFT_HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);

        addFace(Direction.UP, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                color, UP_FACE_MIDPOINT, BEAM_WIDTH, BEAM_WIDTH, bottomLeftUV, UVwidth, UVheight, combinedLight);

        addFace(Direction.DOWN, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                color, DOWN_FACE_MIDPOINT, BEAM_WIDTH, BEAM_WIDTH, bottomLeftUV, UVwidth, UVheight, combinedLight);
    }

    private static void addFace(Direction whichFace,
                                Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                                Color color, Vector3d centrePos, float width, float height,
                                Vector2f bottomLeftUV, float texUwidth, float texVheight,
                                int lightmapValue) {
        // the Direction class has a bunch of methods which can help you rotate quads
        //  I've written the calculations out long hand, and based them on a centre position, to make it clearer what
        //   is going on.
        // Beware that the Direction class is based on which direction the face is pointing, which is opposite to
        //   the direction that the viewer is facing when looking at the face.
        // Eg when drawing the NORTH face, the face points north, but when we're looking at the face, we are facing south,
        //   so that the bottom left corner is the eastern-most, not the western-most!


        // calculate the bottom left, bottom right, top right, top left vertices from the VIEWER's point of view (not the
        //  face's point of view)

        Vector3f leftToRightDirection, bottomToTopDirection;

        switch (whichFace) {
            case NORTH: { // bottom left is east
                leftToRightDirection = new Vector3f(-1F, 0, 0);  // or alternatively Vector3f.XN
                bottomToTopDirection = new Vector3f(0, 1F, 0);  // or alternatively Vector3f.YP
                break;
            }
            case SOUTH: {  // bottom left is west
                leftToRightDirection = new Vector3f(1, 0, 0);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
            case EAST: {  // bottom left is south
                leftToRightDirection = new Vector3f(0, 0, -1);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
            case WEST: { // bottom left is north
                leftToRightDirection = new Vector3f(0, 0, 1);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
            case UP: { // bottom left is southwest by minecraft block convention
                leftToRightDirection = new Vector3f(-1, 0, 0);
                bottomToTopDirection = new Vector3f(0, 0, 1);
                break;
            }
            case DOWN: { // bottom left is northwest by minecraft block convention
                leftToRightDirection = new Vector3f(1, 0, 0);
                bottomToTopDirection = new Vector3f(0, 0, 1);
                break;
            }
            default: {  // should never get here, but just in case;
                leftToRightDirection = new Vector3f(0, 0, 1);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
        }
        leftToRightDirection.mul(0.5F * width);  // convert to half width
        bottomToTopDirection.mul(0.5F * height);  // convert to half height

        // calculate the four vertices based on the centre of the face

        Vector3f bottomLeftPos = new Vector3f(centrePos);
        bottomLeftPos.sub(leftToRightDirection);
        bottomLeftPos.sub(bottomToTopDirection);

        Vector3f bottomRightPos = new Vector3f(centrePos);
        bottomRightPos.add(leftToRightDirection);
        bottomRightPos.sub(bottomToTopDirection);

        Vector3f topRightPos = new Vector3f(centrePos);
        topRightPos.add(leftToRightDirection);
        topRightPos.add(bottomToTopDirection);

        Vector3f topLeftPos = new Vector3f(centrePos);
        topLeftPos.sub(leftToRightDirection);
        topLeftPos.add(bottomToTopDirection);

        // texture coordinates are "upside down" relative to the face
        // eg bottom left = [U min, V max]
        Vector2f bottomLeftUVpos = new Vector2f(bottomLeftUV.x, bottomLeftUV.y);
        Vector2f bottomRightUVpos = new Vector2f(bottomLeftUV.x + texUwidth, bottomLeftUV.y);
        Vector2f topLeftUVpos = new Vector2f(bottomLeftUV.x + texUwidth, bottomLeftUV.y + texVheight);
        Vector2f topRightUVpos = new Vector2f(bottomLeftUV.x, bottomLeftUV.y + texVheight);

        Vector3f normalVector = whichFace.toVector3f();  // gives us the normal to the face

        addQuad(matrixPos, matrixNormal, renderBuffer,
                bottomLeftPos, bottomRightPos, topRightPos, topLeftPos,
                bottomLeftUVpos, bottomRightUVpos, topLeftUVpos, topRightUVpos,
                normalVector, color, lightmapValue);
    }

    /**
     * Add a quad.
     * The vertices are added in anti-clockwise order from the VIEWER's  point of view, i.e.
     * bottom left; bottom right, top right, top left
     * If you add the vertices in clockwise order, the quad will face in the opposite direction; i.e. the viewer will be
     *   looking at the back face, which is usually culled (not visible)
     * See
     * http://greyminecraftcoder.blogspot.com/2014/12/the-tessellator-and-worldrenderer-18.html
     * http://greyminecraftcoder.blogspot.com/2014/12/block-models-texturing-quads-faces.html
     */
    private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                                Vector3f blpos, Vector3f brpos, Vector3f trpos, Vector3f tlpos,
                                Vector2f blUVpos, Vector2f brUVpos, Vector2f trUVpos, Vector2f tlUVpos,
                                Vector3f normalVector, Color color, int lightmapValue) {
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, blpos, blUVpos, normalVector, color, lightmapValue);
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, brpos, brUVpos, normalVector, color, lightmapValue);
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, trpos, trUVpos, normalVector, color, lightmapValue);
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, tlpos, tlUVpos, normalVector, color, lightmapValue);
    }

    // suitable for vertexbuilders using the DefaultVertexFormats.ENTITY format
    private static void addQuadVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder renderBuffer,
                                      Vector3f pos, Vector2f texUV,
                                      Vector3f normalVector, Color color, int lightmapValue) {
        renderBuffer.pos(matrixPos, pos.getX(), pos.getY(), pos.getZ()) // position coordinate
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())        // color
                .tex(texUV.x, texUV.y)                     // texel coordinate
                .overlay(OverlayTexture.NO_OVERLAY)  // only relevant for rendering Entities (Living)
                .lightmap(lightmapValue)             // lightmap with full brightness
                .normal(matrixNormal, normalVector.getX(), normalVector.getY(), normalVector.getZ())
                .endVertex();
    }

}
