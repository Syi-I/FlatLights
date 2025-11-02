package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.startup.registry.ModSoundEvents;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class GravityLiftEntity extends AbstractArrow {
    protected GravityLiftEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    public GravityLiftEntity(Level pLevel) {
        super(ModEntityTypes.GRAVITY_LIFT_ENTITY.get(), pLevel);
    }
    
    public GravityLiftEntity(EntityType<? extends AbstractArrow> pEntityType, LivingEntity livingEntity, Level pLevel) {
        super(pEntityType, livingEntity, pLevel);
    }
    
    //time the gravity lift stays in existence
    public static final int SECONDS = FlatLightsCommonConfig.gravityLiftTime.get() != null ? FlatLightsCommonConfig.gravityLiftTime.get() : 10;
    int TICK_MULTI = 20;
    @Override
    public void tick() {
        liftUp();
        //spawns particles once a second while the entity exists
        if(this.tickCount % 20 == 0) {
            if(level().isClientSide()) {
                for(int i = 0; i < 360; i++) {
                    if(i % 20 == 0) {
                        this.level().addParticle(ParticleTypes.DRAGON_BREATH,
                                this.getX(), this.getY(), this.getZ(),
                                Math.cos(i) * 0.025d, 0.125, Math.sin(i) * 0.025d);
                    }
                }
            }
        }
        //using time of sound effect in seconds to loop sound, in this case the clip is 5 seconds long so 5 * game tick rate
        if(this.tickCount % (5 * TICK_MULTI) == 5) {
            float nextFloatPitch = this.level().random.nextFloat();
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSoundEvents.GRAVITY_LIFT_HUM.get(), SoundSource.AMBIENT, 0.80f, (1.0f + (nextFloatPitch * 0.05f)), true);
        }
        //what happens when we want to remove the entity
        if(this.tickCount > (SECONDS * TICK_MULTI)) {
            this.remove(RemovalReason.DISCARDED);
        }
    }
    
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    protected ItemStack getPickupItem() {
        return ModItems.GUN_RAT.get().getDefaultInstance();
    }
    
    private void liftUp() {
        float LIFT_SPEED = 0.5F;
        //gets the entities touching the gravity lift
        List<Entity> entities = this.level().getEntities(this.getOwner(), this.getBoundingBox());
        for (Entity instance : entities) {
            //sneak check for players for dismounting early from the lift with momentum
            boolean hopOut = false;
            if(instance instanceof Player) {
                if(instance.isCrouching()) {
                    hopOut = true;
                }
            }
            
            //height buffer for the top of the lift
            final float HEIGHT_BUFFER = 0.375F;
            
            //lower lift speed at the top so you bounce less
            if(instance.distanceTo(this) > (ModEntityTypes.GRAV_LIFT_HEIGHT - HEIGHT_BUFFER) && this.tickCount < (SECONDS - 1) * TICK_MULTI && !hopOut) {
                instance.setDeltaMovement(instance.getDeltaMovement().x(), LIFT_SPEED * 0, instance.getDeltaMovement().z());
            }
            //if lift is expiring or player is trying to jump out, give motion increase
            else if(instance.distanceTo(this) > (ModEntityTypes.GRAV_LIFT_HEIGHT - HEIGHT_BUFFER) && (this.tickCount >= (SECONDS - 1) * TICK_MULTI || hopOut)) {
                if(hopOut) {
                    MiscUtils.infoLog("[Gravity Lift Entity] Player should be launching out after sneaking in lift.");
                }
                
                float SPEED_MULTI = 1.75F;
                instance.setDeltaMovement(instance.getDeltaMovement().x() * (SPEED_MULTI + 1.5), LIFT_SPEED * (SPEED_MULTI), instance.getDeltaMovement().z() * (SPEED_MULTI + 1.75));
            }
            //normal lift behavior while still traveling up
            else {
                instance.setDeltaMovement(instance.getDeltaMovement().x(), LIFT_SPEED, instance.getDeltaMovement().z());
            }
        }
    }
}
