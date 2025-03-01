package com.uberhelixx.flatlights.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;


public class SpectrumAnvilTile extends TileEntity {

    public SpectrumAnvilTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public SpectrumAnvilTile() {
        this(ModTileEntities.SPECTRUM_ANVIL_TILE.get());
    }

}
