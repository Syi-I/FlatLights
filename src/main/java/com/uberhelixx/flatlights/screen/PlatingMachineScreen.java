package com.uberhelixx.flatlights.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.container.PlatingMachineContainer;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PlatingMachineScreen extends ContainerScreen<PlatingMachineContainer> {
    private final ResourceLocation GUI = new ResourceLocation(FlatLights.MOD_ID, "textures/gui/plating_machine_gui2.png");

    public PlatingMachineScreen(PlatingMachineContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
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
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize + 2);
        double progressPercent = container.getPlateTime() / 100.0;
        int width = (int) Math.round(progressPercent * 25);
        MiscHelpers.debugLogger("Progress percent in GUI: " + progressPercent);
        MiscHelpers.debugLogger("Width in GUI: " + width);
        this.blit(matrixStack, i + 74, j + 37, 176, 0, width, 26);
    }
}
