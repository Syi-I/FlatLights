package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.block.Mob_B_Gone;
import com.uberhelixx.flatlights.block.SpectrumAnvilBlock;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiscEventHelpers {
    @SubscribeEvent
    public static void indevPlaced(BlockEvent.EntityPlaceEvent event) {
        Block block = event.getPlacedBlock().getBlock();
        if(!FlatLightsCommonConfig.indevBlocks.get()) {
            if (block instanceof Mob_B_Gone || block instanceof SpectrumAnvilBlock) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void quantumDmgCheck(LivingHurtEvent event) {
        if(event.getSource() == ModDamageTypes.QUANTUM) {
            MiscHelpers.debugLogger("[DMG Event Helper] Dealt " + event.getAmount() + " quantum damage");
        }
    }

    @SubscribeEvent
    public static void entangledDmgCheck(LivingHurtEvent event) {
        if(event.getSource() == ModDamageTypes.ENTANGLED) {
            MiscHelpers.debugLogger("[DMG Event Helper] Dealt " + event.getAmount() + " entangled damage");
        }
    }
    
    @SubscribeEvent
    public static void toeStub(LivingDamageEvent event) {
        float stubChance = 0.1f;
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            UUID playerId = player.getUniqueID();
            for(UUID id : MiscHelpers.players) {
                if(id.equals(playerId)) {
                    if(player.getEntityWorld().getRandom().nextFloat() <= stubChance) {
                        player.setHealth(1);
                        event.setAmount((float) MathHelper.absMax(event.getAmount(), 1.0));
                    }
                }
            }
        }
    }
}
