package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.block.Mob_B_Gone;
import com.uberhelixx.flatlights.block.PlatingMachineBlock;
import com.uberhelixx.flatlights.block.SpectrumAnvilBlock;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MiscEventHelpers {
    @SubscribeEvent
    public static void indevPlaced(BlockEvent.EntityPlaceEvent event) {
        Block block = event.getPlacedBlock().getBlock();
        if(!FlatLightsCommonConfig.indevBlocks.get()) {
            if (block instanceof Mob_B_Gone || block instanceof PlatingMachineBlock || block instanceof SpectrumAnvilBlock) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void quantumDmg(LivingHurtEvent event) {
        if(event.getSource() == ModDamageTypes.QUANTUM) {
            FlatLights.LOGGER.info("Dealt " + event.getAmount() + " quantum damage");
        }
    }

    @SubscribeEvent
    public static void entangledDmg(LivingHurtEvent event) {
        if(event.getSource() == ModDamageTypes.ENTANGLED) {
            FlatLights.LOGGER.info("Dealt " + event.getAmount() + " entangled damage");
        }
    }
}
