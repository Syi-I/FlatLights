package com.uberhelixx.flatlights.common.network.packets;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAirstrafeMovement {
    private float forwardVal;
    private float strafeVal;
    private boolean jumpVal;
    private boolean sneakVal;
    public PacketAirstrafeMovement() {
    
    }
    public PacketAirstrafeMovement(float forward, float strafe, boolean jump, boolean sneak) {
        forwardVal = forward;
        strafeVal = strafe;
        jumpVal = jump;
        sneakVal = sneak;
    }
    
    public static void encode(PacketAirstrafeMovement msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.forwardVal);
        buf.writeFloat(msg.strafeVal);
        buf.writeBoolean(msg.jumpVal);
        buf.writeBoolean(msg.sneakVal);
    }
    
    public static PacketAirstrafeMovement decode(FriendlyByteBuf buf) {
        return new PacketAirstrafeMovement(buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean());
    }
    
    public static void handle(PacketAirstrafeMovement msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if(player != null) {
                    //player.addDeltaMovement(processStrafes(msg.forwardVal, msg.strafeVal, player));
                    processStrafes(msg.forwardVal, msg.strafeVal, player);
                    //player.setDeltaMovement(processStrafes(msg.forwardVal, msg.strafeVal));
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
    
    /**
     * Determines the direction of a player's strafe input
     * @param forwardStrafe The forward/backward strafe value from the player's inputs
     * @param sideStrafe The left/right strafe value from the player's inputs
     *  The movement direction that will be added to the player as a {@link Vec3}, meant for {@link net.minecraft.world.entity.player.Player#addDeltaMovement(Vec3)}
     */
    public static void processStrafes(float forwardStrafe, float sideStrafe, Player player) {
        float moveIncrease = 0.1f;
        double side = 0;
        double forward = 0;
        Vec3 multVec = new Vec3(0, 0, 0);
        //positive means forward, negative means backward
        //forward
        if(forwardStrafe > 0) {
            forward = moveIncrease * -1;
            multVec = multVec.add(0, 0, forward);
            MiscUtils.infoLog("[Strafe Packet] Pressed Forward.");
            
        }
        //backward
        else if(forwardStrafe < 0) {
            forward = moveIncrease * 1;
            multVec = multVec.add(0, 0, forward);
            MiscUtils.infoLog("[Strafe Packet] Pressed Backward.");
            
        }
        //positive means left, negative means right
        //left
        if(sideStrafe > 0) {
            side = moveIncrease * -1;
            multVec = multVec.add(side, 0, 0);
            MiscUtils.infoLog("[Strafe Packet] Pressed Left.");
            
        }
        //right
        else if(sideStrafe < 0) {
            side = moveIncrease * 1;
            multVec = multVec.add(side, 0, 0);
            MiscUtils.infoLog("[Strafe Packet] Pressed Right.");
            
        }
        multVec = multVec.yRot((float) player.getLookAngle().y);
        MiscUtils.infoLog("[multVec] " + multVec);
        //Vec3 moveVec =
        //Vec3 playerMoveVec = new Vec3(player.getDeltaMovement().x(), 0, player.getDeltaMovement().z());
        //player.moveRelative(moveIncrease, multVec);
        player.addDeltaMovement(multVec);
    }
}
