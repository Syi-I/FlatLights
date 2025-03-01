package com.uberhelixx.flatlights.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BombSwingEntity extends AbstractArrowEntity {
    protected BombSwingEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BombSwingEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public BombSwingEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
    }

    //explosion radius
    private final float bombRadius = 3.0f;

    @Override
    public void tick() {
        super.tick();
        //explode if it hits the ground
        if (this.timeInGround > 1){
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), bombRadius, false, Explosion.Mode.NONE);
            this.remove();
        }
        //remove if projectile sits and doesn't do anything for 10 seconds
        if(this.ticksExisted > 200) {
            this.remove();
        }
    }

    //no item stack since this isn't meant to be picked up
    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    //if the projectile hits something it causes an explosion
    @Override
    protected void onEntityHit(EntityRayTraceResult ray) {
        super.onEntityHit(ray);
        //this, x, y, z, explosionStrength, setsFires, breakMode
        this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), bombRadius, false, Explosion.Mode.NONE);
        this.remove();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
