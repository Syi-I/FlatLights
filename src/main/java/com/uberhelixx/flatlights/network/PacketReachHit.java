package com.uberhelixx.flatlights.network;

import com.uberhelixx.flatlights.item.tools.PrismaticSword;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class PacketReachHit {
    public static void encode(PacketReachHit msg, PacketBuffer buf) {}

    public static PacketReachHit decode(PacketBuffer buf) {
        return new PacketReachHit();
    }

    public static void handle(PacketReachHit msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = ctx.get().getSender();
                if(player != null && !player.getEntityWorld().isRemote()) {
                    doReachHit(player);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

    private static void doReachHit(PlayerEntity attacker) {
        //World world = attacker.world;
        double reach = attacker.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
        MiscHelpers.debugLogger("[PacketReachHit] Reach Range: " + reach);
        double reachSqr = reach * reach;
/*
        //end of the player's view vector
        Vector3d viewVec = attacker.getLook(1.0F);
        //position of the player's eyes, beginning of the view vector
        Vector3d eyeVec = attacker.getEyePosition(1.0F);
        //extend the player's view vector range by a factor of the player's modified block reach attribute
        Vector3d targetVec = eyeVec.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);

        //expanding the attacker's bounding box by the view vector's scale, and inflating it by 4.0D (x, y, z)
        AxisAlignedBB viewBB = attacker.getBoundingBox().expand(viewVec.scale(reach)).expand(4.0D, 4.0D, 4.0D);
        EntityRayTraceResult result = ProjectileHelper.rayTraceEntities(world, attacker, eyeVec, targetVec, viewBB, EntityPredicates.NOT_SPECTATING);
*/
        EntityRayTraceResult result = raytraceEntity(attacker, reach);
        if (result == null || !(result.getEntity() instanceof LivingEntity)) {
            return;
        }

        //find the entity from the results of the raytrace
        LivingEntity raytraceTarget = (LivingEntity) result.getEntity();

        //need to use squared distance because that's the only way to use the raytraced results
        double distanceToTargetSqr = attacker.getDistanceSq(raytraceTarget);
        MiscHelpers.debugLogger("[PacketReachHit] Reach Squared: " + reachSqr);
        MiscHelpers.debugLogger("[PacketReachHit] Distance to target squared: " + distanceToTargetSqr);

        //did we get an entity from the raytrace result or not
        boolean hitResult = (result != null ? raytraceTarget : null) != null;

        //if we hit something along the path of the new vector result, trigger the hit as if the player were hitting the target
        if (hitResult) {
            if (attacker instanceof PlayerEntity) {
                if (reachSqr >= distanceToTargetSqr) {
                    attacker.attackTargetEntityWithCurrentItem(raytraceTarget);
                }
            }
        }
    }

    private static EntityRayTraceResult raytraceEntity(PlayerEntity playerIn, double reachIn) {
        World world = playerIn.getEntityWorld();
        //end of the player's view vector
        Vector3d viewVec = playerIn.getLook(1.0F);
        //position of the player's eyes, beginning of the view vector
        Vector3d eyeVec = playerIn.getEyePosition(1.0F);
        //extend the player's view vector range by a factor of the player's modified block reach attribute
        Vector3d targetVec = eyeVec.add(viewVec.x * reachIn, viewVec.y * reachIn, viewVec.z * reachIn);

        //expanding the attacker's bounding box by the view vector's scale, and inflating it by 4.0D (x, y, z)
        AxisAlignedBB viewBB = playerIn.getBoundingBox().expand(viewVec.scale(reachIn)).expand(4.0D, 4.0D, 4.0D);
        EntityRayTraceResult result = ProjectileHelper.rayTraceEntities(world, playerIn, eyeVec, targetVec, viewBB, EntityPredicates.NOT_SPECTATING);
        return result;
    }
}
