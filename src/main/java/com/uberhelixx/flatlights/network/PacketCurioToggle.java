package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.item.curio.BaseCurio;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Supplier;

import static com.uberhelixx.flatlights.item.curio.BaseCurio.CUBE_SLOT_ID;
import static com.uberhelixx.flatlights.item.curio.BaseCurio.SET_EFFECT_TOGGLE;

public class PacketCurioToggle {

    public static void encode(PacketCurioToggle msg, PacketBuffer buf) {
    }

    public static PacketCurioToggle decode(PacketBuffer buf) {
        return new PacketCurioToggle();
    }

    public static void handle(PacketCurioToggle msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if(player != null){
                    ItemStack stack = getCurio(player);
                    if(stack != null && !stack.isEmpty())
                        BaseCurio.toggleSetEffect(stack, player);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public static ItemStack getCurio(PlayerEntity playerIn) {
        Optional<SlotResult> slotResult = CuriosApi.getCuriosHelper().findCurio(playerIn, CUBE_SLOT_ID, 0);
        ItemStack hand = playerIn.getHeldItem(Hand.MAIN_HAND);
        //check slot result of cube slot first (this is the only curio slot with set effects)
        if(slotResult.isPresent()) {
            ItemStack curio = slotResult.get().getStack();
            CompoundNBT tag = curio.getTag();
            //check if the curio is one with the set effect toggle
            if(tag != null && !tag.isEmpty() && tag.contains(SET_EFFECT_TOGGLE)) {
                return curio;
            }
        }
        //check main hand to see if player is holding a curio with set effect toggles
        else if(hand.getItem() instanceof BaseCurio){
            CompoundNBT tag = hand.getTag();
            if(tag != null && !tag.isEmpty() && tag.contains(SET_EFFECT_TOGGLE)) {
                return hand;
            }
        }
        return null;
    }
}
