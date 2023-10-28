package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.commands.StatTestCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
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
}
