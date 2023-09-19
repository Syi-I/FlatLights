package com.uberhelixx.flatlights.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class ChairEntity extends Entity {
    private BlockPos source;

    public ChairEntity(World world)
    {
        super(ModEntityTypes.CHAIR_ENTITY.get(), world);
        this.noClip = true;
    }

    private ChairEntity(World world, BlockPos source, double yOffset)
    {
        this(world);
        this.source = source;
        this.setPosition(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    public ChairEntity(EntityType<ChairEntity> chairEntityEntityType, World world) {
        super(chairEntityEntityType, world);

    }

    @Override
    protected void registerData() {

    }

    @Override
    public void tick()
    {
        super.tick();
        if(source == null)
        {
            source = this.getPosition();
        }
        if(!this.world.isRemote())
        {
            //remove this entity if nothing is sitting in it or actual chair block is removed
            if(this.getPassengers().isEmpty() || this.world.isAirBlock(source))
            {
                this.remove();
                world.updateComparatorOutputLevel(getPosition(), world.getBlockState(getPosition()).getBlock());
            }
            //if someone is sitting in the chair give regen and saturation
            if(!this.getPassengers().isEmpty()) {
                List<Entity> passengers = this.getPassengers();
                for(Entity passenger : passengers) {
                    if(passenger instanceof LivingEntity) {
                        ((LivingEntity) passenger).addPotionEffect(new EffectInstance(Effects.REGENERATION, 5 * 20, 3, true, false));
                        ((LivingEntity) passenger).addPotionEffect(new EffectInstance(Effects.SATURATION, 5 * 20, 0, true, false));
                    }
                }
            }
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public double getMountedYOffset()
    {
        return 0.0;
    }

    public BlockPos getSource()
    {
        return source;
    }

    //make into rideable entity
    @Override
    protected boolean canBeRidden(Entity entity)
    {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //makes chair entity for player to sit in
    public static ActionResultType create(World world, BlockPos pos, double yOffset, PlayerEntity player)
    {
        if(!world.isRemote())
        {
            List<ChairEntity> seats = world.getEntitiesWithinAABB(ChairEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
            if(seats.isEmpty())
            {
                ChairEntity chair = new ChairEntity(world, pos, yOffset);
                world.addEntity(chair);
                player.startRiding(chair, false);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
