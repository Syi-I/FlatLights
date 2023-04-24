package com.uberhelixx.flatlights.screen;

import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.util.text.ITextComponent;

public class SpectrumAnvilScreen extends AnvilScreen {

    public SpectrumAnvilScreen(RepairContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

}
