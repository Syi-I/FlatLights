package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.common.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class PortableBlackHoleProjectileEntity extends ThrowableItemProjectile {
    public PortableBlackHoleProjectileEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        
        Entity entity = this.getOwner();
        
        //particle effects from the ender pearl code, might test how it looks ingame first
        /*
        for(int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY() + this.rand.nextDouble() * 2.0D, this.getPosZ(), this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }*/
        
        //summon the black hole entity at the impact location
        if (!this.level().isClientSide() && !this.isRemoved()) {
            PortableBlackHoleEntity blackHoleEntity;
            //if there's a living entity that threw the item, give it kill credit/name it as the source of damage
            if(entity instanceof LivingEntity) {
                blackHoleEntity = new PortableBlackHoleEntity(ModEntityTypes.PORTABLE_BLACK_HOLE_ENTITY.get(), (LivingEntity) entity, level());
                blackHoleEntity.setOwner(entity);
                //MiscUtils.infoLog("set owner of this black hole entity to " + entity);
            }
            else {
                blackHoleEntity = new PortableBlackHoleEntity(ModEntityTypes.PORTABLE_BLACK_HOLE_ENTITY.get(), level());
            }
            
            //actually place the black hole at the location in the world
            blackHoleEntity.absMoveTo(pResult.getLocation().x(), pResult.getLocation().y(), pResult.getLocation().z(), 0f, 0f);
            blackHoleEntity.setNoGravity(true);
            level().addFreshEntity(blackHoleEntity);
            
            //remove the thrown item projectile entity from the world now that it hit something
            this.remove(RemovalReason.DISCARDED);
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(pResult.getEntity().damageSources().indirectMagic(this, this.getOwner()), 0.0f);
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    public PortableBlackHoleProjectileEntity(Level pLevel) {
        super(ModEntityTypes.PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY.get(), pLevel);
    }
    
    public PortableBlackHoleProjectileEntity(LivingEntity livingEntity, Level pLevel) {
        super(ModEntityTypes.PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY.get(), livingEntity, pLevel);
    }
    
    @Override
    protected Item getDefaultItem() {
        return ModItems.PORTABLE_BLACKHOLE.get();
    }
    
    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) {
            this.remove(RemovalReason.DISCARDED);
        } else {
            super.tick();
        }
    }
    
    @Nullable
    public Entity changeDimension(ServerLevel server, net.minecraftforge.common.util.ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level().dimension() != server.dimension()) {
            this.setOwner(null);
        }
        
        return super.changeDimension(server, teleporter);
    }
}
