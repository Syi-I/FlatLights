package com.uberhelixx.flatlights.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.container.SpectralizerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class SpectralizerScreen extends ContainerScreen<SpectralizerContainer> {
    private final ResourceLocation GUI = new ResourceLocation(FlatLights.MOD_ID, "textures/gui/spectralizer_gui.png");

    public SpectralizerScreen(SpectralizerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.playerInventoryTitleY = this.ySize - 62;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, 200);
        //current infuse time / total infuse time
        double progressPercent = container.getInfuseTime() / 80.0;

        //total filled is percent * max width or height (whatever direction the bar fills)
        int height = (int) Math.round(progressPercent * 30);

        //slot outlines starting top going clockwise
        if(container.getWorkingStatus()) {
            this.blit(matrixStack, i + 77, j + 9, 176, 22, 22, 22); //red - 1
            this.blit(matrixStack, i + 113, j + 30, 176, 110, 22, 22); //orange - 2
            this.blit(matrixStack, i + 113, j + 69, 176, 88, 22, 22); //yellow - 3
            this.blit(matrixStack, i + 77, j + 89, 176, 0, 22, 22); //green - 4
            this.blit(matrixStack, i + 41, j + 69, 176, 44, 22, 22); //blue - 5
            this.blit(matrixStack, i + 41, j + 30, 176, 66, 22, 22); //purple - 6
        }
        this.blit(matrixStack, i + 73, j + 45 + (30 - height), 198, 30 - height, 30, height); //rgb - output
    }
}
