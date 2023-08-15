package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class VoidProjectileEntity extends AbstractArrowEntity {
    protected VoidProjectileEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public VoidProjectileEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public VoidProjectileEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.timeInGround > 1){
            summonBlackHole(this.getPosX(), this.getPosY(), this.getPosZ());
            this.remove();
        }
        else if(this.getShooter() != null) {
            if (this.getDistance(this.getShooter()) >= 12) {
                summonBlackHole(this.getPosX(), this.getPosY(), this.getPosZ());
                this.remove();
            }
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
        summonBlackHole(this.getPosX(), this.getPosY(), this.getPosZ());
        this.remove();
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

    public void summonBlackHole(double x, double y, double z) {
        LivingEntity originalShooter = null;
        if(this.getShooter() != null) {
            MiscHelpers.debugLogger("[Void Sphere] shooter is not null");
            originalShooter = (LivingEntity) this.getShooter();
        }
        if(originalShooter == null) {
            return;
        }
        World worldIn = originalShooter.getEntityWorld();
        if (!worldIn.isRemote()){
            VoidSphereEntity orb = new VoidSphereEntity(ModEntityTypes.VOID_SPHERE.get(), (LivingEntity) this.getShooter(), this.getShooter().getEntityWorld());
            orb.setLocationAndAngles(x, y - 1.5, z, 0f, 0f);
            orb.setNoGravity(true);
            worldIn.addEntity(orb);
            MiscHelpers.debugLogger("[Void Sphere] Summon sphere");
        }
    }
}
