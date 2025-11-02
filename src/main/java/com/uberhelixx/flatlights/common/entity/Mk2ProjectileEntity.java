package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.startup.registry.ModSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class Mk2ProjectileEntity extends AbstractArrow {
    protected Mk2ProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    public Mk2ProjectileEntity(Level pLevel) {
        super(ModEntityTypes.MK2_PROJECTILE_ENTITY.get(), pLevel);
    }
    
    public Mk2ProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, LivingEntity livingEntity, Level pLevel) {
        super(pEntityType, livingEntity, pLevel);
    }
    
    //time the projectile stays in existence
    int SECONDS = 5;
    int TICK_MULTI = 20;
    //have to use this to prevent race condition with removal of entity
    boolean didExpireEffects = false;
    @Override
    public void tick() {
        super.tick();
        magnetizeEntities();
        
        //particle effect while sitting
        double PARTICLE_Y_SCALE = 0.3D;
        double PARTICLE_XZ_SCALE = 0.15D;
        if(this.inGround) {
            if(this.tickCount % 20 == 0 && this.level().isClientSide()) {
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getX(), this.getY() + 0.1, this.getZ() - 0.25,
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE, (this.random.nextDouble() - 0.5D) * PARTICLE_Y_SCALE,
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE);
            }
            if(this.tickCount % 20 == 3 && this.level().isClientSide()) {
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getX() + 0.25, this.getY() + 0.7, this.getZ(),
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE, (this.random.nextDouble() - 0.5D) * PARTICLE_Y_SCALE,
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE);
            }
            if(this.tickCount % 20 == 5 && this.level().isClientSide()) {
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getX(), this.getY() + 0.4, this.getZ() + 0.25,
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE, (this.random.nextDouble() - 0.5D) * PARTICLE_Y_SCALE,
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE);
            }
            if(this.tickCount % 20 == 7 && this.level().isClientSide()) {
                this.level().addParticle(ParticleTypes.SCULK_SOUL, this.getX() - 0.6, this.getY() + 0.25, this.getZ(),
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE, (this.random.nextDouble() - 0.5D) * PARTICLE_Y_SCALE,
                        (this.random.nextDouble() - 0.5D) * PARTICLE_XZ_SCALE);
            }
        }
        else {
            this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        
        //need to separate this by a tick from the expiration effects, otherwise the effects inconsistently trigger
        if(didExpireEffects) {
            this.remove(RemovalReason.KILLED);
        }
        //effects to be done as the projectile is removed from the world
        if(this.inGround && this.inGroundTime >= (SECONDS * TICK_MULTI)) {
            fakeExplosion();
            didExpireEffects = true;
        }
        //don't let the projectile fly for too long either
        if(this.tickCount >= ((SECONDS * 2) * TICK_MULTI)) {
            fakeExplosion();
            didExpireEffects = true;
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        setSoundEvent(ModSoundEvents.VOID_FIZZLE.get());
        setCritArrow(true);
        super.onHitEntity(pResult);
        Entity projSpawner = this.getOwner();
        if(projSpawner instanceof Player shooter && !pResult.getEntity().is(projSpawner)) {
            float projectileDmg = PrismaticBladeMk2.calcProjectileDmg(shooter);
            setBaseDamage(projectileDmg);
            pResult.getEntity().hurt(ModDamageTypes.causeQuantumDamage(shooter), projectileDmg);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    protected ItemStack getPickupItem() {
        return Items.DIAMOND.getDefaultInstance();
    }
    
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
        super.onHitBlock(pResult);
    }
    
    private void magnetizeEntities() {
        //constant values to tweak for projectile parameters
        //range the projectile searches for entities
        float MAG_RADIUS = 1.75f;
        //how strong the pull is from the projectile
        double MAG_STRENGTH = 0.05;
        if(this.getOwner() != null) {
            Entity projSpawner = this.getOwner();
            float projectileDmg = 1;
            if(projSpawner instanceof Player shooter) {
                projectileDmg = PrismaticBladeMk2.calcProjectileDmg(shooter);
            }
            //gets the entities around the projectile
            List<Entity> entities = this.level().getEntities(this.getOwner(), this.getBoundingBox().inflate(MAG_RADIUS));
            for (Entity instance : entities) {
                if (instance instanceof LivingEntity && !instance.is(this.getOwner())) {
                    if(instance.distanceTo(this) <= 1.0) {
                        instance.hurt(ModDamageTypes.causeQuantumDamage(this.getOwner()), projectileDmg);
                    }
                    //pull mob towards projectile
                    if (this.getX() > instance.getX() && instance.getDeltaMovement().x() < 1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x() + MAG_STRENGTH, instance.getDeltaMovement().y(), instance.getDeltaMovement().z());
                    } else if (this.getX() < instance.getX() && instance.getDeltaMovement().x() > -1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x() - MAG_STRENGTH, instance.getDeltaMovement().y(), instance.getDeltaMovement().z());
                    }
                    if (this.getY() > instance.getY() && instance.getDeltaMovement().y() < 1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y() + MAG_STRENGTH, instance.getDeltaMovement().z());
                    } else if (this.getY() < instance.getY() && instance.getDeltaMovement().y() > -1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y() - MAG_STRENGTH, instance.getDeltaMovement().z());
                    }
                    if (this.getZ() > instance.getZ() && instance.getDeltaMovement().z() < 1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y(), instance.getDeltaMovement().z() + MAG_STRENGTH);
                    } else if (this.getZ() < instance.getZ() && instance.getDeltaMovement().z() > -1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y(), instance.getDeltaMovement().z() - MAG_STRENGTH);
                    }
                }
            }
        }
    }
    
    private void fakeExplosion() {
        this.level().playSound(this, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0f, (1.0f + (this.level()).getRandom().nextFloat() * 0.05f));
        for(int i = 0; i < 2; i++) {
            this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX() + 1.5 * (this.random.nextDouble()), this.getY() + 1.5 * (this.random.nextDouble()), this.getZ() + 1.5 * (this.random.nextDouble()), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D));
            this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX() - 1.5 * (this.random.nextDouble()), this.getY() + 1.5 * (this.random.nextDouble()), this.getZ() - 1.5 * (this.random.nextDouble()), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D));
            this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX() + 1.5 * (this.random.nextDouble()), this.getY() - 1.5 * (this.random.nextDouble()), this.getZ() + 1.5 * (this.random.nextDouble()), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D));
            this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX() - 1.5 * (this.random.nextDouble()), this.getY() - 1.5 * (this.random.nextDouble()), this.getZ() - 1.5 * (this.random.nextDouble()), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D), (this.random.nextDouble() - 0.5D));
        }
        //fake explosion since real explosion destroys items
        Entity owner = this.getOwner();
        if(owner instanceof Player player) {
            List<Entity> entities = this.level().getEntities(player, this.getBoundingBox().inflate(2));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity mob) {
                    mob.hurt(ModDamageTypes.causeQuantumDamage(player), 5);
                }
            }
        }
    }
}
