package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.common.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BombEntity extends AbstractArrow {
    //returns nothing because this isn't meant to be picked up ever
    @Override
    protected ItemStack getPickupItem() {
        return ModItems.GUN_RAT.get().getDefaultInstance();
    }
    
    protected BombEntity(EntityType<? extends AbstractArrow> type, Level levelIn) {
        super(type, levelIn);
    }
    
    public BombEntity(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level levelIn) {
        super(type, x, y, z, levelIn);
    }
    
    public BombEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level levelIn) {
        super(type, shooter, levelIn);
    }
    
    @Override
    public void setSoundEvent(SoundEvent pSoundEvent) {
        super.setSoundEvent(SoundEvents.EMPTY);
    }
    
    //explosion radius
    private final float bombRadius = 3.0f;
    
    @Override
    public void tick() {
        super.tick();
        //remove if projectile sits and doesn't do anything for 10 seconds
        if(this.tickCount > 200) {
            this.remove(RemovalReason.DISCARDED);
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        //this, x, y, z, explosionStrength, setsFires, explosionInteraction
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), bombRadius, false, Level.ExplosionInteraction.NONE);
        this.remove(RemovalReason.DISCARDED);
    }
    
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), bombRadius, false, Level.ExplosionInteraction.NONE);
        this.remove(RemovalReason.DISCARDED);
    }
    
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
