package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
public class AttributeFunctions {

    @SubscribeEvent
    public static void dodgeChance(LivingHurtEvent event) {
        //make sure the thing getting damaged is a player, because nothing else will have dodge chance sources
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            //dodge chance is stored as a value from 0-100, have to divide by 100 to compare with .nextDouble() values
            double dodge = player.getAttribute(ModAttributes.DODGE_CHANCE.get()).getValue() / 100;
            //checks against player's dodge chance, and cancels if the next value rolls under the chance
            if(player.getEntityWorld().getRandom().nextDouble() <= dodge) {
                event.setCanceled(true);
            }
        }
    }
}
