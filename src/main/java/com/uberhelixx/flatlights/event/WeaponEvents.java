package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketLeftClick;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WeaponEvents {

    //Prismatic Sword check for left click on full swing to throw bomb
    @SubscribeEvent
    public static void bombSwingTrigger(PlayerInteractEvent.LeftClickEmpty event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() == ModItems.PRISMATIC_SWORD.get()) {
            PacketHandler.sendToServer(new PacketLeftClick());
        }
    }
}
