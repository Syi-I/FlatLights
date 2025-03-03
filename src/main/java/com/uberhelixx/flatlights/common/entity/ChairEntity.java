package com.uberhelixx.flatlights.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class ChairEntity extends Entity {
    private BlockPos source;

    public ChairEntity(Level level)
    {
        super(ModEntityTypes.CHAIR_ENTITY.get(), level);
        this.noPhysics = true;
    }

    private ChairEntity(Level level, BlockPos source, double yOffset)
    {
        this(level);
        this.source = source;
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    public ChairEntity(EntityType<ChairEntity> chairEntityEntityType, Level level) {
        super(chairEntityEntityType, level);

    }
    
    @Override
    protected void defineSynchedData() {
    
    }
    
    @Override
    public void tick()
    {
        super.tick();
        if(source == null)
        {
            source = this.blockPosition();
        }
        if(!this.level().isClientSide)
        {
            //remove this entity if nothing is sitting in it or actual chair block is removed
            if(this.getPassengers().isEmpty() || this.level().isEmptyBlock(source))
            {
                this.remove(RemovalReason.DISCARDED);
                level().updateNeighbourForOutputSignal(blockPosition(), level().getBlockState(blockPosition()).getBlock());
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
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    
    }
    
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
    
    }

    @Override
    public double getPassengersRidingOffset()
    {
        return 0.0;
    }

    public BlockPos getSource()
    {
        return source;
    }

    //make into rideable entity
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

    //makes chair entity for player to sit in
    public static InteractionResult create(Level level, BlockPos pos, double yOffset, Player player)
    {
        if(!level.isClientSide)
        {
            List<ChairEntity> seats = level.getEntitiesOfClass(ChairEntity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
            if(seats.isEmpty())
            {
                ChairEntity chair = new ChairEntity(level, pos, yOffset);
                level.addFreshEntity(chair);
                player.startRiding(chair, false);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
