package com.uberhelixx.flatlights.common.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketLeftClick;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponEvents {
    //Check for left click on full swing to perform an action
    @SubscribeEvent
    public static void leftClickEvent(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack heldItem = event.getItemStack();
        if(!heldItem.isEmpty() && heldItem.getItem() == ModItems.PRISMATIC_SWORD.get()) {
            PacketHandler.sendToServer(new PacketLeftClick());
        }
        if(!heldItem.isEmpty() && heldItem.getItem() == ModItems.PRISMATIC_BLADEMK2.get() && heldItem.getTag() != null && heldItem.getTag().contains(PrismaticBladeMk2.MODE_TAG) && heldItem.getTag().getInt(PrismaticBladeMk2.MODE_TAG) == PrismaticBladeMk2.AURA_MODE) {
            PacketHandler.sendToServer(new PacketLeftClick());
        }
        /*if(event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get()) != null && event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue() > 5.0) {
            PacketHandler.sendToServer(new PacketReachHit());
        }*/
    }
}
