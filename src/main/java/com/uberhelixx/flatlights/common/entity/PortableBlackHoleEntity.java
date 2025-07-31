package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.startup.registry.ModSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class PortableBlackHoleEntity extends AbstractArrow {
    protected PortableBlackHoleEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    public PortableBlackHoleEntity(Level pLevel) {
        super(ModEntityTypes.PORTABLE_BLACK_HOLE_ENTITY.get(), pLevel);
    }
    
    public PortableBlackHoleEntity(EntityType<? extends AbstractArrow> pEntityType, LivingEntity livingEntity, Level pLevel) {
        super(pEntityType, livingEntity, pLevel);
    }
    
    //time the black hole stays in existence
    int SECONDS = 7;
    int TICK_MULTI = 20;
    @Override
    public void tick() {
        //super.tick();
        damageInRadius();
        //spawns particles once a second while the entity exists
        double PARTICLE_Y_OFFSET = 0.65;
        if(this.tickCount % 20 == 0) {
            if(level().isClientSide()) {
                double PARTICLE_SPEED_MULTI = 0.75D;
                for(int i = 0; i < 5; i++) {
                    this.level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + PARTICLE_Y_OFFSET, this.getZ(),
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                    this.level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + PARTICLE_Y_OFFSET, this.getZ(),
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                    this.level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + PARTICLE_Y_OFFSET, this.getZ(),
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                    this.level().addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY() + PARTICLE_Y_OFFSET, this.getZ(),
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.random.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                }
            }
        }
        if(this.tickCount % (SECONDS * TICK_MULTI) == 5) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSoundEvents.VOID_HUM.get(), SoundSource.AMBIENT, 0.55f, (1.0f + (this.level().random.nextFloat() * 0.05f)), true);
        }
        //what happens when we want to remove the entity
        if(this.tickCount > (SECONDS * TICK_MULTI)) {
            this.remove(RemovalReason.DISCARDED);
            for(int i = 0; i < 360; i++) {
                if(i % 10 == 0) {
                    this.level().addParticle(ParticleTypes.DRAGON_BREATH,
                            this.getX(), this.getY() + PARTICLE_Y_OFFSET, this.getZ(),
                            Math.cos(i) * 0.5d, 0.0, Math.sin(i) * 0.5d);
                }
            }
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSoundEvents.VOID_FIZZLE.get(), SoundSource.AMBIENT, 1.0f, (1.0f + (this.level().random.nextFloat() * 0.05f)), true);
        }
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
    
    private void damageInRadius() {
        //constant values to tweak for black hole parameters
        //damage that being in the black hole does
        float PROJECTILE_DMG = 1.5f;
        if(FlatLightsCommonConfig.portableBlackHoleDamage.get() != null) {
            PROJECTILE_DMG = FlatLightsCommonConfig.portableBlackHoleDamage.get().floatValue();
        }
        //range the black hole searches for entities
        float SUCC_RADIUS = 4;
        //how strong the pull is from the black hole
        double SUCC_POWER = 0.2;
        if(FlatLightsCommonConfig.portableBlackHoleSuckPower.get() != null) {
            SUCC_POWER = FlatLightsCommonConfig.portableBlackHoleSuckPower.get();
        }
        Entity projSpawner = this.getOwner();
        //MiscUtils.infoLog("[Portable Black Hole Entity] projectile owner is " + projSpawner);
        
        //gets the entities around the black hole
        List<Entity> entities = this.level().getEntities(this.getOwner(), this.getBoundingBox().inflate(SUCC_RADIUS));
        for (Entity instance : entities) {
            if (instance instanceof LivingEntity) {
                //damage mobs in radius
                if(instance.distanceTo(this) <= 1.25 && projSpawner != null) {
                    instance.hurt(ModDamageTypes.causeQuantumDamage(projSpawner), PROJECTILE_DMG);
                }
                //pull mob towards sphere
                else {
                    if(this.getX() > instance.getX() && instance.getDeltaMovement().x() < 1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x() + SUCC_POWER, instance.getDeltaMovement().y(), instance.getDeltaMovement().z());
                    }
                    else if(this.getX() < instance.getX() && instance.getDeltaMovement().x() > -1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x() - SUCC_POWER, instance.getDeltaMovement().y(), instance.getDeltaMovement().z());
                    }
                    if(this.getY() > instance.getY() && instance.getDeltaMovement().y() < 1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y() + SUCC_POWER, instance.getDeltaMovement().z());
                    }
                    else if(this.getY() < instance.getY() && instance.getDeltaMovement().y() > -1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y() - SUCC_POWER, instance.getDeltaMovement().z());
                    }
                    if(this.getZ() > instance.getZ() && instance.getDeltaMovement().z() < 1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y(), instance.getDeltaMovement().z() + SUCC_POWER);
                    }
                    else if(this.getZ() < instance.getZ() && instance.getDeltaMovement().z() > -1) {
                        instance.setDeltaMovement(instance.getDeltaMovement().x(), instance.getDeltaMovement().y(), instance.getDeltaMovement().z() - SUCC_POWER);
                    }
                }
            }
        }
    }
}
