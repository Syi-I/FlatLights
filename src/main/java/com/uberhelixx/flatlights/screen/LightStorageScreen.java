package com.uberhelixx.flatlights.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.container.LightStorageContainer;
import com.uberhelixx.flatlights.container.PlatingMachineContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LightStorageScreen extends ContainerScreen<LightStorageContainer> {
    private final ResourceLocation GUI = new ResourceLocation(FlatLights.MOD_ID, "textures/gui/light_storage_gui.png");

    public LightStorageScreen(LightStorageContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.titleX = 8 - 33 - 2;
        this.titleY = 6 - 30 - 12;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.ySize - 94 + 65;
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
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i - 33, j - 30, 0, 0, 242, 172);
        //committing the worst collage of all time to make a title tab for this thing that's too tall
        this.blit(matrixStack, i - 33 + 4, j - 30 - 4, 36, 166, 101, 7);
        this.blit(matrixStack, i - 33 + 4, j - 30 - 4 - 7, 36, 166, 101, 7);
        this.blit(matrixStack, i - 33, j - 30 - 4 - 7, 0, 0, 4, 20);
        this.blit(matrixStack, i - 33, j - 30 - 4 - 7, 0, 0, 106, 3);
        this.blit(matrixStack, i - 33 + 105, j - 30 - 4 - 7 + 1, 239, 1, 3, 11);
        this.blit(matrixStack, i - 33 + 105, j - 30 - 4 - 7 + 1 + 11, 34, 169, 2, 2);
        //increasing space between container and inventory GUI slots so that the title isn't cut off slightly
        this.blit(matrixStack, i - 33 + 33, j - 30 + 172 + 3, 33, 172, 176, 84);
        this.blit(matrixStack, i - 33 + 33, j - 30 + 172 + 1, 33, 171, 176, 2);
        this.blit(matrixStack, i - 33 + 33, j - 30 + 172, 33, 171, 176, 2);

    }
}
