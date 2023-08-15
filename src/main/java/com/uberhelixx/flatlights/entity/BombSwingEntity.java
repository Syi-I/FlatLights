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

    private final float bombRadius = 3.0f;

    @Override
    public void tick() {
        super.tick();
        if (this.timeInGround > 1){
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), bombRadius, false, Explosion.Mode.NONE);
            this.remove();
        }
        if(this.ticksExisted > 200) {
            this.remove();
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

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
