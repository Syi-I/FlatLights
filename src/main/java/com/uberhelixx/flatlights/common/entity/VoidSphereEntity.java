package com.uberhelixx.flatlights.common.entity;

import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class VoidSphereEntity extends AbstractArrowEntity {
    protected VoidSphereEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public VoidSphereEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public VoidSphereEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
    }

    @Override
    public void tick() {
        //time the black hole stays in existence
        int SECONDS = 8;
        int TICK_MULTI = 20;
        //super.tick();
        damageInRadius();
        //spawns particles once a second while the entity exists
        double PARTICLE_Y_OFFSET = 1;
        if(this.ticksExisted % 20 == 0) {
            if(world.isRemote()) {
                double PARTICLE_SPEED_MULTI = 0.9D;
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
                            this.getPosX(), this.getPosY() + 1, this.getPosZ(),
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
        PlayerEntity shooter = this.getShooter() instanceof PlayerEntity ? (PlayerEntity) this.getShooter() : null;
        //should always be a player shooting this projectile since it's the unusable dev sword
        if(shooter == null) {
            return;
        }
        //pull damage tag from sword to set damage values
        CompoundNBT tag = shooter.getHeldItem(Hand.MAIN_HAND).getItem() instanceof PrismaticBladeMk2 ? shooter.getHeldItem(Hand.MAIN_HAND).getTag() : null;
        //if tag present with totalBonus then get damage, otherwise just get 1
        int projectileBonus = tag != null && tag.contains(PrismaticBladeMk2.TOTAL_CORES_TAG) && tag.getInt(PrismaticBladeMk2.TOTAL_CORES_TAG) > 0 ? tag.getInt(PrismaticBladeMk2.TOTAL_CORES_TAG) : 1;
        int tier = tag != null && tag.contains(PrismaticBladeMk2.TIER_TAG) ? tag.getInt(PrismaticBladeMk2.TIER_TAG) + 1 : 1;
        float projectileDmg = (projectileBonus * ((float) tier / PrismaticBladeMk2.TOTAL_TIERS)) / 2;
        List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this.getShooter(), this.getBoundingBox().grow(5));
        for (Entity instance : entities) {
            if (instance instanceof LivingEntity) {
                //damage mobs in radius
                if(instance.getDistance(this) <= 2) {
                    instance.attackEntityFrom(ModDamageTypes.causeIndirectQuantum(this, this.getShooter()), projectileDmg);
                }
                //pull mob towards sphere
                else {
                    double SUCC_POWER = 0.2;
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
