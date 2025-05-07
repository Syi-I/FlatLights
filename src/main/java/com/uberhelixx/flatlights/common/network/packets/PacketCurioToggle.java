package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;
import java.util.function.Supplier;

import static com.uberhelixx.flatlights.common.item.curio.CurioUtils.CUBE_SLOT_ID;

public class PacketCurioToggle {
    public static void encode(PacketCurioToggle msg, FriendlyByteBuf buf) {
    }
    
    public static PacketCurioToggle decode(FriendlyByteBuf buf) {
        return new PacketCurioToggle();
    }
    
    public static void handle(PacketCurioToggle msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if(player != null){
                    ItemStack stack = getCurio(player);
                    if(stack != null && !stack.isEmpty())
                        CurioUtils.toggleSetEffect(stack, player);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
    
    //TODO: check if this map thing actually returns the correct item ever
    public static ItemStack getCurio(Player playerIn) {
        ItemStack hand = playerIn.getItemInHand(InteractionHand.MAIN_HAND);
        
        ICuriosItemHandler curioInv = CuriosApi.getCuriosInventory(playerIn).orElse(null);
        if(curioInv != null) {
            Optional<SlotResult> slotResult = curioInv.findCurio(CUBE_SLOT_ID, 0);
            //check slot result of cube slot first (this is the only curio slot with set effects)
            if(slotResult.isPresent()) {
                ItemStack curio = slotResult.get().stack();
                CompoundTag tag = curio.getTag();
                //check if the curio is one with the set effect toggle
                if(tag != null && !tag.isEmpty() && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    FlatLights.LOGGER.info("[PacketCurioToggle] Returning worn curio.");
                    return curio;
                }
            }
            //check main hand to see if player is holding a curio with set effect toggles
            else if(hand.getItem() instanceof BaseCurio){
                CompoundTag tag = hand.getTag();
                if(tag != null && !tag.isEmpty() && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    FlatLights.LOGGER.info("[PacketCurioToggle] Returning held curio.");
                    return hand;
                }
            }
        }
        
        FlatLights.LOGGER.info("[PacketCurioToggle] Returning null from no curios inventory.");
        return null;
    }
}
