package com.uberhelixx.flatlights.capability;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RisingHeatStateProvider implements ICapabilitySerializable<INBT> {
    
    public static final ResourceLocation ID = new ResourceLocation(FlatLights.MOD_ID, "rising_heat_state");
    private final Direction NO_SPECIFIC_SIDE = null;
    private RisingHeatState risingHeatState = new RisingHeatState();
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (RisingHeatStateCapability.CAPABILITY_HEATED_STATE == cap) {
            return (LazyOptional<T>)LazyOptional.of(()-> risingHeatState);
        }
        
        return LazyOptional.empty();
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        INBT heated = RisingHeatStateCapability.CAPABILITY_HEATED_STATE.writeNBT(risingHeatState, NO_SPECIFIC_SIDE);
        tag.put(IRisingHeat.RISING_HEAT_KEY, heated);
        return tag;
    }
    
    @Override
    public void deserializeNBT(INBT nbt) {
        if(nbt.getId() != new CompoundNBT().getId()) {
            return;
        }
        CompoundNBT tag = (CompoundNBT) nbt;
        INBT data = tag.get(IRisingHeat.RISING_HEAT_KEY);
        RisingHeatStateCapability.CAPABILITY_HEATED_STATE.readNBT(risingHeatState, NO_SPECIFIC_SIDE, data);
    }
    
    public static LazyOptional<RisingHeatState> getHeatedState(LivingEntity entityIn) {
        return entityIn.getCapability(RisingHeatStateCapability.CAPABILITY_HEATED_STATE);
    }
    
    /**
     * Event handler for the {@link IRisingHeat} capability.
     */
    @Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
    public static class RisingHeatStateProviderEventHandler {
        
        /**
         * Attach the {@link IRisingHeat} capability to all living entities.
         * @param event The event
         */
        @SubscribeEvent
        public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
            //make sure we only put heated state on living entities
            if (event.getObject() instanceof LivingEntity) {
                //MiscHelpers.debugLogger("[attach capability event] added heated state to mob");
                event.addCapability(ID, new RisingHeatStateProvider());
            }
        }
    }
}
