package com.uberhelixx.flatlights.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SpectralizerScreen extends AbstractContainerScreen<SpectralizerMenu> {
    private static final ResourceLocation GUI = new ResourceLocation(FlatLights.MODID, "textures/gui/spectralizer_gui.png");
    
    public SpectralizerScreen(SpectralizerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = this.getYSize() - 62;
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        int x = getGuiLeft();
        int y = getGuiTop();
        guiGraphics.blit(GUI, x, y, 0, 0, this.getXSize(), 200);
        
        renderProgressBar(guiGraphics, x, y);
    }
    
    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            //slot outlines starting top going clockwise
            guiGraphics.blit(GUI, x + 77, y + 9, 176, 22, 22, 22); //red - 1
            guiGraphics.blit(GUI, x + 113, y + 30, 176, 110, 22, 22); //orange - 2
            guiGraphics.blit(GUI, x + 113, y + 69, 176, 88, 22, 22); //yellow - 3
            guiGraphics.blit(GUI, x + 77, y + 89, 176, 0, 22, 22); //green - 4
            guiGraphics.blit(GUI, x + 41, y + 69, 176, 44, 22, 22); //blue - 5
            guiGraphics.blit(GUI, x + 41, y + 30, 176, 66, 22, 22); //purple - 6
            
            guiGraphics.blit(GUI, x + 73, y + 45 + (30 - menu.getScaledProgress()), 198, 30 - menu.getScaledProgress(), 30, menu.getScaledProgress()); //rgb - output
        }
    }
    
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
