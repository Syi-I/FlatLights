package com.uberhelixx.flatlights.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PlatingMachineScreen extends AbstractContainerScreen<PlatingMachineMenu> {
    private static final ResourceLocation GUI = new ResourceLocation(FlatLights.MODID, "textures/gui/plating_machine_gui.png");
    
    public PlatingMachineScreen(PlatingMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        int x = getGuiLeft();
        int y = getGuiTop();
        guiGraphics.blit(GUI, x, y, 0, 0, this.getXSize(), this.getYSize() + 2);
        
        renderProgressBar(guiGraphics, x, y);
    }
    
    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            //double progressPercent = this. / 100.0;
            //int width = (int) Math.round(progressPercent * 25);
            guiGraphics.blit(GUI, x + 74, y + 32, 176, 0, menu.getScaledProgress(), 26);
        }
    }
    
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
