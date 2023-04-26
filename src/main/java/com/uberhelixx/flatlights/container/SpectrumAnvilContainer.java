package com.uberhelixx.flatlights.container;

import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.rmi.registry.RegistryHandler;

public class SpectrumAnvilContainer extends RepairContainer {
    private final IWorldPosCallable posMain;

    public SpectrumAnvilContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPos) {
        super(id, playerInventory, worldPos);
        this.posMain = worldPos;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return isWithinUsableDistance(this.posMain, player, ModBlocks.SPECTRUM_ANVIL.get());
    }
}
