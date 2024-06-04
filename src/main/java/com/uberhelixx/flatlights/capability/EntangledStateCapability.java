package com.uberhelixx.flatlights.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class EntangledStateCapability {
    @CapabilityInject(EntangledState.class)
    public static Capability<EntangledState> CAPABILITY_ENTANGLED_STATE = null;
    
    public static void register() {
        CapabilityManager.INSTANCE.register(
            EntangledState.class,
            new EntangledState.EntangledStateNBTStorage(),
            EntangledState::createDefaultInstance
        );
    }
}