package com.uberhelixx.flatlights.common.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.commands.StatTestCommand;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        Level level = original.getCommandSenderWorld();
        if(!level.isClientSide() && PrismaticBladeMk2.hasCoreTracker(original)) {
            newPlayer.getPersistentData().putInt(PrismaticBladeMk2.PLAYER_CORETRACKER_TAG,
                    original.getPersistentData().getInt(PrismaticBladeMk2.PLAYER_CORETRACKER_TAG));
        }
    }
    
    @SubscribeEvent
    public static void coreSync(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity killer = event.getSource().getEntity();
        if(killer instanceof Player player && entity instanceof LivingEntity mob) {
            //gained cores is equal to how many times more HP the mob had compared to the player's base 20 HP
            int gainedCores = Math.max((Math.round(mob.getMaxHealth() / 20)), 1);
            
            //only try to update if the player has tracker data present
            if(PrismaticBladeMk2.hasCoreTracker(player)) {
                //get current core count from tracker and add gained cores to it, then update player data
                PrismaticBladeMk2.increasePlayerCores(player, gainedCores);
            }
        }
    }
}
