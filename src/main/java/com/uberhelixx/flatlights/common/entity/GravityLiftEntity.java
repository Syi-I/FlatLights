package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class GravityLiftEntity extends AbstractArrowEntity {
    public GravityLiftEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public GravityLiftEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public GravityLiftEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
    }

    //time the gravity lift stays in existence
    public static final int SECONDS = FlatLightsCommonConfig.gravityLiftTime.get() != null ? FlatLightsCommonConfig.gravityLiftTime.get() : 10;
    int TICK_MULTI = 20;
    @Override
    public void tick() {
        liftUp();
        //spawns particles once a second while the entity exists
        if(this.ticksExisted % 20 == 0) {
            if(world.isRemote()) {
                for(int i = 0; i < 360; i++) {
                    if(i % 20 == 0) {
                        this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH,
                                this.getPosX(), this.getPosY(), this.getPosZ(),
                                Math.cos(i) * 0.025d, 0.125, Math.sin(i) * 0.025d);
                    }
                }
            }
        }
        //using time of sound effect in seconds to loop sound, in this case the clip is 5 seconds long so 5 * game tick rate
        if(this.ticksExisted % (5 * TICK_MULTI) == 5) {
            float nextFloatPitch = this.getEntityWorld().rand.nextFloat();
            this.getEntityWorld().playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSoundEvents.GRAVITY_LIFT_HUM.get(), SoundCategory.AMBIENT, 0.80f, (1.0f + (nextFloatPitch * 0.05f)), true);
        }
        //what happens when we want to remove the entity
        if(this.ticksExisted > (SECONDS * TICK_MULTI)) {
            this.remove();
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult ray) {

    }

    //on block collision
    @Override
    protected void func_230299_a_(BlockRayTraceResult ray) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void liftUp() {
        float LIFT_SPEED = 0.5F;
        //gets the entities touching the gravity lift
        List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this.getShooter(), this.getBoundingBox());
        for (Entity instance : entities) {
            //sneak check for players for dismounting early from the lift with momentum
            boolean hopOut = false;
            if(instance instanceof PlayerEntity) {
                if(instance.isSneaking()) {
                    hopOut = true;
                }
            }

            //height buffer for the top of the lift
            final float HEIGHT_BUFFER = 0.375F;

            //lower lift speed at the top so you bounce less
            if(instance.getDistance(this) > (ModEntityTypes.GRAV_LIFT_HEIGHT - HEIGHT_BUFFER) && this.ticksExisted < (SECONDS - 1) * TICK_MULTI && !hopOut) {
                instance.setMotion(instance.getMotion().getX(), LIFT_SPEED * 0, instance.getMotion().getZ());
            }
            //if lift is expiring or player is trying to jump out, give motion increase
            else if(instance.getDistance(this) > (ModEntityTypes.GRAV_LIFT_HEIGHT - HEIGHT_BUFFER) && (this.ticksExisted >= (SECONDS - 1) * TICK_MULTI || hopOut)) {
                if(hopOut) {
                    MiscHelpers.debugLogger("[Gravity Lift Entity] Player should be launching out after sneaking in lift.");
                }

                float SPEED_MULTI = 1.75F;
                instance.setMotion(instance.getMotion().getX() * (SPEED_MULTI + 1.5), LIFT_SPEED * (SPEED_MULTI), instance.getMotion().getZ() * (SPEED_MULTI + 1.75));
            }
            //normal lift behavior while still traveling up
            else {
                instance.setMotion(instance.getMotion().getX(), LIFT_SPEED, instance.getMotion().getZ());
            }
        }
    }
}
