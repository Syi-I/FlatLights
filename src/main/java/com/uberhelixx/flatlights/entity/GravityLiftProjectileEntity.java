package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GravityLiftProjectileEntity extends ProjectileItemEntity {

    public GravityLiftProjectileEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public GravityLiftProjectileEntity(World worldIn) {
        super(ModEntityTypes.GRAVITY_LIFT_PROJECTILE_ENTITY.get(), worldIn);
    }

    public GravityLiftProjectileEntity(LivingEntity livingEntityIn, World worldIn) {
        super(ModEntityTypes.GRAVITY_LIFT_PROJECTILE_ENTITY.get(), livingEntityIn, worldIn);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GRAVITY_LIFT.get();
    }

     //Called when the projectile hits an entity
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        //if the projectile hit, do zero damage but still bonk the entity
        result.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), 0.0F);
    }

    //Called when this EntityFireball hits a block or entity.
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        Entity entity = this.getShooter();

        //particle effects from the ender pearl code, might test how it looks ingame first
        /*
        for(int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY() + this.rand.nextDouble() * 2.0D, this.getPosZ(), this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }*/

        //summon the gravity lift entity at the impact location
        if (!this.world.isRemote && !this.removed) {
            GravityLiftEntity gravityLiftEntity;
            //if there's a living entity that threw the item, give it kill credit/name it as the source of damage
            if(entity instanceof LivingEntity) {
                gravityLiftEntity = new GravityLiftEntity(ModEntityTypes.GRAVITY_LIFT_ENTITY.get(), (LivingEntity) entity, world);
            }
            else {
                gravityLiftEntity = new GravityLiftEntity(ModEntityTypes.GRAVITY_LIFT_ENTITY.get(), world);
            }

            //actually place the gravity lift at the location in the world
            gravityLiftEntity.setLocationAndAngles(result.getHitVec().getX(), result.getHitVec().getY(), result.getHitVec().getZ(), 0f, 0f);
            gravityLiftEntity.setNoGravity(true);
            world.addEntity(gravityLiftEntity);

            //remove the thrown item projectile entity from the world now that it hit something
            this.remove();
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        Entity entity = this.getShooter();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            this.remove();
        } else {
            super.tick();
        }

    }

    @Nullable
    public Entity changeDimension(ServerWorld server, net.minecraftforge.common.util.ITeleporter teleporter) {
        Entity entity = this.getShooter();
        if (entity != null && entity.world.getDimensionKey() != server.getDimensionKey()) {
            this.setShooter((Entity)null);
        }

        return super.changeDimension(server, teleporter);
    }
}
