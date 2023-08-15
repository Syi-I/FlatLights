package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

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
        //super.tick();
        damageInRadius();
        if(this.ticksExisted % 20 == 0) {
            if(world.isRemote()) {
                for(int i = 0; i < 5; i++) {
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX() + 1, this.getPosY() + 1.5, this.getPosZ(),
                            (this.rand.nextDouble() - 0.5D) * 2.0D, (this.rand.nextDouble() - 0.5D) * 2.0D,
                            (this.rand.nextDouble() - 0.5D) * 2.0D);
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX() - 1, this.getPosY()+ 1.5, this.getPosZ(),
                            (this.rand.nextDouble() - 0.5D) * 2.0D, (this.rand.nextDouble() - 0.5D) * 2.0D,
                            (this.rand.nextDouble() - 0.5D) * 2.0D);
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX(), this.getPosY()+ 1.5, this.getPosZ() + 1,
                            (this.rand.nextDouble() - 0.5D) * 2.0D, (this.rand.nextDouble() - 0.5D) * 2.0D,
                            (this.rand.nextDouble() - 0.5D) * 2.0D);
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH, this.getPosX(), this.getPosY()+ 1.5, this.getPosZ() - 1,
                            (this.rand.nextDouble() - 0.5D) * 2.0D, (this.rand.nextDouble() - 0.5D) * 2.0D,
                            (this.rand.nextDouble() - 0.5D) * 2.0D);
                }
            }
        }
        if(this.ticksExisted > 160) {
            this.remove();
            for(int i = 0; i < 360; i++) {
                if(i % 10 == 0) {
                    this.getEntityWorld().addParticle(ParticleTypes.DRAGON_BREATH,
                            this.getPosX() + 0.5d, this.getPosY() + 1, this.getPosZ() + 0.5d,
                            Math.cos(i) * 0.5d, 0.5d, Math.sin(i) * 0.5d);
                }
            }
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
        super.func_230299_a_(ray);
        BlockState theBlockYouHit = this.world.getBlockState(ray.getPos());
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
        int projectileBonus = tag != null && tag.contains("totalBonus") ? tag.getInt("totalBonus") / 10 : 1;
        List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this.getShooter(), this.getBoundingBox().grow(5));
        for (Entity instance : entities) {
            if (instance instanceof LivingEntity) {
                //damage mobs in radius
                if(instance.getDistance(this) <= 2) {
                    instance.attackEntityFrom(ModDamageTypes.causeIndirectQuantum(this, this.getShooter()), projectileBonus);
                }
                //pull mob towards sphere
                else {
                    double SUCC_POWER = 0.1;
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
