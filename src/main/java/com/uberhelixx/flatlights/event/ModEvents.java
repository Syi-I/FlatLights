package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.commands.StatTestCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.PLAYER_CORETRACKER_TAG;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
public class ModEvents {

    //registers the command, can do multiple different commands here if there are eventually more
    @SubscribeEvent
    public static void StatTestCommand(RegisterCommandsEvent event) {
        new StatTestCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    //have to do this so that persistent data is remembered for the player
    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event) {
        if(!event.getOriginal().getEntityWorld().isRemote() && event.getOriginal().getPersistentData().contains(PLAYER_CORETRACKER_TAG)) {
            event.getPlayer().getPersistentData().putInt(PLAYER_CORETRACKER_TAG,
                    event.getOriginal().getPersistentData().getInt(PLAYER_CORETRACKER_TAG));
        }
    }

    @SubscribeEvent
    public static void coreSync(LivingDeathEvent event) {
        Entity mob = event.getEntity();
        Entity killer = event.getSource().getTrueSource();
        if(killer instanceof PlayerEntity && mob instanceof LivingEntity) {
            //gained cores is equal to how many times more HP the mob had compared to the player's base 20 HP
            int gainedCores = Math.max((Math.round(((LivingEntity) mob).getMaxHealth() / 20)), 1);

            //get persistent data from player
            CompoundNBT playerData = killer.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            //only try to update if the player has tracker data present
            if(playerData.contains(PLAYER_CORETRACKER_TAG)) {
                //get current core count from tracker and add gained cores to it, then update player data
                int currCores = playerData.getInt(PLAYER_CORETRACKER_TAG);
                int newCoreCount = currCores + gainedCores;
                playerData.putInt(PLAYER_CORETRACKER_TAG, newCoreCount);
            }
        }
    }
}
