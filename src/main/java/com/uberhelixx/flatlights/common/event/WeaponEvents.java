package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketLeftClick;
import com.uberhelixx.flatlights.network.PacketReachHit;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponEvents {

    //Prismatic Sword check for left click on full swing to throw bomb
    @SubscribeEvent
    public static void bombSwingTrigger(PlayerInteractEvent.LeftClickEmpty event) {
        if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() == ModItems.PRISMATIC_SWORD.get()) {
            PacketHandler.sendToServer(new PacketLeftClick());
        }
        if(event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get()) != null && event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue() > 5.0) {
            PacketHandler.sendToServer(new PacketReachHit());
        }
    }
}
