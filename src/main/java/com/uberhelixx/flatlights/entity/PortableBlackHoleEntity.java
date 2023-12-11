package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class PortableBlackHoleEntity extends AbstractArrowEntity {
    public PortableBlackHoleEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public PortableBlackHoleEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public PortableBlackHoleEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
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
        if(this.ticksExisted % 20 == 0) {
            if(world.isRemote()) {
                double PARTICLE_SPEED_MULTI = 0.75D;
                for(int i = 0; i < 5; i++) {
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX(), this.getPosY() + PARTICLE_Y_OFFSET, this.getPosZ(),
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX(), this.getPosY() + PARTICLE_Y_OFFSET, this.getPosZ(),
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX(), this.getPosY() + PARTICLE_Y_OFFSET, this.getPosZ(),
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX(), this.getPosY() + PARTICLE_Y_OFFSET, this.getPosZ(),
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI, (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI,
                            (this.rand.nextDouble() - 0.5D) * PARTICLE_SPEED_MULTI);
                }
            }
        }
        if(this.ticksExisted % (SECONDS * TICK_MULTI) == 5) {
            this.getEntityWorld().playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSoundEvents.VOID_HUM.get(), SoundCategory.AMBIENT, 0.55f, (1.0f + (this.getEntityWorld().rand.nextFloat() * 0.05f)), true);
        }
        //what happens when we want to remove the entity
        if(this.ticksExisted > (SECONDS * TICK_MULTI)) {
            this.remove();
            for(int i = 0; i < 360; i++) {
                if(i % 10 == 0) {
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH,
                            this.getPosX(), this.getPosY() + PARTICLE_Y_OFFSET, this.getPosZ(),
                            Math.cos(i) * 0.5d, 0.0, Math.sin(i) * 0.5d);
                }
            }
            this.getEntityWorld().playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSoundEvents.VOID_FIZZLE.get(), SoundCategory.AMBIENT, 0.35f, (1.0f + (this.getEntityWorld().rand.nextFloat() * 0.05f)), true);
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult ray) {
        //super.onEntityHit(ray);
        //ray.getEntity().attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getShooter()), 1);
    }

    //on block collision
    @Override
    protected void func_230299_a_(BlockRayTraceResult ray) {
        //super.func_230299_a_(ray);
        //BlockState theBlockYouHit = this.world.getBlockState(ray.getPos());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void damageInRadius() {
        //constant values to tweak for black hole parameters
        //damage that being in the black hole does
        float PROJECTILE_DMG = 1.5f;
        //range the black hole searches for entities
        float SUCC_RADIUS = 4;
        //how strong the pull is from the black hole
        double SUCC_POWER = 0.2;

        //gets the entities around the black hole
        List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this.getShooter(), this.getBoundingBox().grow(SUCC_RADIUS));
        for (Entity instance : entities) {
            if (instance instanceof LivingEntity) {
                //damage mobs in radius
                if(instance.getDistance(this) <= 1.25) {
                    instance.attackEntityFrom(ModDamageTypes.causeIndirectQuantum(this, this.getShooter()), PROJECTILE_DMG);
                }
                //pull mob towards sphere
                else {
                    if(this.getPosX() > instance.getPosX() && instance.getMotion().getX() < 1) {
                        instance.setMotion(instance.getMotion().getX() + SUCC_POWER, instance.getMotion().getY(), instance.getMotion().getZ());
                    }
                    else if(this.getPosX() < instance.getPosX() && instance.getMotion().getX() > -1) {
                        instance.setMotion(instance.getMotion().getX() - SUCC_POWER, instance.getMotion().getY(), instance.getMotion().getZ());
                    }
                    if(this.getPosY() > instance.getPosY() && instance.getMotion().getY() < 1) {
                        instance.setMotion(instance.getMotion().getX(), instance.getMotion().getY() + SUCC_POWER, instance.getMotion().getZ());
                    }
                    else if(this.getPosY() < instance.getPosY() && instance.getMotion().getY() > -1) {
                        instance.setMotion(instance.getMotion().getX(), instance.getMotion().getY() - SUCC_POWER, instance.getMotion().getZ());
                    }
                    if(this.getPosZ() > instance.getPosZ() && instance.getMotion().getZ() < 1) {
                        instance.setMotion(instance.getMotion().getX(), instance.getMotion().getY(), instance.getMotion().getZ() + SUCC_POWER);
                    }
                    else if(this.getPosZ() < instance.getPosZ() && instance.getMotion().getZ() > -1) {
                        instance.setMotion(instance.getMotion().getX(), instance.getMotion().getY(), instance.getMotion().getZ() - SUCC_POWER);
                    }
                }
            }
        }
    }
}
