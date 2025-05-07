package com.uberhelixx.flatlights.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LightStorageScreen extends AbstractContainerScreen<LightStorageMenu> {
    private static final ResourceLocation GUI = new ResourceLocation(FlatLights.MODID, "textures/gui/light_storage_gui.png");
    
    public LightStorageScreen(LightStorageMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    
    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 8 - 33 - 2;
        this.titleLabelY = 6 - 30 - 12;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.getYSize() - 94 + 65;
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI);
        int x = getGuiLeft();
        int y = getGuiTop();
        guiGraphics.blit(GUI, x - 33, y - 30, 0, 0, 242, 172);
        //committing the worst collage of all time to make a title tab for this thing that's too tall
        guiGraphics.blit(GUI, x - 33 + 4, y - 30 - 4, 36, 166, 101, 7);
        guiGraphics.blit(GUI, x - 33 + 4, y - 30 - 4 - 7, 36, 166, 101, 7);
        guiGraphics.blit(GUI, x - 33, y - 30 - 4 - 7, 0, 0, 4, 20);
        guiGraphics.blit(GUI, x - 33, y - 30 - 4 - 7, 0, 0, 106, 3);
        guiGraphics.blit(GUI, x - 33 + 105, y - 30 - 4 - 7 + 1, 239, 1, 3, 11);
        guiGraphics.blit(GUI, x - 33 + 105, y - 30 - 4 - 7 + 1 + 11, 34, 169, 2, 2);
        //increasing space between container and inventory GUI slots so that the title isn't cut off slightly
        guiGraphics.blit(GUI, x - 33 + 33, y - 30 + 172 + 3, 33, 172, 176, 84);
        guiGraphics.blit(GUI, x - 33 + 33, y - 30 + 172 + 1, 33, 171, 176, 2);
        guiGraphics.blit(GUI, x - 33 + 33, y - 30 + 172, 33, 171, 176, 2);
        
    }

    
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
