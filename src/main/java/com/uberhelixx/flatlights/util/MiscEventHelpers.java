package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLightsConfig;
import com.uberhelixx.flatlights.block.Mob_B_Gone;
import com.uberhelixx.flatlights.block.PlatingMachineBlock;
import com.uberhelixx.flatlights.block.SpectrumAnvilBlock;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MiscEventHelpers {
    @SubscribeEvent
    public static void indevPlaced(BlockEvent.EntityPlaceEvent event) {
        Block block = event.getPlacedBlock().getBlock();
        if(!FlatLightsConfig.indevBlocks.get()) {
            if (block instanceof Mob_B_Gone || block instanceof PlatingMachineBlock || block instanceof SpectrumAnvilBlock) {
                event.setCanceled(true);
            }
        }
    }
}
