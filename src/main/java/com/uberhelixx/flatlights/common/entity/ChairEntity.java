package com.uberhelixx.flatlights.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class ChairEntity extends Entity {
    public ChairEntity(Level level)
    {
        super(ModEntityTypes.CHAIR_ENTITY.get(), level);
        this.noPhysics = true;
    }
    
    private ChairEntity(Level level, BlockPos source, double yOffset, Direction direction)
    {
        this(level);
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
        this.setRot(direction.getOpposite().toYRot(), 0F);
    }
    
    public ChairEntity(EntityType<ChairEntity> chairEntityEntityType, Level level) {
        super(chairEntityEntityType, level);
    }
    
    @Override
    public void tick()
    {
        super.tick();
        if(!this.level().isClientSide)
        {
            if(this.getPassengers().isEmpty() || this.level().isEmptyBlock(this.blockPosition()))
            {
                this.remove(RemovalReason.DISCARDED);
                this.level().updateNeighbourForOutputSignal(blockPosition(), this.level().getBlockState(blockPosition()).getBlock());
            }
            //if someone is sitting in the chair give regen and saturation
            if(!this.getPassengers().isEmpty()) {
                List<Entity> passengers = this.getPassengers();
                for(Entity passenger : passengers) {
                    if(passenger instanceof LivingEntity) {
                        //amount of seconds that this potion effect should last is multiplied by 20 since 20 ticks per second ingame
                        int seconds = 1;
                        ((LivingEntity) passenger).addEffect(new MobEffectInstance(MobEffects.REGENERATION, seconds * 20, 2, true, false));
                        ((LivingEntity) passenger).addEffect(new MobEffectInstance(MobEffects.SATURATION, seconds * 20, 0, true, false));
                    }
                }
            }
        }
    }
    
    @Override
    protected void defineSynchedData() {}
    
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}
    
    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}
    
    @Override
    public double getPassengersRidingOffset()
    {
        return 0.0;
    }
    
    @Override
    protected boolean canRide(Entity entity)
    {
        return true;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    public static InteractionResult create(Level level, BlockPos pos, double yOffset, Player player, Direction direction)
    {
        if(!level.isClientSide())
        {
            List<ChairEntity> seats = level.getEntitiesOfClass(ChairEntity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
            if(seats.isEmpty())
            {
                ChairEntity seat = new ChairEntity(level, pos, yOffset, direction);
                level.addFreshEntity(seat);
                player.startRiding(seat, false);
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity)
    {
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};
        for(Direction dir : offsets)
        {
            Vec3 safeVec = DismountHelper.findSafeDismountLocation(entity.getType(), this.level(), this.blockPosition().relative(dir), false);
            if(safeVec != null)
            {
                return safeVec.add(0, 0.25, 0);
            }
        }
        return super.getDismountLocationForPassenger(entity);
    }
    
    @Override
    protected void addPassenger(Entity entity)
    {
        super.addPassenger(entity);
        entity.setYRot(this.getYRot());
    }
    
    @Override
    public void positionRider(Entity entity, Entity.MoveFunction function)
    {
        super.positionRider(entity, function);
        this.clampYaw(entity);
    }
    
    @Override
    public void onPassengerTurned(Entity entity)
    {
        this.clampYaw(entity);
    }
    
    private void clampYaw(Entity passenger)
    {
        passenger.setYBodyRot(this.getYRot());
        float wrappedYaw = Mth.wrapDegrees(passenger.getYRot() - this.getYRot());
        float clampedYaw = Mth.clamp(wrappedYaw, -120.0F, 120.0F);
        passenger.yRotO += clampedYaw - wrappedYaw;
        passenger.setYRot(passenger.getYRot() + clampedYaw - wrappedYaw);
        passenger.setYHeadRot(passenger.getYRot());
    }
}
